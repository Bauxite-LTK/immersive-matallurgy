package net.bauxite_ltk.immersive_metallurgy.block.transporter.api;

import blusunrize.immersiveengineering.api.utils.SafeChunkUtils;
import blusunrize.immersiveengineering.common.blocks.ticking.IEServerTickableBE;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceHandler.IUniHandler;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.*;

public interface IBlocklikeResourceTransporter<R> extends IEServerTickableBE {

    Class<R> getResourceClass();

    IUniHandler<R> getSelfHandler();

    List<TransportationData> getTDList();

    byte getConnectionByte();

    BlockPos getBlockPos();

    Level getLevel();

    IUniHandler<R> getNeighborCapability(Direction direction);

    default boolean isNeighborCapabilityInvalid(Direction direction){
        return getNeighborCapability(direction)==null;
    }

    IBlocklikeResourceTransporter<?> getNeighborInstance(Direction direction);

    default boolean isNeighborInstanceInvalid(Direction direction){
        return getNeighborInstance(direction) == null;
    }

    void allocateResourceLocal(BlockFace sourceKey, boolean tryEmptySelf);

    //this needs to implement outside.

    boolean shouldTick();

    @Override
    default void tickServer(){
        if(shouldTick()){
            removeInvalidConnectionInfo();
            List<TransportationData> tdList = List.copyOf(getTDList());
            for(TransportationData data : tdList){
                BlockFace sourceKey = data.sourceKey;
                TransportationData.Status status = data.status;

                if(status.equals(TransportationData.Status.SOURCE)){
                    //if(sourceKey.faceDir == null) throw new RuntimeException("WTF");
                    //IMUtils.LOGGER.info("Source update subnet pos:{}", getBlockPos());
                    rootUpdateSubnet(sourceKey);
                    if(isNeighborCapabilityInvalid(data.input) || getSelfHandler().isAllEmpty()) {
                        //IMUtils.LOGGER.info("set subroot");
                        data.setStatus(TransportationData.Status.DRAIN);
                        data.removeInput();
                    }
                }

                else if(status.equals(TransportationData.Status.DRAIN)){
                    //IMUtils.LOGGER.info("drain update subnet pos:{}", getBlockPos());
                    BlockFace drainKey = new BlockFace(getBlockPos(),null);
                    subrootUpdateSubnet(sourceKey,
                            drainKey
                    );
                    if(isClaimedByOtherRoot(drainKey) || getSelfHandler().isAllEmpty()){
                        for(Direction nextDir : data.outputs){
                            if(!isNeighborInstanceInvalid(nextDir) && getNeighborInstance(nextDir).hasData(sourceKey)){
                                getNeighborInstance(nextDir).getDataBySourceKey(sourceKey).setStatus(TransportationData.Status.DRAIN);
                            }
                        }
                        data.setStatus(TransportationData.Status.INVALID);
                    }

                }

                else if(status.equals(TransportationData.Status.COMMON)){
                    if (isNeighborCapabilityInvalid(data.input)){
                        //IMUtils.LOGGER.info("common set sub source pos:{}", getBlockPos());
                        data.setStatus(TransportationData.Status.DRAIN);
                    }
                }

            }
        }
    }

    default void rootUpdateSubnet(BlockFace sourceKey){
        BlockPos sourceTransporterPos;
        try {
            sourceTransporterPos = sourceKey.pos.relative(sourceKey.faceDir);
        }
        catch(NullPointerException e){
            IMUtils.LOGGER.error("sourceKey.faceDir is null");
            return;
        }
        List<BlockPos> openList = new LinkedList<>();
        List<BlockPos> closeList = new LinkedList<>();
        openList.add(sourceTransporterPos);
        for(int i = 0; i < 1024; i++){
            //TFCTrihydrate.LOGGER.info("rootUpdateSubnet: i = {}", i);
            if(openList.isEmpty()) break;

            BlockPos curPos = openList.getFirst();
            openList.removeFirst();

            if(closeList.contains(curPos)) continue;

            BlockEntity be = null;
            if (getLevel() != null) be = SafeChunkUtils.getSafeBE(getLevel(), curPos);
            if(be == null) continue;

            if(be instanceof IBlocklikeResourceTransporter<?> blocklikeTransporter
                    && blocklikeTransporter.getResourceClass().equals(getResourceClass())){
                blocklikeTransporter.tryClaimNext(sourceKey);
                blocklikeTransporter.allocateResourceLocal(sourceKey, false);
                for(Direction direction : blocklikeTransporter.getDataBySourceKey(sourceKey).outputs){
                    openList.addLast(curPos.relative(direction));
                }
                closeList.addFirst(curPos);
            }
        }
    }

    default void subrootUpdateSubnet(BlockFace oldSourceKey, BlockFace newSourceKey){
        BlockPos sourceTransporterPos;
        sourceTransporterPos = newSourceKey.pos;

        List<BlockPos> openList = new LinkedList<>();
        List<BlockPos> closeList = new LinkedList<>();
        openList.add(sourceTransporterPos);
        for(int i = 0; i < 1024; i++){
            //TFCTrihydrate.LOGGER.info("rootUpdateSubnet: i = {}", i);
            if(openList.isEmpty()) break;
            BlockPos curPos = openList.getFirst();
            openList.removeFirst();
            if(closeList.contains(curPos)) continue;
            BlockEntity be = null;
            if (getLevel() != null) be = SafeChunkUtils.getSafeBE(getLevel(), curPos);
            if(be == null) continue;
            if(be instanceof IBlocklikeResourceTransporter<?> blocklikeTransporter
                    && blocklikeTransporter.getResourceClass().equals(getResourceClass())){

                if(oldSourceKey != newSourceKey && blocklikeTransporter.hasData(oldSourceKey)){
                    blocklikeTransporter.getDataBySourceKey(oldSourceKey).resetSourceForDrain(newSourceKey);
                }
                blocklikeTransporter.tryClaimNext(newSourceKey);
                blocklikeTransporter.allocateResourceLocal(newSourceKey, true);
                for(Direction direction : blocklikeTransporter.getDataBySourceKey(newSourceKey).outputs){
                    openList.addLast(curPos.relative(direction));
                }
                closeList.addFirst(curPos);
            }
        }
    }

    private void tryClaimNext(BlockFace sourceKey){

        // update nextList
        for(int i = 0; i < 6; i++) {
            Direction dir = Direction.from3DDataValue(i);
            //node should not check previous direction
            if(getDataBySourceKey(sourceKey).input == dir) continue;
            //check if the direction is connected
            if (((getConnectionByte() >> i) & 1) != 1) {
                getDataBySourceKey(sourceKey).removeOutput(dir);
                continue;
            }
            //check if the direction has available handler
            IUniHandler<R> handler = getNeighborCapability(dir);
            if(handler!=null /*&& handler.getTanks() > 0*/){
                getDataBySourceKey(sourceKey).addOutput(dir);

            }
            else{
                getDataBySourceKey(sourceKey).removeOutput(dir);
            }
        }

        // from nextList, try claim neighbor pipe
        for(Direction outputDir : getDataBySourceKey(sourceKey).outputs){
            IBlocklikeResourceTransporter<?> neighbor = getNeighborInstance(outputDir);
            int thisDepth = getDataBySourceKey(sourceKey).depthInNetwork;
            if(neighbor!=null){

                //if pipe is not in subnet, then add it
                if(!neighbor.hasData(sourceKey)){
                    neighbor.getTDList().add(TransportationData.forCommon(sourceKey, outputDir.getOpposite(), thisDepth+1));
                }

                // If depth of next is abnormally large, then reverse its next and previous direction
                // Note that depth will not smaller than thisDepth+1
                // Because we use BFS in an indirection graph, if a new connection links to a smaller depth
                // Then this path would have been detected earlier when BFS runs on the smaller depth node
                else if(neighbor.getDataBySourceKey(sourceKey).depthInNetwork > thisDepth + 1){
                    TransportationData neighborInfo = neighbor.getDataBySourceKey(sourceKey);
                    neighborInfo.removeInput();
                    neighborInfo.setInput(outputDir.getOpposite());
                    neighborInfo.removeOutput(outputDir.getOpposite());
                    neighborInfo.depthInNetwork = thisDepth + 1;
                }


                // When the depth of next is valid, nextList still can be invalid.
                // This will occur when a subpath join back and form a ring.
                // The path will rearrange, a series of node will be reversed.
                // But at last, two BFS branches will merge into a single node (Proof is Down Below this method)
                // At this point slower branch will see the depth is valid, but its nextList still needs to change.
                else if(neighbor.getDataBySourceKey(sourceKey).depthInNetwork == thisDepth + 1){
                    TransportationData neighborInfo = neighbor.getDataBySourceKey(sourceKey);
                    //try to remove invalid nextDir of neighbor node
                    if(neighborInfo.outputs.contains(outputDir.getOpposite())){
                        neighborInfo.removeOutput(outputDir.getOpposite());
                    }
                    //add multiple preDir.
                    neighborInfo.setInput(outputDir.getOpposite());
                }
                /* In a grid network, obviously a ring must contain even number of nodes.
                 *  While doing BFS, obviously every branch shares a same root.
                 *  When BFS branches b1 and b2 are going to merge,
                 *  we can construct a ring [b1_tail--root--b2_tail--b1_tail], call it LOOP_12;
                 *  Define assumption P: b1 and b2 will merge into an edge.
                 *  First We can consider LOOP_12's node count;
                 *  And the count is: node_count_of_b1 + node_count_of_b2 - 1, minus one because they share a same root;
                 *  Since we are doing BFS, b1's length is equal to b2, call it N;
                 *  Then the LOOP_12's total node count will simplify into 2*N - 1, which must be an odd;
                 *  but at first we know a ring in a grid network cannot form a ring with odd number of nodes;
                 *  Assumption P Contradicts to the basic facts, so the assumption P does not hold.
                 *  Then we proved BFS branches b1 and b2 must merge into a node, not an edge.
                 */

            }
        }
    }

    default int startAllocateResourceGlobal(Direction sourceDir, R resource, int amount, boolean simulate){
        BlockFace sourceKey = new BlockFace(getBlockPos().relative(sourceDir), sourceDir.getOpposite());
        int lastAmount = amount;
        BlockPos sourceTransporterPos;
        try {
            sourceTransporterPos = sourceKey.pos.relative(sourceKey.faceDir);
        }
        catch(NullPointerException e){
            IMUtils.LOGGER.error("startAllocateResourceGlobal: sourceKey.faceDir is null");
            return 0;
        }
        List<BlockPos> openList = new LinkedList<>();
        List<BlockPos> closeList = new LinkedList<>();
        openList.add(sourceTransporterPos);
        for(int i = 0; i < 1024; i++){
            //TFCTrihydrate.LOGGER.info("rootUpdateSubnet: i = {}", i);
            if(openList.isEmpty() || lastAmount == 0) break;

            BlockPos curPos = openList.getFirst();
            openList.removeFirst();

            if(closeList.contains(curPos)) continue;

            BlockEntity be = null;
            if (getLevel() != null) be = SafeChunkUtils.getSafeBE(getLevel(), curPos);
            if(be == null) continue;

            if(be instanceof IBlocklikeResourceTransporter<?> transporter
                    && transporter.getResourceClass().equals(getResourceClass())){
                @SuppressWarnings("unchecked")
                IBlocklikeResourceTransporter<R> typed = (IBlocklikeResourceTransporter<R>)transporter;
                int fill = typed.forceAllocateResource(sourceKey,resource,lastAmount,simulate);
                lastAmount -= fill;
                if(typed.hasData(sourceKey)) { // avoid this method execute earlier than current transporter is claimed by sourceKey
                    for (Direction direction : typed.getDataBySourceKey(sourceKey).outputs) {
                        openList.addLast(curPos.relative(direction));
                    }
                }
                closeList.addFirst(curPos);
            }
        }
        return amount - lastAmount;
    }

    int forceAllocateResource(BlockFace sourceKey ,R resource, int amount, boolean simulate);





    default TransportationData getDataBySourceKey(BlockFace source){
        List<TransportationData> tdList = getTDList();
        for(TransportationData data: tdList){
            if(data.sourceKey.equals(source)){
                return data;
            }
        }
        throw new RuntimeException("BlocklikeResourceTransporter: getData expected return nonNull but appears to return null");
    }

    private boolean hasData(BlockFace source){
        List<TransportationData> tdList = getTDList();
        for(TransportationData data: tdList){
            if(data.sourceKey.equals(source)){
                return true;
            }
        }
        return false;
    }

    private void removeInvalidConnectionInfo(){

        getTDList().removeIf(info -> info.status.equals(TransportationData.Status.INVALID));
    }

    private boolean isSelfSource(BlockFace sourceKey){
        List<TransportationData> tdList = getTDList();
        for(TransportationData data : tdList){
            if(data.sourceKey.equals(sourceKey)){
                return false;
            }
        }
        return true;
    }

    private boolean isClaimedByOtherRoot(BlockFace originalSource){
        List<TransportationData> tdList = getTDList();
        for(TransportationData data : tdList){
            if(data.sourceKey != originalSource && data.sourceKey.faceDir != null) return true;
        }
        return false;
    }

    // If fromDir already exists, means block at 'from' is also a IBlocklikeTransporter.
    // Because only IBlocklikeTransporter will set its next's 'from' before filling.
    // Then, if this is called and return true, means someone try to fill without set next's 'from'.
    // So it must be a new Source.
    private boolean isNewInput(Direction in){
        List<TransportationData> tdList = getTDList();
        for(TransportationData data : tdList){
            if(data.input == in){
                return false;
            }
        }
        return true;
    }

    default void trySetSource(Direction receiveFrom){
        if(this.isNewInput(receiveFrom)){
            IBlocklikeResourceTransporter.BlockFace sourceBlockFace = new IBlocklikeResourceTransporter.BlockFace(this.getBlockPos().relative(receiveFrom), receiveFrom.getOpposite());
            IMUtils.LOGGER.info("Set Source");
            if(this.isSelfSource(sourceBlockFace)){
                this.getTDList().add(TransportationData.forSource(sourceBlockFace));
            }
        }
    }

    default boolean isDirectionSourceFace(Direction direction){
        BlockFace otherFace = new BlockFace(getBlockPos().relative(direction), direction.getOpposite());
        return hasData(otherFace) && getDataBySourceKey(otherFace).status.equals(TransportationData.Status.SOURCE);
    }




    record BlockFace(BlockPos pos, Direction faceDir){}

    class TransportationData {

        BlockFace sourceKey;
        Status status;

        Direction input;
        List<Direction> outputs = new ArrayList<>();

        List<Pair<IFluidHandler, Integer>> localAllocateCache = new LinkedList<>();

        int depthInNetwork;


        private TransportationData(BlockFace sourceKey, Status status, Direction input, int depthInNetwork){
            this.sourceKey = sourceKey;
            this.status = status;
            this.input = input;
            this.depthInNetwork = depthInNetwork;
        }

        public static TransportationData forSource(BlockFace sourceKey){
            if(sourceKey.faceDir == null) throw new RuntimeException("Fuck");
            return new TransportationData(sourceKey, Status.SOURCE, sourceKey.faceDir.getOpposite(), 0);
        }

        public static TransportationData forCommon(BlockFace sourceFace, Direction input, int depth){
            return new TransportationData(sourceFace, Status.COMMON, input, depth);
        }

        public void setStatus(Status status){
            this.status = status;
        }

        public void resetSourceForDrain(BlockFace drainerKey){
            this.sourceKey = drainerKey;
        }

        public void addOutput(Direction output){
            if(!outputs.contains(output)){
                outputs.add(output);
            }
        }

        public void removeInput(){
            this.input = null;
        }

        public void setInput(Direction input){
            this.input = input;
        }

        public void removeOutput(Direction output){
            outputs.remove(output);
        }

        public void clearLocalAllocateCache(){
            localAllocateCache.clear();
        }

        public List<Pair<IFluidHandler, Integer>> getLocalAllocateCache(){
            return localAllocateCache;
        }




        /*-----------------------------------------------------------------------------------------------------
         [Strategy Overview]:
            The variance tryEmptySelf influences allocation priority. This is a little complicate to explain.
            In simple terms, when fluid amount is less than handlers count,
            then tryEmptySelf affect whether these 1mB fluidStack will stay or allocate to neighbors.

         [Problem Example]:
            For example, we have 3 nextDirs, added self, then we have 4 handlers to allocate.
            We will have trouble when we only have 3mB of fluid.

         [Self's Index Depend on Other Pipes]:
            Since self is the first handler that join the cache and its capacity is pipe's capacity,
            if larger container is absent in cache, then pipes, including self will be the largest containers in cache.
            After sorted, they will appear at the tail of cache.
            So self, as the first pipe joined in cache, its index will depend on whether other pipes would add to its front or its back.

         [How tryEmptySelf Works]:
            If tryEmptySelf is true, then other pipes will insert to self's back.
            This means self will be the first element in pipes' region.
            Which according to our allocate algorithm, it has the lowest priority, so it will try to empty self.
            Conversely, self will be the last element in pipes' region and not try to empty self.
        -------------------------------------------------------------------------------------------------------*/

        public void addToLocalAllocateCacheSorted(IFluidHandler handler, int capacity, boolean tryEmptySelf){
            boolean insertWhenEqual = !tryEmptySelf;
            int size = localAllocateCache.size();
            for(int i = 0; i <= size; i++){
                if(i == size){
                    localAllocateCache.addLast(ObjectIntImmutablePair.of(handler, capacity));
                    break;
                }
                Pair<IFluidHandler, Integer> curPair = localAllocateCache.get(i);
                if(capacity <= curPair.value()){
                    if(insertWhenEqual || capacity < curPair.value()){
                        localAllocateCache.add(i , ObjectIntImmutablePair.of(handler, capacity));
                        break;
                    }
                }
            }
        }

        public enum Status {
            SOURCE,     // The Provider of resources
            DRAIN,      // When Input Invalid, try Dump remaining resources
            COMMON,     // Only Transport resources
            INVALID     // Do nothing and will be Deleted once update
        }
    }


}
