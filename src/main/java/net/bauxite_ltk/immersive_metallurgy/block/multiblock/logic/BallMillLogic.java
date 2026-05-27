package net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic;


import blusunrize.immersiveengineering.api.energy.AveragingEnergyStorage;
import blusunrize.immersiveengineering.api.fluid.FluidUtils;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.ComparatorManager;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.RedstoneControl;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.*;
import blusunrize.immersiveengineering.api.tool.MachineInterfaceHandler;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcess;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import blusunrize.immersiveengineering.common.fluids.ArrayFluidHandler;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.SlotwiseItemHandler;
import blusunrize.immersiveengineering.common.util.inventory.WrappingItemHandler;
import blusunrize.immersiveengineering.common.util.sound.MultiblockSound;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.process.IMMultiblockProcessInMachine;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.shapes.BallMillShapes;
import net.bauxite_ltk.immersive_metallurgy.crafting.BallMillRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BallMillLogic implements
        IMultiblockLogic<BallMillLogic.State>,
        IServerTickableComponent<BallMillLogic.State>,
        IClientTickableComponent<BallMillLogic.State> {
    public static final BlockPos MASTER_OFFSET = new BlockPos(3, 1, 1);
    public static final BlockPos REDSTONE_POS = new BlockPos(3, 1, 2);
    private static final MultiblockFace OUTPUT_OFFSET = new MultiblockFace(7, 1, 1, RelativeBlockFace.RIGHT);
    private static final MultiblockFace OUTPUT_FLUID_OFFSET = new MultiblockFace(7, 0, 1, RelativeBlockFace.RIGHT);
    private static final MultiblockFace INPUT_OFFSET = new MultiblockFace(-1, 1, 1, RelativeBlockFace.LEFT);
    private static final MultiblockFace INPUT_FLUID_OFFSET = new MultiblockFace(-1, 0, 1, RelativeBlockFace.LEFT);
    private static final CapabilityPosition OUTPUT_CAP = CapabilityPosition.opposing(OUTPUT_OFFSET);
    private static final CapabilityPosition OUTPUT_FLUID_CAP = CapabilityPosition.opposing(OUTPUT_FLUID_OFFSET);
    private static final CapabilityPosition INPUT_CAP = CapabilityPosition.opposing(INPUT_OFFSET);
    private static final CapabilityPosition INPUT_FLUID_CAP = CapabilityPosition.opposing(INPUT_FLUID_OFFSET);
    private static final CapabilityPosition ENERGY_INPUT = new CapabilityPosition(0, 2, 1, RelativeBlockFace.UP);

    public static final int NUM_SLOTS = 9;
    public static final int NUM_INPUT_SLOTS = 8;
    public static final int OUTPUT_SLOT = NUM_INPUT_SLOTS;
    public static final int TANK_CAPACITY = 24 * FluidType.BUCKET_VOLUME;
    public static final int ENERGY_CAPACITY = 96000;



    public State createInitialState(IInitialMultiblockContext<State> capabilitySource) {
        return new State(capabilitySource);
    }

    @Override
    public void tickServer(IMultiblockContext<State> context) {
        final State state = context.getState();
        final boolean active = state.processor.tickServer(state, context.getLevel(), state.rsState.isEnabled(context));
        if(active!=state.active)
        {
            state.active = active;
            context.requestMasterBESync();
        }
        enqueueProcesses(state, context.getLevel().getRawLevel());
        if(context.getLevel().shouldTickModulo(8))
            handleItemOutput(context);
        FluidUtils.multiblockFluidOutput(
                state.fluidOutput.get(), state.tanks.output,
                -1, -1,null
        );
    }

    private void enqueueProcesses(State state, Level level) {

        final FluidStack inputFluid = state.tanks.input.getFluid();


        if(state.energy.getEnergyStored() <= 0||state.processor.getQueueSize() >= state.processor.getMaxQueueSize())
            return;
        final int[] usedInvSlots = new int[NUM_INPUT_SLOTS];
        for(MultiblockProcess<?, ?> process : state.processor.getQueue())
            if(process instanceof IMMultiblockProcessInMachine)
                for(int i : ((IMMultiblockProcessInMachine<?>)process).getInputSlots())
                    usedInvSlots[i]++;

        Integer[] preferredSlots = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7};
        Arrays.sort(preferredSlots, 0, NUM_INPUT_SLOTS, Comparator.comparingInt(arg0 -> usedInvSlots[arg0]));
        for(int slot : preferredSlots)
        {
            ItemStack stack = state.inventory.getStackInSlot(slot);
            if(stack.getCount() <= usedInvSlots[slot])
                continue;
            stack = stack.copy();
            stack.shrink(usedInvSlots[slot]);
            RecipeHolder<BallMillRecipe> recipe = BallMillRecipe.findRecipe(level, stack, inputFluid);
            if(recipe!=null){
                IMMultiblockProcessInMachine<BallMillRecipe> process = new IMMultiblockProcessInMachine<>(recipe, slot);
                if(!inputFluid.isEmpty()){
                    process.setInputTanks(0);
                }
                state.processor.addProcessToQueue(process, level, false);
            }
        }
    }

    private void handleItemOutput(IMultiblockContext<State> ctx)
    {
        final State state = ctx.getState();
        final ItemStack fullOutputStack = state.inventory.getStackInSlot(OUTPUT_SLOT);
        if(fullOutputStack.isEmpty())
            return;
        int outputCount = Math.min(fullOutputStack.getCount(), 32);
        ItemStack stack = fullOutputStack.copyWithCount(outputCount);
        final ItemStack remaining = Utils.insertStackIntoInventory(state.itemOutput, stack, false);
        if(remaining.isEmpty())
        {
            fullOutputStack.shrink(outputCount);
            ctx.markMasterDirty();
        }
        else{
            fullOutputStack.shrink(outputCount - remaining.getCount());
            ctx.markMasterDirty();
        }
    }

    @Override
    public void tickClient(IMultiblockContext<State> context) {
        final State state = context.getState();
        if(state.active)
            state.barrelAngle = (state.barrelAngle+9)%360;
        if(!state.isPlayingSound.getAsBoolean())
        {
            final Vec3 soundPos = context.getLevel().toAbsolute(new Vec3(3.5, 1.5, 1.5));
            state.isPlayingSound = MultiblockSound.startSound(
                    () -> state.active, context.isValid(), soundPos, IESounds.refinery , 0.5f
            );
        }
    }

    @Override
    public void registerCapabilities(CapabilityRegistrar<State> register)
    {
        register.registerAtOrNull(Capabilities.EnergyStorage.BLOCK, ENERGY_INPUT, state -> state.energy);
        register.registerAtOrNull(Capabilities.FluidHandler.BLOCK, OUTPUT_FLUID_CAP, state -> state.fluidOutputCap);
        register.registerAtOrNull(Capabilities.FluidHandler.BLOCK, INPUT_FLUID_CAP, state -> state.fluidInputCap);
        register.register(Capabilities.ItemHandler.BLOCK, (state, position) -> {
            if(OUTPUT_CAP.equals(position))
                return state.itemOutputCap;
            else if(INPUT_CAP.equals(position))
                return state.itemInputCap;
            else
                return null;
        });
        register.registerAtBlockPos(MachineInterfaceHandler.IMachineInterfaceConnection.CAPABILITY, REDSTONE_POS, state -> state.mifHandler);
    }

    @Override
    public void dropExtraItems(State state, Consumer<ItemStack> drop) {
        MBInventoryUtils.dropItems(state.inventory, drop);
    }


    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType)
    {
        return BallMillShapes.SHAPE_GETTER;
    }

    public static ComparatorManager<State> makeComparator()
    {
        return ComparatorManager.makeSimple(
                state -> Utils.calcRedstoneFromInventory(OUTPUT_SLOT, state.getInventory()), REDSTONE_POS
        );
    }



    public static class State implements IMultiblockState, ProcessContext.ProcessContextInMachine<BallMillRecipe>
    {
        private final AveragingEnergyStorage energy = new AveragingEnergyStorage(ENERGY_CAPACITY);

        private boolean active;
        private float barrelAngle;
        public final RedstoneControl.RSState rsState = RedstoneControl.RSState.enabledByDefault();
        public final MultiblockProcessor.InMachineProcessor<BallMillRecipe> processor;
        private final SlotwiseItemHandler inventory;
        public final BallMillTanks tanks = new BallMillTanks();


        private final IFluidTank[] tankArray = {tanks.input, tanks.output};
        private final Supplier<@Nullable IItemHandler> itemOutput;
        private final Supplier<@Nullable IFluidHandler> fluidOutput;
        private final IItemHandler itemInputCap;
        private final IItemHandler itemOutputCap;
        private final IFluidHandler fluidInputCap;
        private final IFluidHandler fluidOutputCap;
        private BooleanSupplier isPlayingSound = () -> false;
        private final MachineInterfaceHandler.IMachineInterfaceConnection mifHandler;

        public State(IInitialMultiblockContext<State> ctx)
        {
            final Runnable markDirty = ctx.getMarkDirtyRunnable();
            this.inventory = SlotwiseItemHandler.makeWithGroups(List.of(
                    new SlotwiseItemHandler.IOConstraintGroup(SlotwiseItemHandler.IOConstraint.NO_CONSTRAINT, NUM_INPUT_SLOTS),
                    new SlotwiseItemHandler.IOConstraintGroup(SlotwiseItemHandler.IOConstraint.OUTPUT, 1)
            ), markDirty);
            this.itemOutput = ctx.getCapabilityAt(Capabilities.ItemHandler.BLOCK, OUTPUT_OFFSET);
            this.fluidOutput = ctx.getCapabilityAt(Capabilities.FluidHandler.BLOCK, OUTPUT_FLUID_OFFSET);
            this.processor = new MultiblockProcessor.InMachineProcessor<>(
                    NUM_INPUT_SLOTS, 0, NUM_INPUT_SLOTS, markDirty, BallMillRecipe.RECIPES::getById
            );
            this.itemInputCap = new WrappingItemHandler(
                    getInventory(), true, false, new WrappingItemHandler.IntRange(0, NUM_INPUT_SLOTS)
            );
            this.itemOutputCap = new WrappingItemHandler(
                    getInventory(), false, true, new WrappingItemHandler.IntRange(OUTPUT_SLOT, OUTPUT_SLOT+1)
            );
            this.fluidInputCap = new ArrayFluidHandler(
                    false, true, markDirty, tanks.input
            );
            this.fluidOutputCap = ArrayFluidHandler.drainOnly(tanks.output, markDirty);
            this.mifHandler = () -> new MachineInterfaceHandler.MachineCheckImplementation[]{
                    new MachineInterfaceHandler.MachineCheckImplementation<>((BooleanSupplier)() -> this.active, MachineInterfaceHandler.BASIC_ACTIVE),
                    //new MachineInterfaceHandler.MachineCheckImplementation<>(processor, MachineInterfaceHandler.BASIC_ITEM_IN, processor.getMachineInterfaceOptions(true)),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(energy, MachineInterfaceHandler.BASIC_ENERGY),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(itemInputCap, MachineInterfaceHandler.BASIC_ITEM_IN),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(itemOutputCap, MachineInterfaceHandler.BASIC_ITEM_OUT),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(fluidInputCap, MachineInterfaceHandler.BASIC_FLUID_IN),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(fluidOutputCap, MachineInterfaceHandler.BASIC_FLUID_OUT)
            };
        }

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider)
        {
            nbt.put("energy", energy.serializeNBT(provider));
            nbt.put("inventory", inventory.serializeNBT(provider));
            nbt.put("tanks", tanks.toNBT(provider));
            nbt.put("processor", processor.toNBT(provider));
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            energy.deserializeNBT(provider, nbt.get("energy"));
            inventory.deserializeNBT(provider, nbt.getCompound("inventory"));
            processor.fromNBT(
                    nbt.get("processor"),
                    (getRecipe, data, p) -> new IMMultiblockProcessInMachine<>(getRecipe, data),
                    provider
            );
            tanks.readNBT(provider, nbt.getCompound("tanks"));
        }

        @Override
        public void writeSyncNBT(CompoundTag nbt, HolderLookup.Provider provider)
        {
            nbt.putBoolean("active", active);
        }

        @Override
        public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider)
        {
            active = nbt.getBoolean("active");
        }

        @Override
        public AveragingEnergyStorage getEnergy() { return energy; }

        @Override
        public IItemHandlerModifiable getInventory()
        {
            return inventory;
        }

        @Override
        public int[] getOutputSlots()
        {
            return new int[]{OUTPUT_SLOT};
        }

        @Override
        public IFluidTank[] getInternalTanks()
        {
            return tankArray;
        }

        @Override
        public int[] getOutputTanks()
        {
            return new int[]{1};
        }

        public boolean shouldRenderActive()
        {
            return active;
        }

        public float getBarrelAngle()
        {
            return barrelAngle;
        }

    }


    public record BallMillTanks(FluidTank input, FluidTank output)
    {

        public BallMillTanks()
        {
            this(new FluidTank(TANK_CAPACITY), new FluidTank(TANK_CAPACITY));
        }

        public Tag toNBT(HolderLookup.Provider provider)
        {
            CompoundTag tag = new CompoundTag();
            tag.put("in", input.writeToNBT(provider, new CompoundTag()));
            tag.put("out", output.writeToNBT(provider, new CompoundTag()));
            return tag;
        }

        public void readNBT(HolderLookup.Provider provider, CompoundTag tag)
        {
            input.readFromNBT(provider, tag.getCompound("in"));
            output.readFromNBT(provider, tag.getCompound("out"));
        }
    }
}
