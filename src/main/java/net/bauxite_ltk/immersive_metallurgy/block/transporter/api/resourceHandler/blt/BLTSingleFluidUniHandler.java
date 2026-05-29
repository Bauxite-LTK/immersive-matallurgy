package net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceHandler.blt;

import net.bauxite_ltk.immersive_metallurgy.block.transporter.api.BlocklikeFluidTransporterBE;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.api.BlocklikeResourceTransporter;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceStorage.FluidUniStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class BLTSingleFluidUniHandler extends BLTSingleUniHandler<FluidStack> implements IFluidHandler {
    FluidUniStorage fluidUniStorage;

    public BLTSingleFluidUniHandler(FluidUniStorage fluidUniStorage, BlocklikeFluidTransporterBE instance, Direction facing) {
        super(fluidUniStorage, instance, facing);
        this.fluidUniStorage = fluidUniStorage;
    }

    @Override
    public int getTanks() {
        return fluidUniStorage.getTanks();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        return fluidUniStorage.getResource();
    }

    @Override
    public int getTankCapacity(int i) {
        return fluidUniStorage.getCapacity();
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return fluidUniStorage.isFluidValid(i, fluidStack);
    }

    @Override
    public int fill(@NotNull FluidStack fluidStack, FluidAction fluidAction) {
        return receiveResource(fluidStack, fluidAction.simulate());
    }

    @Override
    public @NotNull FluidStack drain(@NotNull FluidStack fluidStack, FluidAction fluidAction) {
        return extractResource(fluidStack, fluidAction.simulate());
    }

    @Override
    public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
        return extractResource(i, fluidAction.simulate());
    }

    public int setFluidAmount(Fluid fluid, int amount){
        return fluidUniStorage.setFluidAmount(fluid,amount);
    }
}
