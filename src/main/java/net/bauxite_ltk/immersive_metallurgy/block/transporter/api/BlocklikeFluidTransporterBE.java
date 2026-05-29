package net.bauxite_ltk.immersive_metallurgy.block.transporter.api;

import blusunrize.immersiveengineering.api.utils.DirectionUtils;
import blusunrize.immersiveengineering.api.utils.SafeChunkUtils;
import blusunrize.immersiveengineering.common.blocks.IEBaseBlockEntity;
import blusunrize.immersiveengineering.common.util.IEBlockCapabilityCaches;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceHandler.FluidUniHandler;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceHandler.IUniHandler;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceHandler.blt.BLTSingleFluidUniHandler;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceStorage.FluidUniStorage;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceStorage.IUniStorage;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.casting_channel.PressurePipeBlockEntity;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BlocklikeFluidTransporterBE extends IEBaseBlockEntity implements BlocklikeResourceTransporter<FluidStack> {

    /**
     * FluidResourceStorage works same as FluidTank
     * {@link IUniStorage} provides unified methods for handling specific type of Resources
     * In this case, {@link FluidUniStorage} handles {@link FluidStack}.
     */
    FluidUniStorage tank;

    //Record six faces' connections
    protected byte connections = 0;

    List<TransportationData> tdList = new ArrayList<>(6);

    public Object2BooleanMap<Direction> sideConfig = new Object2BooleanOpenHashMap<>();
    {
        for(Direction d : DirectionUtils.VALUES){
            sideConfig.put(d, d.get3DDataValue() > 1);
        }
    }

    protected final Map<Direction, IEBlockCapabilityCaches.IEBlockCapabilityCache<IFluidHandler>> neighbors = IEBlockCapabilityCaches.allNeighbors(
            Capabilities.FluidHandler.BLOCK, this
    );

    protected final Map<Direction, IFluidHandler> sidedHandlers = new EnumMap<>(Direction.class);
    {
        for(Direction f : DirectionUtils.VALUES)
            sidedHandlers.put(f, new BLTSingleFluidUniHandler(tank,this, f));
    }

    private BlocklikeFluidTransporterBE(
            FluidUniStorage storage, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType,pos,blockState);
        tank = storage;
    }

    public static BlocklikeFluidTransporterBE create(
            int initialCapacity, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState){
        return new BlocklikeFluidTransporterBE(
                new FluidUniStorage(initialCapacity),
                blockEntityType,
                pos,
                blockState
        );
    }

    @Override
    public void allocateResourceLocal(BlockFace sourceKey, boolean tryEmptySelf) {
        // Get current Fluid in self
        Fluid fluid = tank.getResource().getFluid();
        if(tank.getResourceAmount() == 0) return;

        // Get total fluid amount in pipe to allocate
        // Only count fluid in pipe because we do not want to extract and reallocate fluid from consumer.
        int totalAmount = tank.getResourceAmount();
        for(Direction dir : getData(sourceKey).outputs){
            BlocklikeFluidTransporterBE fluidTransporterBE = getNeighborInstance(dir);
            if(fluidTransporterBE!=null){
                if(fluidTransporterBE.tank.getResource().getFluid().isSame(fluid)){
                    totalAmount += fluidTransporterBE.tank.getResourceAmount();
                }
            }
        }

        // Update localAllocateCache
        // Work to get the capacity limit of every local neighbor container, including self.
        // It is for the next part's allocate algorithm:
        getData(sourceKey).clearLocalAllocateCache();
        getData(sourceKey).addToLocalAllocateCacheSorted(this.tank, Math.min(this.tank.getCapacity(), totalAmount), tryEmptySelf);
        for(Direction nextDir : getData(sourceKey).outputs){
            IFluidHandler handler = neighbors.get(nextDir).getCapability();
            int capacity = 0;
            if(handler instanceof BLTSingleFluidUniHandler && handler.getFluidInTank(0).getFluid().isSame(fluid)){
                capacity = Math.min(this.tank.getCapacity(), totalAmount);
            }
            else if (handler != null && handler.getTanks() > 0){
                capacity = handler.fill(new FluidStack(fluid, totalAmount), IFluidHandler.FluidAction.SIMULATE);
            }
            else continue;
            getData(sourceKey).addToLocalAllocateCacheSorted(handler, capacity, tryEmptySelf);
        }

        // Execute Allocate Algorithm according to localAllocateCache.
        // local Allocate Cache is already sorted by capacity in ascending order
        // This Algorithm simulates Round-robin Dispatch but using minimum resource patch(1mB).
        // Due to properties of Integer division, the process is equivalent to Round-robin from cache's tail to head.
        // that means if the fluid amount cannot be divided, handlers close to cache's tail will be more likely to obtain 1mB more than others
        // It is also equivalent to [Max-Min Fairness Algorithm]
        List<Pair<IFluidHandler, Integer>> localAllocateCache = getData(sourceKey).getLocalAllocateCache();
        int handlersCount = localAllocateCache.size();
        if(handlersCount <= 1) return;

        int overflow = totalAmount;
        int bottom = 0;
        for(int i = 0; i < handlersCount; i++){
            int handlersRemain = (handlersCount - i);
            int thatCanFill = localAllocateCache.get(i).right();
            IFluidHandler handler = localAllocateCache.get(i).left();
            if((thatCanFill - bottom) * handlersRemain < overflow){
                int fill = setHandlerFluidAmountForAllocate(handler, fluid, thatCanFill);
                overflow -= (fill - bottom) * handlersRemain;
                bottom = fill;
            }
            else{
                int fill = setHandlerFluidAmountForAllocate(handler, fluid, bottom + overflow/handlersRemain);;
                overflow -= fill - bottom;
            }
        }
    }

    private int setHandlerFluidAmountForAllocate(IFluidHandler handler, Fluid fluid, int amount){
        int fill = 0;
        if(handler instanceof BLTSingleFluidUniHandler fluidUniHandler){
            fill = fluidUniHandler.setFluidAmount(fluid, amount);
        }
        else if(handler.equals(this.tank)){
            fill = this.tank.setFluidAmount(fluid, amount);
        }
        else{
            fill = handler.fill(new FluidStack(fluid, amount), IFluidHandler.FluidAction.EXECUTE);
        }
        return fill;
    }


    @Override
    public Class<FluidStack> getResourceClass() {
        return FluidStack.class;
    }

    @Override
    public IUniStorage<FluidStack> getStorage() {
        return tank;
    }

    @Override
    public List<TransportationData> getTDList() {
        return tdList;
    }

    @Override
    public byte getConnectionByte() {
        return connections;
    }

    @Override
    public IUniHandler<FluidStack> getNeighborCapability(Direction direction) {
        IFluidHandler neighborHandler = neighbors.get(direction).getCapability();
        if(neighborHandler == null) return null;
        return FluidUniHandler.cast(neighborHandler);
    }

    @Override
    public BlocklikeFluidTransporterBE getNeighborInstance(Direction direction) {
        BlockEntity be = SafeChunkUtils.getSafeBE(level,getBlockPos().relative(direction));
        if(be instanceof BlocklikeFluidTransporterBE blocklikeFluidTransporterBE)
            return blocklikeFluidTransporterBE;
        else return null;
    }



    @Override
    public boolean shouldTick() {
        return false;
    }

    @Override
    public void readCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider) {

    }

    @Override
    public void writeCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider) {

    }
}
