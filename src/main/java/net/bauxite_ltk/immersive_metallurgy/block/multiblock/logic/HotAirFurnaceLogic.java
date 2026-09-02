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
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.shapes.HotAirFurnaceShapes;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.shapes.HydrocycloneShapes;
import net.bauxite_ltk.immersive_metallurgy.crafting.HotAirFurnaceRecipe;
import net.bauxite_ltk.immersive_metallurgy.crafting.HydrocycloneRecipe;
import net.bauxite_ltk.immersive_metallurgy.fluid.IMFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

public class HotAirFurnaceLogic implements
        IMultiblockLogic<HotAirFurnaceLogic.State>,
        IServerTickableComponent<HotAirFurnaceLogic.State>,
        IClientTickableComponent<HotAirFurnaceLogic.State> {
    public static final BlockPos MASTER_OFFSET = new BlockPos(1, 0, 1);
    public static final BlockPos REDSTONE_POS = new BlockPos(1, 3, 0);
    public static final MultiblockFace INPUT_GAS_OFFSET = new MultiblockFace(1,6,1, RelativeBlockFace.DOWN);
    public static final MultiblockFace OUTPUT_AIR_OFFSET = new MultiblockFace(1,3,3, RelativeBlockFace.FRONT);

    public static final CapabilityPosition OUTPUT_AIR_CAP = CapabilityPosition.opposing(OUTPUT_AIR_OFFSET);
    public static final CapabilityPosition INPUT_GAS_CAP = CapabilityPosition.opposing(INPUT_GAS_OFFSET);



    public static final int HOT_AIR_CAPACITY = 2000;

    @Override
    public State createInitialState(IInitialMultiblockContext<State> capabilitySource) {
        return new HotAirFurnaceLogic.State(capabilitySource);
    }

    @Override
    public void tickClient(IMultiblockContext<State> context) {
        final State state = context.getState();
        if(!state.isPlayingSound.getAsBoolean())
        {
            final Vec3 soundPos = context.getLevel().toAbsolute(new Vec3(3.5, 1.5, 1.5));
            state.isPlayingSound = MultiblockSound.startSound(
                    () -> state.active, context.isValid(), soundPos, IESounds.preheater , 0.5f
            );
        }
    }

    @Override
    public void tickServer(IMultiblockContext<State> context) {
        // Get State
        final HotAirFurnaceLogic.State state = context.getState();

        // IE Multiblock Processor seems like it will fail while handle recipes that cost Energy of 0.
        // So we have to set its energy cost to a non-zero value for our Elite Blast Furnace Recipes.
        // With code below, we actually make tricks to avoid energy cost, by continuously insert energy into the machine.
        state.energy.receiveEnergy(1000,false);

        // 'state.processor' do Tick and get status of 'active'
        final boolean active = state.processor.tickServer(state, context.getLevel(), state.rsState.isEnabled(context));

        // Request an update to the BE if the value of 'state.active' changes
        if(active!=state.active)
        {
            state.active = active;
            context.requestMasterBESync();
        }

        // Entry: Try adding new processes to the queue of processor
        enqueueProcesses(state, context.getLevel().getRawLevel());

        // output fluid
        FluidUtils.multiblockFluidOutput(
                state.fluidOutputAir.get(), state.tanks.outputAir,
                -1, -1,null
        );

        //update recipe progress percentage
        if(!state.processor.getQueue().isEmpty()){
            HotAirFurnaceRecipe recipe = state.processor.getQueue().getFirst().getRecipe(context.getLevel().getRawLevel());
            if(recipe!= null){
                int current =  state.processor.getQueue().getFirst().processTick;
                int total = recipe.getTotalProcessTime();
                state.recipeProgress = (float) current/total;
            }
            else{
                state.recipeProgress = 0;
            }
        }
        else{
            state.recipeProgress = 0;
        }


    }

    private void enqueueProcesses(HotAirFurnaceLogic.State state, Level level) {

        final FluidStack inputFluid = state.tanks.inputGas.getFluid();


        if(state.energy.getEnergyStored() <= 0||state.processor.getQueueSize() >= state.processor.getMaxQueueSize())
            return;
        RecipeHolder<HotAirFurnaceRecipe> recipe = HotAirFurnaceRecipe.findRecipe(level, inputFluid);
        if(recipe==null){
            return;
        }
        IMMultiblockProcessInMachine<HotAirFurnaceRecipe> process = new IMMultiblockProcessInMachine<HotAirFurnaceRecipe>(recipe);
        process.setInputTanks(0);
        state.processor.addProcessToQueue(process, level, false);
    }



    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return HotAirFurnaceShapes.SHAPE_GETTER;
    }

    @Override
    public void registerCapabilities(CapabilityRegistrar<HotAirFurnaceLogic.State> register)
    {
        register.register(Capabilities.FluidHandler.BLOCK, (state,position) -> {
            if(OUTPUT_AIR_CAP.equals(position))
                return state.outputFluidAirCap;
            else if(INPUT_GAS_CAP.equals(position))
                return state.inputFluidGasCap;
            else
                return null;
        });
        register.registerAtBlockPos(MachineInterfaceHandler.IMachineInterfaceConnection.CAPABILITY, REDSTONE_POS, state -> state.mifHandler);
    }


    public static class State implements IMultiblockState, ProcessContext.ProcessContextInMachine<HotAirFurnaceRecipe>
    {
        private final AveragingEnergyStorage energy = new AveragingEnergyStorage(1000);

        private boolean active;
        private float recipeProgress = 0;
        public final RedstoneControl.RSState rsState = RedstoneControl.RSState.enabledByDefault();
        public final MultiblockProcessor.InMachineProcessor<HotAirFurnaceRecipe> processor;
        public final HotAirFurnaceLogic.HotAirFurnaceTanks tanks = new HotAirFurnaceLogic.HotAirFurnaceTanks();
        //private int temperature = 0;


        private final IFluidTank[] tankArray = {tanks.inputGas, tanks.outputAir};
        private final Supplier<@Nullable IFluidHandler> fluidOutputAir;
        private final IFluidHandler inputFluidGasCap;
        private final IFluidHandler outputFluidAirCap;
        private BooleanSupplier isPlayingSound = () -> false;
        private final MachineInterfaceHandler.IMachineInterfaceConnection mifHandler;

        public State(IInitialMultiblockContext<HotAirFurnaceLogic.State> ctx)
        {
            final Runnable markDirty = ctx.getMarkDirtyRunnable();
            //this.fluidOutput = ctx.getCapabilityAt(Capabilities.FluidHandler.BLOCK, OUTPUT_FLUID_OFFSET);
            this.processor = new MultiblockProcessor.InMachineProcessor<>(
                    1, 0, 1, markDirty, HotAirFurnaceRecipe.RECIPES::getById
            );
            this.fluidOutputAir = ctx.getCapabilityAt(Capabilities.FluidHandler.BLOCK, OUTPUT_AIR_OFFSET);
            this.inputFluidGasCap = new ArrayFluidHandler(
                    false, true, markDirty, tanks.inputGas
            );
            this.outputFluidAirCap = new ArrayFluidHandler(
                    true, false, markDirty, tanks.outputAir
            );
            this.mifHandler = () -> new MachineInterfaceHandler.MachineCheckImplementation[]{
                    new MachineInterfaceHandler.MachineCheckImplementation<>((BooleanSupplier)() -> this.active, MachineInterfaceHandler.BASIC_ACTIVE),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(energy, MachineInterfaceHandler.BASIC_ENERGY),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(tanks.inputGas, MachineInterfaceHandler.BASIC_FLUID_IN),
                    new MachineInterfaceHandler.MachineCheckImplementation<>(tanks.outputAir, MachineInterfaceHandler.BASIC_FLUID_OUT),
            };
        }

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider)
        {
            nbt.put("energy", energy.serializeNBT(provider));
            nbt.put("tanks", tanks.toNBT(provider));
            nbt.put("processor", processor.toNBT(provider));
            //nbt.putInt("temperature", temperature);
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
            //temperature = nbt.getInt("temperature");
        }

        @Override
        public void writeSyncNBT(CompoundTag nbt, HolderLookup.Provider provider)
        {

            nbt.putBoolean("active", active);
            nbt.put("tanks", tanks.toNBT(provider));
            //nbt.putInt("temperature", temperature);
            nbt.putFloat("progress", recipeProgress);
        }

        @Override
        public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider)
        {

            active = nbt.getBoolean("active");
            tanks.readNBT(provider,nbt.getCompound("tanks"));
            //temperature = nbt.getInt("temperature");
            recipeProgress = nbt.getFloat("progress");
        }


        @Override
        public AveragingEnergyStorage getEnergy() { return energy; }

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

        public float getRecipeProgress() {
            return recipeProgress;
        }

        public boolean shouldRenderActive()
        {
            return true;
        }

        public boolean isActive()
        {
            return active;
        }




    }

    public record HotAirFurnaceTanks(FluidTank inputGas, FluidTank outputAir){
        public HotAirFurnaceTanks(){
            this(
                new FluidTank(HOT_AIR_CAPACITY){
                    @Override
                    public boolean isFluidValid(final FluidStack stack) {
                        return stack.is(IMFluids.WATER_GAS.source());
                    }
                },
                new FluidTank(HOT_AIR_CAPACITY){
                    @Override
                    public boolean isFluidValid(final FluidStack stack) {
                        return stack.is(IMFluids.HOT_AIR.source());
                    }
                }
            );
        }

        public Tag toNBT(HolderLookup.Provider provider)
        {
            CompoundTag tag = new CompoundTag();
            tag.put("inputGas", inputGas.writeToNBT(provider, new CompoundTag()));
            tag.put("outputAir", outputAir.writeToNBT(provider, new CompoundTag()));
            return tag;
        }

        public void readNBT(HolderLookup.Provider provider, CompoundTag tag)
        {
            inputGas.readFromNBT(provider, tag.getCompound("inputGas"));
            outputAir.readFromNBT(provider, tag.getCompound("outputAir"));
        }
    }
}
