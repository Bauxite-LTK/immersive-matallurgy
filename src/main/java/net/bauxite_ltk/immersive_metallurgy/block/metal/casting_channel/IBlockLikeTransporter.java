package net.bauxite_ltk.immersive_metallurgy.block.metal.casting_channel;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.*;

public interface IBlockLikeTransporter {

    byte getConnectionByte();

    BlockPos getBlockPos();

    List<TransportationData> getTransportationDataList();

    default TransportationData getData(BlockFace source){
        List<TransportationData> tdList = getTransportationDataList();
        for(TransportationData data: tdList) if(data.source.equals(source)) return data;
        return null;
    }

    default boolean setSelfSource(BlockFace source){
        List<TransportationData> tdList = getTransportationDataList();
        for(TransportationData data : tdList){
            if(data.source.equals(source)){
                return false;
            }
        }
        tdList.add(TransportationData.forRoot(source, TransportationData.Status.SOURCE, source.faceDir.getOpposite()));
        return true;
    }

    abstract class TransporterFluidHandler implements IFluidHandler{
        IBlockLikeTransporter tile;
        Direction facing;

        TransporterFluidHandler(IBlockLikeTransporter tile, Direction facing){
            this.tile = tile;
            this.facing = facing;
        }

        abstract IFluidHandler getTank();

        @Override
        public int fill(FluidStack resource, FluidAction doFill){
            int canAccept = resource.getAmount();
            if(canAccept <= 0){
                return 0;
            }
            if(tile.isFromDirNew(facing)){
                BlockFace sourceBlockFace = new BlockFace(tile.getBlockPos().relative(facing), facing.getOpposite());
                IMUtils.LOGGER.info("Pipe Set Root");
                if(!tile.setSelfSource(sourceBlockFace)){
                    tile.getData(sourceBlockFace).setFrom(facing);
                }
            }

            return getTank().fill(resource,doFill);
        }
    }


    // If fromDir already exists, means block at 'from' is also a IBlocklikeTransporter.
    // Because only IBlocklikeTransporter will set its next's 'from' before filling.
    // Then, if this is called and return true, means someone try to fill without set next's 'from'.
    // So it must be a new Source.
    default boolean isFromDirNew(Direction fromDir){
        List<TransportationData> tdList = getTransportationDataList();
        for(TransportationData data : tdList){
            if(data.from == fromDir){
                return false;
            }
        }
        return true;
    }


    record BlockFace(BlockPos pos, Direction faceDir){};

    class TransportationData {

        BlockFace source;
        Status status;

        Direction from;
        List<Direction> tos = new ArrayList<>();

        List<Pair<IFluidHandler, Integer>> allocateCache = new LinkedList<>();

        int depthInNetwork;


        private TransportationData(BlockFace source, Status status, Direction from, int depthInNetwork){
            this.source = source;
            this.status = status;
            this.from = from;
            this.depthInNetwork = depthInNetwork;
        }

        public static TransportationData forRoot(BlockFace sourceFace, Status status, Direction from){
            return new TransportationData(sourceFace, status, from, 0);
        }

        public static TransportationData forCommon(BlockFace sourceFace, Status status, Direction from, int depth){
            return new TransportationData(sourceFace, status, from, depth);
        }

        public void setStatus(Status status){
            this.status = status;
        }

        public void addNext(Direction nextDir){
            if(!tos.contains(nextDir)){
                tos.add(nextDir);
            }
        }

        public void setFrom(Direction fromDir){
            this.from = fromDir;
        }

        public void removeNext(Direction nextDir){
            tos.remove(nextDir);
        }

        public void clearLocalAllocateCache(){
            allocateCache.clear();
        }

        public List<Pair<IFluidHandler, Integer>> getAllocateCache(){
            return allocateCache;
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

        public void addToLocalAllocateCacheSorted(IFluidHandler handler, int capacity, boolean insertWhenEqual){
            int size = allocateCache.size();
            for(int i = 0; i <= size; i++){
                if(i == size){
                    allocateCache.addLast(ObjectIntImmutablePair.of(handler, capacity));
                    break;
                }
                Pair<IFluidHandler, Integer> curPair = allocateCache.get(i);
                if(capacity <= curPair.value()){
                    if(!insertWhenEqual || capacity < curPair.value()){
                        allocateCache.add(i , ObjectIntImmutablePair.of(handler, capacity));
                        break;
                    }
                }
            }
        }

        public enum Status {
            SOURCE,     // The Provider of resource
            DRAIN,      // When From
            COMMON,
            INVALID
        }
    }


}
