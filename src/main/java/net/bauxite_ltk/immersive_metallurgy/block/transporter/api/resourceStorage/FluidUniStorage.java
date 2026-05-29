package net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceStorage;

import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class FluidUniStorage implements IUniStorage<FluidStack>, IFluidHandler{
    IFluidTank tank;

    public FluidUniStorage(int initialCapacity){
        this.tank = new FluidTank(initialCapacity);
    }

    public FluidUniStorage(IFluidTank tank){
        this.tank = tank;
    }

    @Override
    public int receiveResource(FluidStack fluidStack, boolean simulate) {
        return tank.fill(fluidStack, simulate? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public FluidStack extractResource(FluidStack fluidStack, boolean simulate) {
        return extractResource(fluidStack.getAmount(), simulate);
    }


    @Override
    public FluidStack extractResource(int amount, boolean simulate) {
        return tank.drain(amount, simulate? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
    }


    @Override
    public int getResourceAmount() {
        return tank.getFluidAmount();
    }

    @Override
    public int getCapacity() {
        return tank.getCapacity();
    }

    @Override
    public FluidStack getResource() {
        return tank.getFluid();
    }



    public int getTanks(){
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int i) {
        return tank.getFluid();
    }

    @Override
    public int getTankCapacity(int i) {
        return tank.getCapacity();
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack){
        return tank.isFluidValid(fluidStack);
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        return tank.fill(fluidStack, fluidAction);
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return tank.drain(fluidStack,fluidAction);
    }

    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        return tank.drain(i, fluidAction);
    }

    public int setFluidAmount(Fluid fluid, int amount){
        int thisAmount = tank.getFluidAmount();
        if(amount>tank.getCapacity()) IMUtils.LOGGER.warn("Pressure Pipe Set Fluid Amount: Larger Than Capacity!");
        if(amount == thisAmount) return tank.getFluidAmount();
        if(!tank.getFluid().isEmpty() && !fluid.isSame(tank.getFluid().getFluid())) return 0;
        if(amount > thisAmount){
            int fillResult = tank.fill(new FluidStack(fluid, amount-thisAmount), IFluidHandler.FluidAction.EXECUTE);
            if(fillResult != amount - thisAmount) IMUtils.LOGGER.error("Pressure Pipe Set Fluid Amount: Unexpected Fill Amount!");
            return tank.getFluidAmount();
        }
        else {
            int drainResult = tank.drain(thisAmount - amount, IFluidHandler.FluidAction.EXECUTE).getAmount();
            if(drainResult != thisAmount - amount) IMUtils.LOGGER.error("Pressure Pipe Set Fluid Amount: Unexpected Drain Amount!");
            return tank.getFluidAmount();
        }
    }
}
