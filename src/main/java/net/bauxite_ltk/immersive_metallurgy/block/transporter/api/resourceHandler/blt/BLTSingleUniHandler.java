package net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceHandler.blt;

import net.bauxite_ltk.immersive_metallurgy.block.transporter.api.BlocklikeResourceTransporter;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceHandler.IUniHandler;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceStorage.IUniStorage;
import net.minecraft.core.Direction;

public class BLTSingleUniHandler<R> implements IUniHandler<R> {

    private final IUniStorage<R> storage;
    private final BlocklikeResourceTransporter<R> blt;
    private final Direction facing;

    protected BLTSingleUniHandler(IUniStorage<R> storage, BlocklikeResourceTransporter<R> instance, Direction facing){
        this.storage = storage;
        this.blt = instance;
        this.facing = facing;
    }


    @Override
    public int receiveResource(R fluidStack, boolean simulate) {
        blt.trySetSource(facing);
        return storage.receiveResource(fluidStack, simulate);
    }

    @Override
    public R extractResource(R fluidStack, boolean simulate) {
        return storage.extractResource(fluidStack, simulate);
    }

    @Override
    public R extractResource(int amount, boolean simulate) {
        return storage.extractResource(amount, simulate);
    }

    @Override
    public int getResourceAmount(int storageId) {
        return storage.getResourceAmount();
    }

    @Override
    public int getCapacity(int storageId) {
        return storage.getCapacity();
    }

    @Override
    public int getStorages() {
        return 1;
    }

    @Override
    public R getResource(int storageId) {
        return storage.getResource();
    }

}
