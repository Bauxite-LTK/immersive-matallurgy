package net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceHandler;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FluidUniHandler implements IUniHandler<FluidStack>, IFluidHandler {
    IFluidHandler handler;

    private FluidUniHandler(IFluidHandler handler){
        this.handler = handler;
    }

    public static FluidUniHandler cast(IFluidHandler fluidHandler){
        return new FluidUniHandler(fluidHandler);
    }

    @Override
    public int receiveResource(FluidStack resource, boolean simulate) {
        return handler.fill(resource, simulate?FluidAction.SIMULATE:FluidAction.EXECUTE);
    }

    @Override
    public FluidStack extractResource(FluidStack resource, boolean simulate) {
        return handler.drain(resource, simulate?FluidAction.SIMULATE:FluidAction.EXECUTE);
    }

    @Override
    public FluidStack extractResource(int amount, boolean simulate) {
        return handler.drain(amount, simulate?FluidAction.SIMULATE:FluidAction.EXECUTE);
    }

    @Override
    public int getResourceAmount(int storageId) {
        return handler.getFluidInTank(storageId).getAmount();
    }

    @Override
    public int getCapacity(int storageId) {
        return handler.getTankCapacity(storageId);
    }

    @Override
    public int getStorages() {
        return handler.getTanks();
    }

    @Override
    public FluidStack getResource(int storageId) {
        return handler.getFluidInTank(storageId);
    }





    @Override
    public int getTanks() {
        return handler.getTanks();
    }

    @Override
    public FluidStack getFluidInTank(int i) {
        return handler.getFluidInTank(i);
    }

    @Override
    public int getTankCapacity(int i) {
        return handler.getTankCapacity(i);
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return handler.isFluidValid(i,fluidStack);
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        return handler.fill(fluidStack, fluidAction);
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return handler.drain(fluidStack, fluidAction);
    }

    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        return handler.drain(i,fluidAction);
    }
}
