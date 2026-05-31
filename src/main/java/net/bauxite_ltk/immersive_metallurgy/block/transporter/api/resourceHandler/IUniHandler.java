package net.bauxite_ltk.immersive_metallurgy.block.transporter.api.resourceHandler;

public interface IUniHandler<R> {

    int receiveResource(R resource, boolean simulate);

    int receiveResource(R resource, int amount, boolean simulate);

    R extractResource(R resource, boolean simulate);

    R extractResource(int amount, boolean simulate);

    int getResourceAmount(int storageId);

    int getCapacity(int storageId);

    int getStoragesCount();

    R getResource(int storageId);

    default boolean isEmpty(int storageId){
        return getResourceAmount(storageId) == 0;
    }

    default boolean isAllEmpty(){
        for(int i = 0; i < getStoragesCount(); i++){
            if(!isEmpty(i)) return false;
        }
        return true;
    }

}
