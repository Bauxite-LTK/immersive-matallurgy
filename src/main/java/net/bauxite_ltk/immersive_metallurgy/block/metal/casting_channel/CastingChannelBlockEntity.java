package net.bauxite_ltk.immersive_metallurgy.block.metal.casting_channel;


import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.fluid.IFluidPipe;
import blusunrize.immersiveengineering.api.utils.DirectionUtils;
import blusunrize.immersiveengineering.api.utils.SafeChunkUtils;
import blusunrize.immersiveengineering.api.utils.shapes.CachedVoxelShapes;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.metal.FluidPipeBlockEntity;
import blusunrize.immersiveengineering.common.blocks.ticking.IEServerTickableBE;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.google.common.collect.Lists;
import net.bauxite_ltk.immersive_metallurgy.block.BlockCapabilityRegistration;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlockEntities;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlocks;
import net.bauxite_ltk.immersive_metallurgy.util.Helper;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//@EventBusSubscriber(modid = TFCTrihydrate.MODID, value = Dist.CLIENT)
public class CastingChannelBlockEntity extends PressurePipeBlockEntity
        implements IEServerTickableBE, IEBlockInterfaces.IPlacementInteraction, IEBlockInterfaces.IPlayerInteraction,
        IEBlockInterfaces.ICollisionBounds, IEBlockInterfaces.ISelectionBounds, IEBlockInterfaces.IHammerInteraction

{

    public CastingChannelBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.CASTING_CHANNEL.get(), pos, state, 200);
    }

//    public Object2BooleanMap<Direction> sideConfig = new Object2BooleanOpenHashMap<>();
//    {
//        for(Direction d : DirectionUtils.VALUES){
//            sideConfig.put(d, d.get3DDataValue() > 1);
//        }
//
//    }
//
//    private final Map<Direction, IFluidHandler> sidedHandlers = new EnumMap<>(Direction.class);
//    private final Map<Direction, IEBlockCapabilityCaches.IEBlockCapabilityCache<IFluidHandler>> neighbors = IEBlockCapabilityCaches.allNeighbors(
//            Capabilities.FluidHandler.BLOCK, this
//    );

    {
        for(Direction f : DirectionUtils.VALUES)
            sidedHandlers.put(f, new CastingChannelFluidHandler(this, f));
    }


//    @Override
//    public void readCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider) {
//        int[] config = nbt.getIntArray("sideConfig");
//        for(int i = 0; i < 6; ++i)
//        {
//            Direction curDir = Direction.from3DDataValue(i);
//            if(i < config.length)
//            {
//                boolean connected = config[i]!=0;
//                sideConfig.put(curDir, connected);
//                if(connected)
//                    setValidHandler(curDir);
//                else
//                    invalidateHandler(curDir);
//            }
//            else
//            {
//                sideConfig.put(curDir, false);
//                invalidateHandler(curDir);
//            }
//        }
//        tank.readFromNBT(provider, nbt.getCompound("tank"));
//
//        byte oldConns = connections;
//        connections = nbt.getByte("connections");
//        if(level!=null&&level.isClientSide&&(connections!=oldConns))
//        {
//            BlockState state = level.getBlockState(worldPosition);
//            level.sendBlockUpdated(worldPosition, state, state, 3);
//        }
//        if(nbt.contains("isRoot")){
//            boolean isRoot = nbt.getBoolean("isRoot");
//            //TFCTrihydrate.LOGGER.info("read NBT isRoot: {}", isRoot);
//            if(isRoot){
//                setStatus(getBlockPos(),Status.ROOT);
//                int[] preDir = nbt.getIntArray("previous");
//                for(int dv : preDir){
//                    previous.addToList(getBlockPos(), Direction.from3DDataValue(dv));
//                }
//
//            }
//        }
//
//
//        invalidateCapabilities();
//        markContainingBlockForUpdate(getBlockState());
//
//
//    }
//
//    @Override
//    public void writeCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider) {
//        int[] config = new int[6];
//        for(int i = 0; i < 6; ++i)
//            if(sideConfig.getBoolean(Direction.from3DDataValue(i)))
//                config[i] = 1;
//        nbt.putIntArray("sideConfig", config);
//        nbt.put("tank", tank.writeToNBT(provider, new CompoundTag()));
//        nbt.putByte("connections", connections);
//        if(getStatus(getBlockPos()) != null){
//
//            nbt.putBoolean("isRoot", getStatus(getBlockPos()).equals(Status.ROOT));
//            //TFCTrihydrate.LOGGER.info("write NBT isRoot: {}", getStatus(getBlockPos()).equals(Status.ROOT));
//            //IDK why previous.get(getBlockPos()) could be null randomly
//            if(getStatus(getBlockPos()).equals(Status.ROOT)){
//                //TFCTrihydrate.LOGGER.info("write getBlockPos:{}",getBlockPos());
//                nbt.putIntArray("previous", previous.getList(getBlockPos()).stream().map(Direction::get3DDataValue).toList());
//            }
//        }
//    }
//
//    /**
//     * Hold On, Stay Here For A Moment
//     * IF You Don't want to Read SHIT Codes, or You Hatefully Dislike Recursive things like DFS or BFS,
//     * then Quit this part as fast as possible.
//     * Skip To {@link CastingChannelBlockEntity#fluidLight()}
//     */
//
//    public Map<BlockPos, Status> statusMap = new HashMap<>();
//    protected MapOfDirectionList previous = new MapOfDirectionList();
//
//    protected MapOfDirectionList next = new MapOfDirectionList();
//
//    protected static class MapOfDirectionList {
//        public Map<BlockPos, List<Direction>> map = new HashMap<>();
//
//        public void addRoot(@Nonnull BlockPos root){
//            map.put(root, new ArrayList<>());
//        }
//
//        public void addToList(@Nonnull BlockPos root, @Nonnull Direction nextDir){
//            map.computeIfAbsent(root, (k -> new ArrayList<>()));
//            if(!map.get(root).contains(nextDir)) map.get(root).addLast(nextDir);
//        }
//
//        public List<Direction> getList(BlockPos root){
//            return map.get(root) == null? new ArrayList<>() : map.get(root);
//        }
//
//        public void removeFromList(@Nonnull BlockPos root, @Nonnull Direction nextDir){
//            if(map.containsKey(root))
//                map.get(root).remove(nextDir);
//        }
//
//        public void removeRoot(@Nonnull BlockPos root){
//            map.remove(root);
//        }
//    }
//
//    public boolean isNotClaimedByRoot(){
//        return statusMap.isEmpty();
//    }
//
//    public boolean isClaimedByOthers(BlockPos currentRoot){
//        for(BlockPos root : statusMap.keySet()){
//            if(root == currentRoot) continue;
//            if(!statusMap.get(root).equals(Status.UNINITIALIZED)){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public Status getStatus(BlockPos root) {
//        return statusMap.get(root) == null? null : statusMap.get(root);
//    }
//
//    public void setStatus(BlockPos root ,Status s){
//        statusMap.put(root, s);
//    }
//
//    public void removeStatus(BlockPos root){
//        statusMap.remove(root);
//    }
//
//    int tickCount = 0;
//
//
//
//    int updated = 0;
//    volatile AtomicInteger updateMutex = new AtomicInteger(1);
//    @Override
//    public void tickServer() {
//        if(this.tickCount < 5){
//            tickCount++;
//            return;
//        }
//        updated = statusMap.size();
//        tickCount = 0;
//        fluidLight();
//        Set<BlockPos> immutableSet = new HashSet<>(statusMap.keySet());
//        for(BlockPos root : immutableSet){
//            Status status = getStatus(root);
//
//            if (status.equals(Status.UNINITIALIZED)) {
//                removeStatus(root);
//                previous.removeRoot(root);
//                next.removeRoot(root);
//                continue;
//            }
//            else if (status.equals(Status.COMMON)) {
//                if(getNeighborChannel(previous.getList(root).getFirst()) == null){
//                    TFCTrihydrate.LOGGER.info("Neighbor is null, remove common");
//                }
//                if(!isConnectTo(previous.getList(root).getFirst())){
//                    TFCTrihydrate.LOGGER.info("Neighbor is disconnect, remove common");
//                }
//                if (getNeighborChannel(previous.getList(root).getFirst()) == null || !isConnectTo(previous.getList(root).getFirst())){
//                    //setStatus(getBlockPos(), Status.SUB_ROOT);
//                    subrootReplaceDisconnectRootChain(root,getBlockPos());
//                }
//                //release for common
//                continue;
//            }
//            else if(status.equals(Status.SUB_ROOT)){
//                //subroot start allocate chain
//                while (updateMutex.get() <= 0) Thread.onSpinWait();
//                updateMutex.decrementAndGet();
//                tryClaimAndAllocateChain(root, true);
//                updateMutex.incrementAndGet();
//                //release for subroot
//                if(tank.isEmpty() || isClaimedByOthers(root)){
//                    for(Direction d : next.getList(root)){
//                        CastingChannelBlockEntity neighbor = getNeighborChannel(d);
//                        if(neighbor!=null) neighbor.setStatus(root, Status.SUB_ROOT);
//                    }
//                    setStatus(root, Status.UNINITIALIZED);
//                }
//            }
//            else if(status.equals(Status.ROOT)){
//                //root start allocate chain;
//                while (updateMutex.get() <= 0) Thread.onSpinWait();
//                updateMutex.decrementAndGet();
//                tryClaimAndAllocateChain(root, false);
//                updateMutex.incrementAndGet();
//                //check source is still valid, if not, change self to subroot
//                if(previous.getList(root).isEmpty()) setStatus(root,Status.SUB_ROOT);
//                List<IFluidHandler> sources = previous.getList(getBlockPos()).stream().map(d -> neighbors.get(d).getCapability()).toList();
//                if(sources.isEmpty() || tank.isEmpty()) setStatus(root,Status.SUB_ROOT);
//            }
//        }
//    }
//
//    public void tryClaimAndAllocateChain(BlockPos root, boolean tryEmptySelf){
//        //TFCTrihydrate.LOGGER.info("TryClaimAndAllocateChain");
//        //TFCTrihydrate.LOGGER.info("BlockPos:{}, Root:{}", getBlockPos(), root);
//        if(updated <= 0) return;
//        updated--;
//        for(int i = 0; i < 6; i++){
//            //check if the direction is connected
//            Direction dir = Direction.from3DDataValue(i);
//            if(((connections >> i) & 1) != 1){
//                next.removeFromList(root,dir);
//                continue;
//            }
//            if(Direction.from3DDataValue(i).equals(previous.getList(root).getFirst())) continue;
//            //check if the direction has available handler
//            IFluidHandler handler = neighbors.get(dir).getCapability();
//            if(handler!=null&&handler.getTanks() > 0)
//                next.addToList(root,dir);
//            else{
//                next.removeFromList(root,dir);
//            }
//            CastingChannelBlockEntity neighbor = getNeighborChannel(dir);
//            if(neighbor!=null){
//                if(neighbor.previous.map.isEmpty() || !neighbor.previous.map.containsKey(root)){
//                    neighbor.previous.addToList(root,dir.getOpposite());
//                    //try to claim next Channel and set status to COMMON
//                    claimChannel(root, dir);
//                }
//            }
//
//            //try to allocate fluid stack
//            // FIXME This will execute 6 times
//            //  Although this will not change the final result, but still cost performance
//            allocateFluid(root, tryEmptySelf);
//
//
//
//
//            if(neighbor!=null){
//                //multi claim
//                if(!neighbor.previous.map.containsKey(root)) {
//                    neighbor.tryClaimAndAllocateChain(root, tryEmptySelf);
//                }
//                //prevent cycle
//                if(neighbor.previous.getList(root).getFirst().equals(dir.getOpposite())) {
//                    neighbor.tryClaimAndAllocateChain(root, tryEmptySelf);
//                }
//            }
//        }
//        setChanged();
//        markContainingBlockForUpdate(null);
//    }
//
//    public void subrootReplaceDisconnectRootChain(BlockPos root, BlockPos subroot){
//        TFCTrihydrate.LOGGER.info("subrootReplaceDisconnectRootChain: Pos: {}", getBlockPos());
//        statusMap.put(subroot, statusMap.get(root).equals(Status.ROOT)? Status.COMMON : statusMap.get(root));
//        statusMap.remove(root);
//        previous.addToList(subroot,previous.getList(root).getFirst());
//        previous.removeRoot(root);
//        for(Direction d : next.getList(root)){
//            next.addToList(subroot,d);
//            CastingChannelBlockEntity nextChannel = getNeighborChannel(d);
//            if(nextChannel!=null && !nextChannel.previous.getList(root).isEmpty()){
//                if(nextChannel.previous.getList(root).getFirst().equals(d.getOpposite())){
//                    nextChannel.subrootReplaceDisconnectRootChain(root,subroot);
//                }
//            }
//        }
//        next.removeRoot(root);
//    }
//
//    public void claimChannel(BlockPos root, Direction direction){
//        CastingChannelBlockEntity next = getNeighborChannel(direction);
//        if(next != null){
//            next.setStatus(root, Status.COMMON);
//            TFCTrihydrate.LOGGER.info("set Common {}", root);
//        }
//    }
//
//
//    LinkedList<Pair<IFluidHandler, Integer>> handlersCapacities = new LinkedList<>();
//    public void allocateFluid(BlockPos root, boolean tryEmptySelf){
//        Fluid fluid = tank.getFluid().getFluid();
//        if(tank.isEmpty()) return;
//
//        int totalAmount = tank.getFluidAmount();
//        for(Direction dir : next.getList(root)){
//            CastingChannelBlockEntity channel = getNeighborChannel(dir);
//            if(channel!=null){
//                if(channel.tank.getFluid().getFluid().isSame(fluid)){
//                    totalAmount += channel.tank.getFluidAmount();
//                }
//            }
//        }
//
//
//
//        handlersCapacities.clear();
//        Pair<IFluidHandler, Integer> thisPair = ObjectIntImmutablePair.of(this.tank, this.tank.getCapacity());
//        handlersCapacities.addFirst(thisPair);
//        for(Direction dir : next.getList(root)){
//            IFluidHandler handler = neighbors.get(dir).getCapability();
//            if(handler == null) continue;
//            int capacity = 0;
//            if(handler instanceof CastingChannelFluidHandler && handler.getFluidInTank(0).getFluid().isSame(fluid)){
//                capacity = this.tank.getCapacity();
//            }
//            else{
//                capacity = handler.fill(new FluidStack(fluid, totalAmount), IFluidHandler.FluidAction.SIMULATE);
//            }
//            Pair<IFluidHandler, Integer> curPair = ObjectIntImmutablePair.of(handler, capacity);
//            int size = handlersCapacities.size();
//            for(int i = 0; i <= size; i++){
//                if(i == size){
//                    handlersCapacities.addLast(curPair);
//                    break;
//                }
//                Pair<IFluidHandler, Integer> pair = handlersCapacities.get(i);
//                if(pair.right() > curPair.right()){
//                    handlersCapacities.add(i,curPair);
//                    break;
//                }
//            }
//        }
//        int handlersCount = handlersCapacities.size();
//        //TFCTrihydrate.LOGGER.info("BlockPos: {}, Total Amount:{}", getBlockPos(), totalAmount);
//        if(handlersCount <= 1) return;
//        int overflow = totalAmount;
//        int bottom = 0;
//        for(int i = 0; i < handlersCount; i++){
//            int handlersRemain = (handlersCount - i);
//            int thatCanFill = handlersCapacities.get(i).right();
//            IFluidHandler handler = handlersCapacities.get(i).left();
//            if((thatCanFill - bottom) * handlersRemain < overflow){
//                int fill = 0;
//                if(handler instanceof CastingChannelFluidHandler channel){
//                    fill = channel.castingChannel.setFluidAmount(fluid, thatCanFill);
//                }
//                else if(handler.equals(this.tank)){
//                    fill = this.setFluidAmount(fluid, thatCanFill);
//                }
//                else{
//                    fill = handler.fill(new FluidStack(fluid, thatCanFill), IFluidHandler.FluidAction.EXECUTE);
//                }
//
//                //TFCTrihydrate.LOGGER.info("Not Enough: thatCanFill:{}, fill:{}, BlockPos:{}",thatCanFill, fill, getBlockPos());
//                overflow -= (fill - bottom) * handlersRemain;
//                bottom = fill;
//            }
//            else{
//                int fill = 0;
//                if(handler instanceof CastingChannelFluidHandler channel){
//                    fill = channel.castingChannel.setFluidAmount(fluid, bottom + overflow/handlersRemain);
//                }
//                else if(handler.equals(this.tank)){
//                    fill = this.setFluidAmount(fluid, bottom + overflow/handlersRemain);
//                }
//                else{
//                    fill = handler.fill(new FluidStack(fluid, bottom + overflow/handlersRemain), IFluidHandler.FluidAction.EXECUTE);
//                }
//                overflow -= fill - bottom;
//            }
//        }
//
//        if(overflow != 0){
//            TFCTrihydrate.LOGGER.warn("Allocate Fluid: Unexpected Allocate, The Fluid May Lose");
//            TFCTrihydrate.LOGGER.warn("TotalAmount: {}", totalAmount);
//            TFCTrihydrate.LOGGER.warn("Overflow: {}", overflow);
//        }
//
//
//        setChanged();
//        markContainingBlockForUpdate(null);
//    }
//
//    public int setFluidAmount(Fluid fluid, int amount){
//        int thisAmount = tank.getFluidAmount();
//        if(amount>tank.getCapacity()) TFCTrihydrate.LOGGER.warn("Set Fluid Amount: Larger Than Capacity!");
//        if(amount == thisAmount) return tank.getFluidAmount();;
//        if(!tank.isEmpty() && !fluid.isSame(tank.getFluid().getFluid())) return 0;
//        if(amount > thisAmount){
//            int fillResult = tank.fill(new FluidStack(fluid, amount-thisAmount), IFluidHandler.FluidAction.EXECUTE);
//            if(fillResult != amount - thisAmount) TFCTrihydrate.LOGGER.error("Set Fluid Amount: Unexpected Fill Amount!");
//            return tank.getFluidAmount();
//        }
//        else {
//            int drainResult = tank.drain(thisAmount - amount, IFluidHandler.FluidAction.EXECUTE).getAmount();
//            if(drainResult != thisAmount - amount) TFCTrihydrate.LOGGER.error("Set Fluid Amount: Unexpected Drain Amount!");
//            return tank.getFluidAmount();
//        }
//    }
//
//
//    AtomicInteger forceAllocateMutex = new AtomicInteger(1);
//    LinkedList<Pair<IFluidHandler, Integer>> handlersForceFillSimulationCache = new LinkedList<>();
//    public int forceToAllocate(BlockPos root, Fluid fluid, int amount, boolean simulate){
//
//        int allocateAmount = amount;
//        Fluid allocateFluid = fluid;
//        boolean drainSelf = false;
//        if(!tank.isEmpty() && !this.tank.getFluid().getFluid().isSame(allocateFluid)){
//
//            //if(getStatus(root).equals(Status.ROOT) && this.tank.getFluidAmount() < )
//
//            drainSelf = true;
//            allocateAmount = this.tank.getFluidAmount();
//            allocateFluid = this.tank.getFluid().getFluid();
//        }
//        int totalCanFill = 0;
//        int handlersCount = 0;
//        if(simulate){
//            handlersForceFillSimulationCache.clear();
//            int thisCanFill = tank.fill(new FluidStack(allocateFluid, allocateAmount), IFluidHandler.FluidAction.SIMULATE);
//
//            if(drainSelf){
//                thisCanFill = 0;
//                TFCTrihydrate.LOGGER.info("execute drain self simulate");
//            }
//
//            totalCanFill = thisCanFill;
//            int neighborsShouldFill = allocateAmount - thisCanFill;
//            if(thisCanFill >= allocateAmount){
//                return allocateAmount;
//            }
//            for(Direction direction : next.getList(root)){
//                IFluidHandler handler = neighbors.get(direction).getCapability();
//                if(handler!=null && handler.getTanks() > 0){
//                    handlersCount++;
//                    int handlerMaxFill = checkAndForceToFill(handler, root, allocateFluid, neighborsShouldFill, true);
//                    totalCanFill += handlerMaxFill;
//                    Pair<IFluidHandler, Integer> curPair = ObjectIntImmutablePair.of(handler, handlerMaxFill);
//                    int size = handlersForceFillSimulationCache.size();
//                    for(int i = 0; i <= size; i++){
//                        if(i == size){
//                            handlersForceFillSimulationCache.addLast(curPair);
//                            break;
//                        }
//                        Pair<IFluidHandler, Integer> pair = handlersForceFillSimulationCache.get(i);
//                        if(pair.right() > curPair.right()){
//                            handlersForceFillSimulationCache.add(i,curPair);
//                            break;
//                        }
//                    }
//                }
//                //if(totalCanFill >= allocateAmount) break;
//            }
//            if(drainSelf){
//                //execute drain self immediately.
//                //The fluid is the origin input fluid, not the one in this tank.
//                //so that the boolean value drainSelf will work
//                forceToAllocate(root, fluid, allocateAmount, false);
//
//                return 0;
//            }
//
//            return Math.min(totalCanFill,allocateAmount);
//        }
//        if(handlersForceFillSimulationCache.isEmpty()){
//            forceToAllocate(root, allocateFluid, allocateAmount, true);
//        }
//        handlersCount = handlersForceFillSimulationCache.size();
//        //TFCTrihydrate.LOGGER.info("Allocate Amount:{}, BlockPos:{}", allocateAmount, getBlockPos());
//        //TFCTrihydrate.LOGGER.info("Handler Count:{}", handlersCount);
//
//        //execute fluid allocate
//        int thisFill;
//        if(!drainSelf){
//            thisFill = tank.fill(new FluidStack(allocateFluid, allocateAmount), IFluidHandler.FluidAction.EXECUTE);
//        }
//        else{
//            thisFill = 0;
//            TFCTrihydrate.LOGGER.info("execute drain self");
//        }
//
//        //TFCTrihydrate.LOGGER.info("thisFill:{}, BlockPos:{}", thisFill, getBlockPos());
//        int neighborsShouldFill = allocateAmount - thisFill;
//        int overflow = neighborsShouldFill;
//        //TFCTrihydrate.LOGGER.info("neighborsShouldFill:{}", overflow);
//        int bottom = 0;
//        for(int i = 0; i < handlersCount; i++){
//            int handlersRemain = (handlersCount - i);
//            int thatCanFill = handlersForceFillSimulationCache.get(i).right();
//            IFluidHandler handler = handlersForceFillSimulationCache.get(i).left();
//            if((thatCanFill - bottom) * handlersRemain < overflow){
//                int fill = checkAndForceToFill(handler,root, allocateFluid, thatCanFill, false);
//                //TFCTrihydrate.LOGGER.info("Not Enough: thatCanFill:{}, fill:{}, BlockPos:{}",thatCanFill, fill, getBlockPos());
//                overflow -= (fill - bottom) * handlersRemain;
//                bottom = fill;
//            }
//            else{
//                int fill = checkAndForceToFill(handler, root,  allocateFluid, bottom + overflow/handlersRemain, false);
//                //TFCTrihydrate.LOGGER.info("Enough: ShouldFill:{}, fill:{}, BlockPos:{}", bottom + overflow/handlersRemain, fill, getBlockPos());
//                overflow -= fill - bottom;
//
//            }
//        }
//        //TFCTrihydrate.LOGGER.info("totalFill:{}, BlockPos:{}" ,neighborsShouldFill - overflow + thisFill, getBlockPos());
//
//        handlersForceFillSimulationCache.clear();
//        if(drainSelf){
//            this.tank.drain(neighborsShouldFill - overflow + thisFill, IFluidHandler.FluidAction.EXECUTE);
//            setChanged();
//            markContainingBlockForUpdate(null);
//            return 0;
//        }
//        setChanged();
//        markContainingBlockForUpdate(null);
//
//        return neighborsShouldFill - overflow + thisFill;
//    }
//
//
//    private int checkAndForceToFill(IFluidHandler handler, BlockPos root, Fluid fluid, int amount, boolean simulate){
//        if(handler instanceof CastingChannelFluidHandler channel) {
//            BlockPos thatPos = channel.castingChannel.getBlockPos();
//            if(!channel.castingChannel.previous.map.containsKey(root)){
//                //TFCTrihydrate.LOGGER.info("Not Claimed By Root {}", root);
//                return 0;
//            }
//            if (thatPos.relative(channel.castingChannel.previous.getList(root).getFirst()).equals(getBlockPos())) {
//                return channel.castingChannel.forceToAllocate(root, fluid, amount, simulate);
//            }
//            else {
//                //TFCTrihydrate.LOGGER.info("Previous Direction: {}, Not Equal this Pos: {}", channel.castingChannel.previous.get(root), getBlockPos());
//                return 0;
//            }
//        }
//        return handler.fill(new FluidStack(fluid, amount), simulate? IFluidHandler.FluidAction.SIMULATE: IFluidHandler.FluidAction.EXECUTE);
//
//    }


    @Override
    public void tickServer() {
        super.tickServer();
        fluidLight();
    }



    private void fluidLight(){
        int lightLevel = 0;
        if(tank.getFluidInTank(0).getFluid().getFluidType().getLightLevel() > 0){
            lightLevel = 15;
        }
        level.setBlockAndUpdate(getBlockPos(),getState().setValue(CastingChannelBlock.LIGHT_LEVEL, lightLevel));

    }


    private void invalidateHandler(Direction side)
    {
        IFluidHandler handler = sidedHandlers.get(side);
        if(handler!=null)
        {
            sidedHandlers.put(side, null);
            invalidateCapabilities();
        }
    }

    private void setValidHandler(Direction side)
    {
        IFluidHandler handler = sidedHandlers.get(side);
        if(handler==null)
        {
            sidedHandlers.put(side, new CastingChannelFluidHandler(this, side));
            invalidateCapabilities();
        }
    }

    public void toggleSide(Direction side)
    {
        boolean newSideConnected = !sideConfig.getBoolean(side);
        setSide(side, newSideConnected);
    }

    public void setSide(Direction side, boolean connectable)
    {
        setSide(side, connectable, true);
    }

    public void setSide(Direction side, boolean connectable, boolean firstPipe)
    {
        if((connections & 3) != 0)
            this.horizontalNeighborOfVerticalConnection = getUniqueHorizontalDirection();
        sideConfig.put(side, connectable);
        if(connectable)
            setValidHandler(side);
        else
            invalidateHandler(side);
        setChanged();
        if(firstPipe)
        {
            BlockEntity neighborTile = level.getBlockEntity(getBlockPos().relative(side));
            if(neighborTile instanceof CastingChannelBlockEntity)
                ((CastingChannelBlockEntity)neighborTile).setSide(side.getOpposite(), connectable, false);
            updateConnectionByte(side); //yes, this is not meant for neighborTile
        }
        level.blockEvent(getBlockPos(), getBlockState().getBlock(), 0, 0);
    }


    public boolean updateConnectionByte(Direction dir)
    {
        if(level==null||level.isClientSide||!SafeChunkUtils.isChunkSafe(level, worldPosition.relative(dir)))
            return false;
        final byte oldConn = connections;
        int i = dir.get3DDataValue();
        int mask = 1<<i;
        connections &= ~mask;

        if(sideConfig.getBoolean(dir))
        {
            IFluidHandler handler = neighbors.get(dir).getCapability();
            if(handler!=null&&handler.getTanks() > 0)
                connections |= mask;
        }
        return oldConn!=connections;
    }


    @Override
    public boolean triggerEvent(int id, int arg)
    {
        if(id==0)
        {
            this.markContainingBlockForUpdate(null);
            return true;
        }
        return false;
    }



    @Override
    public void onNeighborBlockChange(BlockPos otherPos)
    {
        super.onNeighborBlockChange(otherPos);
        Direction dir = Direction.getNearest(otherPos.getX()-worldPosition.getX(),
                otherPos.getY()-worldPosition.getY(), otherPos.getZ()-worldPosition.getZ());
        IMUtils.LOGGER.info("{} onNeighborBlockChange, dir: {}", getBlockPos(), dir);
        if(updateConnectionByte(dir))
        {
            if(checkDownAndSide()) return;
            if(checkUpAndSide()) return;
            Level world = getLevelNonnull();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    //When a Vertical Connection is destroyed because of horizontal neighbor's toggle,
    //this variable will record the horizontal neighbor's direction
    private Direction horizontalNeighborOfVerticalConnection = null;
    public boolean checkDownAndSide(){
        if((connections & 1) == 1){
            Direction horizon = null;
            for (int i = 2; i < 6; i++){
                if(sideConfig.getBoolean(Direction.from3DDataValue(i)))
                    horizon = Direction.from3DDataValue(i);
            }
            if(getNeighborChannel(horizon)==null){
                IMUtils.LOGGER.info("checkDownAndSide {}", getBlockPos());
                CastingChannelBlockEntity down = getNeighborChannel(Direction.DOWN);
                for(Direction d : DirectionUtils.VALUES){
                    sideConfig.put(d,d.get3DDataValue() > 1 && !d.equals(horizontalNeighborOfVerticalConnection));
                    down.sideConfig.put(d,d.get3DDataValue() > 1);
                    updateConnectionByte(d);
                    down.updateConnectionByte(d);
                }
                invalidateCapabilities();
                down.invalidateCapabilities();
                markContainingBlockForUpdate(null);
                down.markContainingBlockForUpdate(null);
                return true;
            }
            else if(getNeighborChannel(Direction.DOWN) == null){
                for(Direction d : DirectionUtils.VALUES){
                    sideConfig.put(d,d.get3DDataValue() > 1 && !d.equals(horizontalNeighborOfVerticalConnection));

                }
                invalidateCapabilities();
                markContainingBlockForUpdate(getBlockState());
                horizontalNeighborOfVerticalConnection = null;
                return true;
            }
        }
        return false;
    }

    public boolean checkUpAndSide(){
        if(((connections >> 1) & 1) == 1){
            Direction horizon = null;
            for (int i = 2; i < 6; i++){
                if(sideConfig.getBoolean(Direction.from3DDataValue(i)))
                    horizon = Direction.from3DDataValue(i);
            }
            if(getNeighborChannel(horizon)==null){
                CastingChannelBlockEntity up = getNeighborChannel(Direction.UP);
                for(Direction d : DirectionUtils.VALUES){
                    sideConfig.put(d,d.get3DDataValue() > 1 && !d.equals(horizontalNeighborOfVerticalConnection));
                    up.sideConfig.put(d,d.get3DDataValue() > 1);
                    updateConnectionByte(d);
                    up.updateConnectionByte(d);
                }
                invalidateCapabilities();
                up.invalidateCapabilities();
                markContainingBlockForUpdate(getBlockState());
                up.markContainingBlockForUpdate(up.getBlockState());
                return true;
            }
            else if(getNeighborChannel(Direction.UP) == null){
                for(Direction d : DirectionUtils.VALUES){
                    sideConfig.put(d,d.get3DDataValue() > 1 && !d.equals(horizontalNeighborOfVerticalConnection));
                }
                invalidateCapabilities();
                markContainingBlockForUpdate(getBlockState());
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBEPlaced(BlockPlaceContext ctx) {
        boolean doUpdate = false;
        for(Direction d : DirectionUtils.VALUES){
            if(updateConnectionByte(d)){
                doUpdate = true;
            }
        }
        if(doUpdate){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }


    }

    public CastingChannelBlockEntity getNeighborChannel(Direction direction){
        if(direction==null) return null;
        BlockEntity con = SafeChunkUtils.getSafeBE(level, getBlockPos().relative(direction));
        if (con instanceof CastingChannelBlockEntity up) {
            return up;
        }
        return null;
    }

    public Direction getUniqueHorizontalDirection(){
        int horizon = (connections >> 2) & 15;
        if(horizon > 0 && (horizon & (horizon - 1))==0){
            for(int i = 0; i < 4; i++){
                if((horizon >> i & 1) == 1)
                    return Direction.from3DDataValue(i+2);
            }
        }
        return null;
    }

    @Override
    public ItemInteractionResult interact(Direction side, Player player, InteractionHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ) {
        if(!heldItem.is(IMBlocks.CASTING_CHANNEL.asItem())) {
            if (heldItem.is(Items.STICK)) {
//                TFCTrihydrate.LOGGER.info("----Channel Info----");
//                TFCTrihydrate.LOGGER.info("Pos:{}", getBlockPos());
//                TFCTrihydrate.LOGGER.info("fluidAmount:{}", tank.getFluidAmount());
//                for (BlockPos root : statusMap.keySet()) {
//                    TFCTrihydrate.LOGGER.info("Root:{}", root);
//                    TFCTrihydrate.LOGGER.info("Previous:{}", previous.getList(root).getFirst());
//                    for (Direction nextDir : next.getList(root)) {
//                        TFCTrihydrate.LOGGER.info("Next:{}", nextDir);
//                    }
//                }
//                TFCTrihydrate.LOGGER.info("----=========----");
                return ItemInteractionResult.sidedSuccess(getLevelNonnull().isClientSide);
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        CastingChannelBlockEntity up = getNeighborChannel(Direction.UP);
        if(up!=null && (connections & 3) == 0 && (up.connections & 3) == 0){
            Direction upFlowFrom = up.getUniqueHorizontalDirection();
            Direction downFlowTo = getUniqueHorizontalDirection();
            if(upFlowFrom!=null && downFlowTo!=null){
                if(upFlowFrom.getOpposite().equals(downFlowTo)){
                    for(Direction d : DirectionUtils.VALUES){
                        sideConfig.put(d, d.equals(Direction.UP) || d.equals(downFlowTo));
                        up.sideConfig.put(d, d.equals(Direction.DOWN) || d.equals(upFlowFrom));
                        updateConnectionByte(d);
                        up.updateConnectionByte(d);
                    }
                    invalidateCapabilities();
                    up.invalidateCapabilities();
                    markContainingBlockForUpdate(null);
                    up.markContainingBlockForUpdate(null);
                    Helper.playSound(level, worldPosition, SoundEvents.AMETHYST_BLOCK_PLACE);
                    return ItemInteractionResult.sidedSuccess(getLevelNonnull().isClientSide);
                }
            }
        }

        Helper.playSound(level, worldPosition, SoundEvents.ITEM_BREAK);


        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }


    @Override
    public boolean hammerUseSide(Direction side, Player player, InteractionHand hand, Vec3 hitVec)
    {
        if(level.isClientSide)
            return true;
        hitVec = hitVec.subtract(Vec3.atLowerCornerOf(worldPosition));
        Direction fd = side;
        List<AABB> boxes = getBoxes(new BoundingBoxKey(true, this));
        for(AABB box : boxes) {
            if (box.inflate(.002).contains(hitVec)) {
                for (Direction d : DirectionUtils.VALUES) {
                    Vec3 testVec = new Vec3(0.5 + 0.5 * d.getStepX(), 4d / 16 + 0.5 * d.getStepY(), 0.5 + 0.5 * d.getStepZ());
                    if (box.inflate(0.002).contains(testVec)) {
                        fd = d;
                        break;
                    }
                }
                break;
            }
        }
        if((connections & 3) == 0 && fd.get3DDataValue() > 1)
        {
            toggleSide(fd);
            this.markContainingBlockForUpdate(null);
            Helper.playSound(level, worldPosition, SoundEvents.NETHERITE_BLOCK_PLACE);
            return true;
        }
        Helper.playSound(level, worldPosition, SoundEvents.ITEM_BREAK);
        return false;
    }

    public boolean isConnectTo(Direction direction){
        return ((connections >> direction.get3DDataValue()) & 1) == 1;
    }

    public byte getAvailableConnectionByte()
    {
        byte availableConnections = connections;
        int mask = 1;
        for(Direction dir : DirectionUtils.VALUES)
        {
            if((availableConnections&mask)==0)
            {
                if(level.getBlockEntity(getBlockPos().relative(dir)) instanceof FluidPipeBlockEntity)
                    availableConnections |= mask;
                else
                {
                    IFluidHandler handler = neighbors.get(dir).getCapability();
                    if(handler!=null&&handler.getTanks() > 0)
                        availableConnections |= mask;
                }
            }
            mask <<= 1;
        }
        return availableConnections;
    }


    private static final CachedVoxelShapes<BoundingBoxKey> SHAPES = new CachedVoxelShapes<>(CastingChannelBlockEntity::getBoxes);

    @Override
    public VoxelShape getCollisionShape(CollisionContext ctx)
    {
        return SHAPES.get(new BoundingBoxKey(false, this));
    }

    @Override
    public VoxelShape getSelectionShape(@Nullable CollisionContext ctx)
    {
        //TODO needs to be a more generic check!
        boolean hammer = ctx!=null&&ctx.isHoldingItem(IEItems.Tools.HAMMER.get());
        if(ctx instanceof EntityCollisionContext ecc){
            if(ecc.getEntity() instanceof Player player){
                hammer = player.getItemInHand(InteractionHand.MAIN_HAND).getTags()
                        .anyMatch(tagKey -> tagKey.equals(IETags.hammers));
            }
        }
        return SHAPES.get(new BoundingBoxKey(hammer, this));
    }


    private static List<AABB> getBoxes(BoundingBoxKey key)
    {
        List<AABB> list = Lists.newArrayList();
        byte availableConnections = key.availableConnections;
        byte activeConnections = key.connections;
        for(Direction d : DirectionUtils.VALUES)
        {
            int i = d.get3DDataValue();
            if(((availableConnections >> i) & 1)==1)
            {
                if(((activeConnections >> i) & 1)==1||key.showToolView)
                {
                    switch (d){
                        case Direction.UP:{
                            list.add(new AABB(3d/16, 0, 0, 13d/16,1,1));
                            break;
                        }
                        case Direction.NORTH:{
                            list.add(new AABB(3d/16, 0, 0, 13d/16, 8d/16,3d/16));
                            break;
                        }
                        case Direction.SOUTH:{
                            list.add(new AABB(3d/16, 0, 13d/16, 13d/16, 8d/16,1));
                            break;
                        }
                        case Direction.EAST:{
                            list.add(new AABB(13d/16, 0, 3d/16, 1, 8d/16,13d/16));
                            break;
                        }
                        case Direction.WEST:{
                            list.add(new AABB(0, 0, 3d/16, 3d/16, 8d/16,13d/16));
                            break;
                        }
                    }
//                    if(key.connectionStyles.get(d)== ConnectionStyle.TO_UP_NORTH)
//                        list.add(new AABB(
//                                i==4?0: i==5?0.875: 0.125, i==0?0: i==1?0.875: 0.125, i==2?0: i==3?0.875: 0.125,
//                                i==4?0.125: i==5?1: 0.875, i==0?0.125: i==1?1: 0.875, i==2?0.125: i==3?1: 0.875
//                        ));
                }
            }
        }
        list.add(new AABB(3d/16, 0, 3d/16, 13d/16, 8d/16, 13d/16));
        return list;
    }


    private static class BoundingBoxKey
    {
        private final boolean showToolView;
        private final byte connections;
        private final byte availableConnections;
        private final Map<Direction, ConnectionStyle> connectionStyles = new EnumMap<>(Direction.class);

        private BoundingBoxKey(boolean showToolView, CastingChannelBlockEntity te)
        {
            this.showToolView = showToolView;
            this.connections = te.connections;
            this.availableConnections = te.getAvailableConnectionByte();
            for(Direction d : DirectionUtils.VALUES)
                connectionStyles.put(d, te.getConnectionStyle(d));
        }

        @Override
        public boolean equals(Object o)
        {
            if(this==o) return true;
            if(o==null||getClass()!=o.getClass()) return false;
            BoundingBoxKey that = (BoundingBoxKey)o;
            return showToolView==that.showToolView&&
                    connections==that.connections&&
                    availableConnections==that.availableConnections&&
                    connectionStyles.equals(that.connectionStyles);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(showToolView, connections, availableConnections, connectionStyles);
        }

    }



    static class CastingChannelFluidHandler extends PressurePipeFluidHandler{

        public CastingChannelFluidHandler(PressurePipeBlockEntity castingChannel, Direction facing) {
            super(castingChannel, facing);
        }
    }

//    static class CastingChannelFluidHandler implements IFluidHandler
//    {
//        private static final Random CURRENT_TICK_RANDOM = new Random();
//
//        CastingChannelBlockEntity castingChannel;
//        Direction facing;
//
//        public CastingChannelFluidHandler(CastingChannelBlockEntity castingChannel, Direction facing)
//        {
//            this.castingChannel = castingChannel;
//            this.facing = facing;
//        }
//
//        @Override
//        public int getTanks()
//        {
//            return 1;
//        }
//
//        @Nonnull
//        @Override
//        public FluidStack getFluidInTank(int tank)
//        {
//            return castingChannel.tank.getFluidInTank(0);
//        }
//
//        @Override
//        public int getTankCapacity(int tank)
//        {
//            return castingChannel.tank.getCapacity();
//        }
//
//        @Override
//        public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
//        {
//            return castingChannel.tank.isFluidValid(tank, stack);
//        }
//
//        @Override
//        public int fill(FluidStack resource, FluidAction doFill)
//        {
//            int canAccept = resource.getAmount();
//            if(canAccept <= 0){
//                return 0;
//            }
//
//            if(castingChannel.previous.map.values().stream().noneMatch(list -> list.contains(facing))){
//                TFCTrihydrate.LOGGER.info("set root");
//                castingChannel.setStatus(castingChannel.getBlockPos(),Status.ROOT);
//                castingChannel.previous.addToList(castingChannel.getBlockPos(), facing);
//            }
//
//
//            int result = castingChannel.tank.fill(resource,doFill);
//            if(result == 0 && castingChannel.tickCount%5!=0){
//                if(castingChannel.forceAllocateMutex.get() <= 0) Thread.onSpinWait();
//                castingChannel.forceAllocateMutex.decrementAndGet();
//                result = castingChannel.forceToAllocate(castingChannel.getBlockPos(), resource.getFluid(),resource.getAmount(), doFill.simulate());
//                castingChannel.forceAllocateMutex.incrementAndGet();
//            }
//
//            castingChannel.setChanged();
//            castingChannel.markContainingBlockForUpdate(null);
//
//
//
//            return result;
//        }
//
//        @Nonnull
//        @Override
//        public FluidStack drain(FluidStack resource, FluidAction doDrain)
//        {
//            return this.drain(resource.getAmount(), doDrain);
//        }
//
//        @Nonnull
//        @Override
//        public FluidStack drain(int maxDrain, FluidAction doDrain)
//        {
//            FluidStack fluidStack = castingChannel.tank.drain(maxDrain, doDrain);
//            castingChannel.setChanged();
//            castingChannel.markContainingBlockForUpdate(null);
//            return fluidStack;
//        }
//    }


    public record DirectionalFluidOutput(
            IFluidHandler output,
            Direction direction,
            @Nullable BlockEntity containingTile,
            BlockPos pos
    )
    {
        boolean stripPressure()
        {
            if(containingTile instanceof IFluidPipe pipe)
                return pipe.stripPressureTag();
            return true;
        }
    }
    
    




    public ConnectionStyle getConnectionStyle(Direction connection)
    {
        if((connections&(1<<connection.get3DDataValue()))==0)
            return ConnectionStyle.NO_CONNECTION;

        // Here goes to the Up Connection cases:
        // Casting Channel can only go straight down.
        // So if current channel has a connection on its top,
        // the channel on its top must have only one connection in its horizontal direction,
        // and current channel's horizontal direction must be opposite to the channel on top
        if(connection.equals(Direction.UP)){
            BlockEntity con = SafeChunkUtils.getSafeBE(level, getBlockPos().relative(connection));
            if(con instanceof CastingChannelBlockEntity castingChannel){
                int upChannelConnections = castingChannel.connections|(1<<connection.getOpposite().get3DDataValue());
                if((upChannelConnections >> 2 & 1) != 0){
                    // up connect to north, then flow goes south
                    return ConnectionStyle.TO_UP_SOUTH;
                }
                else if((upChannelConnections >> 3 & 1)!=0){
                    // up connect to south, then flow goes north
                    return ConnectionStyle.TO_UP_NORTH;
                }
                else if((upChannelConnections >> 4 & 1)!=0){
                    // up connect to west, then flow goes east
                    return ConnectionStyle.TO_UP_EAST;
                }
                else if((upChannelConnections >> 5 & 1)!=0){
                    // up connect to east, then flow goes west
                    return ConnectionStyle.TO_UP_WEST;
                }
            }
        }
        return ConnectionStyle.COMMON;
    }

    public enum Status{
        UNINITIALIZED,
        COMMON,
        SUB_ROOT,
        ROOT
    }

    public enum ConnectionStyle
    {
        NO_CONNECTION,
        COMMON,
        TO_UP_SOUTH,
        TO_UP_NORTH,
        TO_UP_EAST,
        TO_UP_WEST,
    }


    public static void registerCapabilities(BlockCapabilityRegistration.BECapabilityRegistrar<CastingChannelBlockEntity> registrar)
    {
        registrar.register(Capabilities.FluidHandler.BLOCK, (be, side) -> {
            if(side!=null&&be.sideConfig.getBoolean(side))
                return be.sidedHandlers.get(side);
            else
                return null;
        });
    }

    public static boolean isHorizontal(Direction direction){
        return direction.get3DDataValue()<2;
    }
}
