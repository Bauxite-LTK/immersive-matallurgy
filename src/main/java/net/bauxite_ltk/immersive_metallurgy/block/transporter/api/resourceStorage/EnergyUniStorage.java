package net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceStorage;

import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyUniStorage implements IUniStorage<Integer> {
    IEnergyStorage energyStorage;

    public EnergyUniStorage(IEnergyStorage energyStorage){
        this.energyStorage = energyStorage;
    }

    @Override
    public int receiveResource(Integer i, boolean simulate) {
        return energyStorage.receiveEnergy(i,simulate);
    }

    @Override
    public Integer extractResource(Integer i, boolean simulate) {
        return energyStorage.extractEnergy(i, simulate);
    }

    @Override
    public Integer extractResource(int amount, boolean simulate) {
        return energyStorage.extractEnergy(amount, simulate);
    }

    @Override
    public int getResourceAmount() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getCapacity() {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public Integer getResource() {
        return energyStorage.getEnergyStored();
    }
}
