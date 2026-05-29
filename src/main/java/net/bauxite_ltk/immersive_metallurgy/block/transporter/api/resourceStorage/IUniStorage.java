package net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceStorage;

public interface IUniStorage<R> {
    int receiveResource(R resource, boolean simulate);

    R extractResource(R resource, boolean simulate);

    R extractResource(int amount, boolean simulate);

    int getResourceAmount();

    int getCapacity();

    R getResource();

    default boolean isEmpty(){
        return getResourceAmount() == 0;
    }
}
