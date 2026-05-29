package net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceHandler;

public interface IUniHandler<R> {

    int receiveResource(R resource, boolean simulate);

    R extractResource(R resource, boolean simulate);

    R extractResource(int amount, boolean simulate);

    int getResourceAmount(int storageId);

    int getCapacity(int storageId);

    int getStorages();

    R getResource(int storageId);

}
