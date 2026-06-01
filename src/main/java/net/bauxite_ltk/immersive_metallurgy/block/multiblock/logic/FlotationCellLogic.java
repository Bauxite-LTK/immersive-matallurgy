package net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic;


import blusunrize.immersiveengineering.api.energy.AveragingEnergyStorage;
import blusunrize.immersiveengineering.api.fluid.FluidUtils;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.RedstoneControl;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.CapabilityPosition;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.MultiblockFace;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.RelativeBlockFace;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import blusunrize.immersiveengineering.api.tool.MachineInterfaceHandler;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import blusunrize.immersiveengineering.common.fluids.ArrayFluidHandler;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.sound.MultiblockSound;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.process.IMMultiblockProcessInMachine;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.shapes.FlotationCellShapes;
import net.bauxite_ltk.immersive_metallurgy.crafting.FlotationCellRecipe;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
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
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FlotationCellLogic implements
        IMultiblockLogic<FlotationCellLogic.State>,
        IServerTickableComponent<FlotationCellLogic.State>,
        IClientTickableComponent<FlotationCellLogic.State> {
    public static final BlockPos MASTER_OFFSET = new BlockPos(2, 1, 1);
    public static final BlockPos REDSTONE_POS = new BlockPos(2, 1, 3);
    private static final MultiblockFace FLUID_OUTPUT_TAILING_OFFSET = new MultiblockFace(5, 0, 1, RelativeBlockFace.RIGHT);
    private static final MultiblockFace FLUID_OUTPUT_CONCENTRATE_OFFSET = new MultiblockFace(2, 2, 4, RelativeBlockFace.FRONT);
    private static final MultiblockFace INPUT_FLUID_1_OFFSET = new MultiblockFace(-1, 0, 1, RelativeBlockFace.LEFT);
    private static final MultiblockFace INPUT_FLUID_2_OFFSET = new MultiblockFace(2, 2, -1, RelativeBlockFace.BACK);
    private static final CapabilityPosition OUTPUT_FLUID_TAILING_CAP = CapabilityPosition.opposing(FLUID_OUTPUT_TAILING_OFFSET);
    private static final CapabilityPosition OUTPUT_FLUID_CONCENTRATE_CAP = CapabilityPosition.opposing(FLUID_OUTPUT_CONCENTRATE_OFFSET);
    private static final CapabilityPosition INPUT_FLUID_ORE_CAP = CapabilityPosition.opposing(INPUT_FLUID_1_OFFSET);
    private static final CapabilityPosition INPUT_FLUID_ADD_CAP = CapabilityPosition.opposing(INPUT_FLUID_2_OFFSET);
    private static final CapabilityPosition ENERGY_INPUT = new CapabilityPosition(2, 4, 0, RelativeBlockFace.UP);


    private static final Set<CapabilityPosition> FLUID_INPUT_CAPS = Set.of(
            INPUT_FLUID_ORE_CAP,
            INPUT_FLUID_ADD_CAP
    );

    private static final Set<CapabilityPosition> FLUID_OUTPUT_CAPS = Set.of(
            OUTPUT_FLUID_TAILING_CAP,
            OUTPUT_FLUID_CONCENTRATE_CAP
    );

    private static final Set<BlockPos> FLUID_INPUTS = FLUID_INPUT_CAPS.stream()
            .map(CapabilityPosition::posInMultiblock)
            .collect(Collectors.toSet());

    private static final Set<BlockPos> FLUID_OUTPUTS = FLUID_OUTPUT_CAPS.stream()
            .map(CapabilityPosition::posInMultiblock)
            .collect(Collectors.toSet());

    public static final int ORE_CAPACITY = 48 * FluidType.BUCKET_VOLUME;
    public static final int ADD_CAPACITY = FluidType.BUCKET_VOLUME;
    public static final int OUTPUT_CAPACITY = 12 * FluidType.BUCKET_VOLUME;
    public static final int ENERGY_CAPACITY = 96000;

    public static ResourceLocation MIF_CONDITION_FLUID_ORE = IMUtils.modRL("flotation_cell/tank_ore");
    public static ResourceLocation MIF_CONDITION_FLUID_ADD = IMUtils.modRL("flotation_cell/tank_add");
    public static ResourceLocation MIF_CONDITION_FLUID_CONCENTRATE = IMUtils.modRL("flotation_cell/tank_concentrate");
    public static ResourceLocation MIF_CONDITION_FLUID_TAILING = IMUtils.modRL("flotation_cell/tank_tailing");

    static
    {
        MachineInterfaceHandler.copyOptions(MIF_CONDITION_FLUID_ORE, MachineInterfaceHandler.BASIC_FLUID_IN);
        MachineInterfaceHandler.copyOptions(MIF_CONDITION_FLUID_ADD, MachineInterfaceHandler.BASIC_FLUID_IN);
        MachineInterfaceHandler.copyOptions(MIF_CONDITION_FLUID_CONCENTRATE, MachineInterfaceHandler.BASIC_FLUID_OUT);
        MachineInterfaceHandler.copyOptions(MIF_CONDITION_FLUID_TAILING, MachineInterfaceHandler.BASIC_FLUID_OUT);
    }

    public State createInitialState(IInitialMultiblockContext<State> capabilitySource) {
        return new State(capabilitySource);
    }
    private int tankLastTick = 0;
    @Override
    public void tickServer(IMultiblockContext<State> context) {
        final State state = context.getState();

        final boolean active = state.processor.tickServer(state, context.getLevel(), state.rsState.isEnabled(context));
        boolean updateFlag = false;
        if(active!=state.active || tankLastTick!=state.tanks.inputOre.getFluidAmount())
        {
            updateFlag = true;
            state.active = active;
            tankLastTick = state.tanks.inputOre.getFluidAmount();
            context.markMasterDirty();
            context.requestMasterBESync();
        }
        tryEnqueueProcesses(state, context.getLevel().getRawLevel());
        FlotationCellTanks tanks = state.tanks;
        int totalAmount = tanks.inputOre.getFluidAmount() + tanks.outputTailing.getFluidAmount() + tanks.outputConcentrate.getFluidAmount();

        if(totalAmount >= 48000 && !tanks.outputConcentrate.isEmpty()){
            FluidStack unitConcentrate = tanks.outputConcentrate().getFluid().copy();
            int extraAmount = totalAmount-48000;
            if(extraAmount<=100){
                unitConcentrate.setAmount(extraAmount);
            }
            else{
                unitConcentrate.setAmount(100);
            }


            if(state.tanks.overflow.fill(unitConcentrate, IFluidHandler.FluidAction.SIMULATE) == unitConcentrate.getAmount()){
                state.tanks.overflow.fill(unitConcentrate, IFluidHandler.FluidAction.EXECUTE);
                state.tanks.outputConcentrate.drain(unitConcentrate, IFluidHandler.FluidAction.EXECUTE);
            }

        }
        boolean output1 = FluidUtils.multiblockFluidOutput(
                state.fluidOutputTailing.get(), state.tanks.outputTailing,
                -1, -1,null
        );
        boolean output2 = FluidUtils.multiblockFluidOutput(
                state.fluidOutputConcentrate.get(), state.tanks.overflow,
                -1, -1,null
        );
        if((output1 || output2) && !updateFlag)
        {
            updateFlag = true;
            context.markMasterDirty();
            context.requestMasterBESync();
        }
    }

    private void tryEnqueueProcesses(State state, Level level) {
        if(state.energy.getEnergyStored() <= 0||state.processor.getQueueSize() >= state.processor.getMaxQueueSize())
            return;
        final FluidStack inputOre = state.tanks.inputOre.getFluid();
        final FluidStack inputAdd = state.tanks.inputAdd.getFluid();
        if(inputOre.isEmpty()&&inputAdd.isEmpty())
            return;
        RecipeHolder<FlotationCellRecipe> recipe = FlotationCellRecipe.findRecipe(level, inputOre, inputAdd);
        if(recipe==null){
            return;
        }
        IMMultiblockProcessInMachine<FlotationCellRecipe> process = new IMMultiblockProcessInMachine<>(recipe);
        process.setInputTanks(0, 1);
        state.processor.addProcessToQueue(process, level, false);
    }


    @Override
    public void tickClient(IMultiblockContext<State> context) {
        final State state = context.getState();
        if(state.active)
            state.bladeAngle = (state.bladeAngle+9)%360;
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
        register.register(Capabilities.FluidHandler.BLOCK, (state,position) -> {
            if(OUTPUT_FLUID_TAILING_CAP.equals(position))
                return state.fluidOutputTailingCap;
            else if(OUTPUT_FLUID_CONCENTRATE_CAP.equals(position))
                return state.fluidOutputConcentrateCap;
            else if(INPUT_FLUID_ORE_CAP.equals(position))
                return state.fluidInputOreCap;
            else if(INPUT_FLUID_ADD_CAP.equals(position))
                return state.fluidInputAddCap;
            else
                return null;

        });
        register.registerAtBlockPos(MachineInterfaceHandler.IMachineInterfaceConnection.CAPABILITY, REDSTONE_POS, state -> state.mifHandler);
    }


    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType)
    {
        return FlotationCellShapes.SHAPE_GETTER;
    }


    public static class State implements IMultiblockState, ProcessContext.ProcessContextInMachine<FlotationCellRecipe>
    {
        private final AveragingEnergyStorage energy = new AveragingEnergyStorage(ENERGY_CAPACITY);

        private boolean active;
        private float bladeAngle;
        public final RedstoneControl.RSState rsState = RedstoneControl.RSState.enabledByDefault();
        public final MultiblockProcessor.InMachineProcessor<FlotationCellRecipe> processor;
        public final FlotationCellTanks tanks = new FlotationCellTanks();


        private final IFluidTank[] tankArray = {tanks.inputOre, tanks.inputAdd, tanks.outputConcentrate, tanks.outputTailing, tanks.overflow };
        private final Supplier<@Nullable IFluidHandler> fluidOutputTailing;
        private final Supplier<@Nullable IFluidHandler> fluidOutputConcentrate;
        private final IFluidHandler fluidInputOreCap;
        private final IFluidHandler fluidInputAddCap;
        private final IFluidHandler fluidOutputConcentrateCap;
        private final IFluidHandler fluidOutputTailingCap;
        private BooleanSupplier isPlayingSound = () -> false;
        private final MachineInterfaceHandler.IMachineInterfaceConnection mifHandler;

        public State(IInitialMultiblockContext<State> ctx)
        {
            final Runnable markDirty = ctx.getMarkDirtyRunnable();
            //this.fluidOutput = ctx.getCapabilityAt(Capabilities.FluidHandler.BLOCK, OUTPUT_FLUID_OFFSET);
            this.processor = new MultiblockProcessor.InMachineProcessor<>(
                    1, 0, 1, markDirty, FlotationCellRecipe.RECIPES::getById
            );
            this.fluidOutputTailing = ctx.getCapabilityAt(Capabilities.FluidHandler.BLOCK, FLUID_OUTPUT_TAILING_OFFSET);
            this.fluidOutputConcentrate = ctx.getCapabilityAt(Capabilities.FluidHandler.BLOCK, FLUID_OUTPUT_CONCENTRATE_OFFSET);
            this.fluidInputOreCap = new ArrayFluidHandler(
                    true, true, markDirty, tanks.inputOre
            );
            this.fluidInputAddCap = new ArrayFluidHandler(
                    true, true, markDirty, tanks.inputAdd
            );
            this.fluidOutputConcentrateCap = new ArrayFluidHandler(
                    true,false, markDirty, tanks.overflow
            );
            this.fluidOutputTailingCap = new ArrayFluidHandler(
                    true,false, markDirty, tanks.outputTailing
            );
            this.mifHandler = () -> new MachineInterfaceHandler.MachineCheckImplementation[]{
                    new MachineInterfaceHandler.MachineCheckImplementation<>((BooleanSupplier)() -> this.active, MachineInterfaceHandler.BASIC_ACTIVE),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(energy, MachineInterfaceHandler.BASIC_ENERGY),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(tanks.inputOre, MIF_CONDITION_FLUID_ORE),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(tanks.inputAdd, MIF_CONDITION_FLUID_ADD),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(tanks.outputTailing, MIF_CONDITION_FLUID_TAILING),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(tanks.outputConcentrate, MIF_CONDITION_FLUID_CONCENTRATE)
            };
        }

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider)
        {
            nbt.put("energy", energy.serializeNBT(provider));
            nbt.put("tanks", tanks.toNBT(provider));
            nbt.put("processor", processor.toNBT(provider));
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            energy.deserializeNBT(provider, nbt.get("energy"));
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
            nbt.put("tanks", tanks.toNBT(provider));
        }

        @Override
        public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider)
        {

            active = nbt.getBoolean("active");
            //TODO THIS IS IMPORTANT WHEN GETTING DATA OUTSIDE FROM CTX
            tanks.readNBT(provider,nbt.getCompound("tanks"));
        }



        @Override
        public AveragingEnergyStorage getEnergy() { return energy; }


        @Override
        public IFluidTank[] getInternalTanks()
        {
            return tankArray;
        }

        //TODO OMG Here is so important!!!!
        @Override
        public int[] getOutputTanks()
        {
            return new int[]{2,3};
        }

        public boolean shouldRenderActive()
        {
            return true;
        }

        public boolean isActive()
        {
            return active;
        }

        public float getBladeAngle()
        {
            return bladeAngle;
        }

        public FluidStack getInputOreTankFluid(){
            return tanks.inputOre.getFluid();
        }

    }


    public record FlotationCellTanks(FluidTank inputOre, FluidTank inputAdd, FluidTank outputTailing, FluidTank outputConcentrate, FluidTank overflow)
    {

        public FlotationCellTanks()
        {
            this(
                    new FluidTank(ORE_CAPACITY),
                    new FluidTank(ADD_CAPACITY),
                    new FluidTank(OUTPUT_CAPACITY),
                    new FluidTank(OUTPUT_CAPACITY),
                    new FluidTank(OUTPUT_CAPACITY));
        }

        public Tag toNBT(HolderLookup.Provider provider)
        {
            CompoundTag tag = new CompoundTag();
            tag.put("inOre", inputOre.writeToNBT(provider, new CompoundTag()));
            tag.put("inAdd", inputAdd.writeToNBT(provider, new CompoundTag()));
            tag.put("outTailing", outputTailing.writeToNBT(provider, new CompoundTag()));
            tag.put("outConcentrate", outputConcentrate.writeToNBT(provider, new CompoundTag()));
            tag.put("overflow", overflow.writeToNBT(provider, new CompoundTag()));
            return tag;
        }

        public void readNBT(HolderLookup.Provider provider, CompoundTag tag)
        {
            inputOre.readFromNBT(provider, tag.getCompound("inOre"));
            inputAdd.readFromNBT(provider, tag.getCompound("inAdd"));
            outputTailing.readFromNBT(provider, tag.getCompound("outTailing"));
            outputConcentrate.readFromNBT(provider, tag.getCompound("outConcentrate"));
            overflow.readFromNBT(provider, tag.getCompound("overflow"));
        }
    }
}
