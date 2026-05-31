package net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceHandler;

import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FluidUniHandler implements IUniHandler<FluidStack>, IFluidHandler {
    IFluidHandler handler;

    protected FluidUniHandler(IFluidHandler handler){
        this.handler = handler;
    }

    public static FluidUniHandler cast(IFluidHandler fluidHandler){
        return new FluidUniHandler(fluidHandler);
    }

    @Override
    public int receiveResource(FluidStack fluidStack, boolean simulate) {
        return handler.fill(fluidStack, simulate?FluidAction.SIMULATE:FluidAction.EXECUTE);
    }

    @Override
    public int receiveResource(FluidStack fluidStack, int amount, boolean simulate) {
        return handler.fill(fluidStack.copyWithAmount(amount), simulate?FluidAction.SIMULATE:FluidAction.EXECUTE);
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
    public int getStoragesCount() {
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

    public int getFluidAmount(int i){
        return getResourceAmount(i);
    }


    public int setFluidAmount(Fluid fluid, int amount){
        for(int i = 0; i < getTanks(); i++){
            if(getFluidInTank(i).getFluid().isSame(fluid) || getFluidInTank(i).isEmpty()){
                return setTankFluidAmount(i, fluid, amount);
            }
        }
        return 0;
    }

    private int setTankFluidAmount(int i, Fluid fluid, int amount){
        int thisAmount = getFluidAmount(i);
        if(amount > getCapacity(i)) IMUtils.LOGGER.warn("FluidUniHandler Set Fluid Amount: Larger Than Capacity!");
        if(amount == thisAmount) return getFluidAmount(i);
        if(amount > thisAmount){
            int fillResult = fill(new FluidStack(fluid, amount-thisAmount), IFluidHandler.FluidAction.EXECUTE);
            if(fillResult != amount - thisAmount) IMUtils.LOGGER.error("FluidUniHandler Set Fluid Amount: Unexpected Fill Amount!");
            return getFluidAmount(i);
        }
        else {
            int drainResult = drain(thisAmount - amount, IFluidHandler.FluidAction.EXECUTE).getAmount();
            if(drainResult != thisAmount - amount) IMUtils.LOGGER.error("FluidUniHandler Set Fluid Amount: Unexpected Drain Amount!");
            return getFluidAmount(i);
        }
    }
}
