package net.bauxite_ltk.immersive_metallurgy.block.metal;

import blusunrize.immersiveengineering.api.utils.SafeChunkUtils;
import net.bauxite_ltk.immersive_metallurgy.ImmersiveMetallurgy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.LinkedList;
import java.util.List;

public interface IElectricCableConnectionBE {

    List<ConnectionInfo> getConnectionInfoList();

    byte getConnectionByte();

    BlockEntity getBE();

    IEnergyStorage getNeighborHandler(Direction direction);

    IElectricCableConnectionBE getNeighborIElectricCable(Direction direction);

    int getTransferLimit();

    default void updateNode(){
        List<ConnectionInfo> connectionInfoList = getConnectionInfoList();
        int connectionByte = getConnectionByte();
        // remove invalid connections
        // TODO Is This Necessary?
        connectionInfoList.removeIf(info -> info.status.equals(ConnectionStatus.INVALID));

        // perform node update behaviour
        for(ConnectionInfo info : connectionInfoList){
            BlockPos rootPos = info.rootTerminal;
            ConnectionStatus status = info.status;

            if(status.equals(ConnectionStatus.ROOT)){
                rootUpdateSubnet(rootPos, getBE().getBlockPos());
            }

            else if(status.equals(ConnectionStatus.COMMON)){
                IEnergyStorage source = getNeighborHandler(info.previous);
                if (source == null){
                    ImmersiveMetallurgy.LOGGER.info("invalid");
                    info.setStatus(ConnectionStatus.INVALID);
                    updateNode();
                }
            }
        }
    }

    default void updateAllRootNode(){
        List<ConnectionInfo> connectionInfoList = getConnectionInfoList();
        for(ConnectionInfo info : connectionInfoList){
            BlockEntity rootBE = null;
            if (getBE().getLevel() != null) rootBE = SafeChunkUtils.getSafeBE(getBE().getLevel(), info.rootTerminal);
            if(rootBE == null) continue;
            if(rootBE instanceof IElectricCableConnectionBE electricCable) electricCable.updateNode();
        }
    }

    default void rootUpdateSubnet(BlockPos infoRootPos, BlockPos currentRootPos){
        //forgive my code using BFS, I just don't want to write a new logic.
        List<BlockPos> openList = new LinkedList<>();
        List<BlockPos> closeList = new LinkedList<>();
        openList.add(infoRootPos);
        for(int i = 0; i < 1024; i++){
            ImmersiveMetallurgy.LOGGER.info("rootUpdateSubnet: i = {}", i);
            if(openList.isEmpty()) break;
            BlockPos curPos = openList.getFirst();
            openList.removeFirst();
            if(closeList.contains(curPos)) continue;
            BlockEntity be = null;
            if (getBE().getLevel() != null) be = SafeChunkUtils.getSafeBE(getBE().getLevel(), curPos);
            if(be == null) continue;
            if(be instanceof IElectricCableConnectionBE electricCable){
                //if currentRoot is not equal to root in info, means this node was common and just change to root
                if(infoRootPos != currentRootPos && getConnectionInfo(infoRootPos) != null){
                    getConnectionInfo(infoRootPos).rootTerminal = currentRootPos;
                }
                electricCable.tryClaimNext(currentRootPos);
                Direction nextDir = electricCable.getConnectionInfo(currentRootPos).next;
                if(nextDir != null)
                    openList.addLast(curPos.relative(nextDir));

                closeList.addFirst(curPos);
            }
        }
    }


    default void tryClaimNext(BlockPos rootOfSubnet){
        byte connections = getConnectionByte();
        // update nextList
        for(int i = 0; i < 6; i++) {
            Direction dir = Direction.from3DDataValue(i);
            //node should not check previous direction
            if(getConnectionInfo(rootOfSubnet).previous.equals(dir)) continue;
            //check if the direction is connected
            if (((connections >> i) & 1) != 1) {
                getConnectionInfo(rootOfSubnet).removeNext(dir);
                continue;
            }
            //check if the direction has available handler
            IEnergyStorage handler = getNeighborHandler(dir);
            if(handler!=null){
                getConnectionInfo(rootOfSubnet).addNext(dir);

            }
            else{
                getConnectionInfo(rootOfSubnet).removeNext(dir);
            }
        }

        // from nextList, try claim neighbor pipe
        Direction nextDir = getConnectionInfo(rootOfSubnet).next;
        IElectricCableConnectionBE neighbor = getNeighborIElectricCable(nextDir);
        if(neighbor!=null){
            //if pipe is not in subnet, then add it
            if(neighbor.getConnectionInfo(rootOfSubnet)==null){
                neighbor.setSelfCommonToSubnet(rootOfSubnet, nextDir.getOpposite());
            }
        }

    }

    default void setSelfCommonToSubnet(BlockPos rootOfSubnet, Direction previousDir){
        List<ConnectionInfo> connectionInfoList = getConnectionInfoList();
        //FIXME when we complete the algorithm, delete this check.
        for(ConnectionInfo info : connectionInfoList){
            if(info.rootTerminal.equals(rootOfSubnet)){
                throw new RuntimeException("duplicate set pipe common status in subnet: " + rootOfSubnet);
            }
        }

        connectionInfoList.add(new ConnectionInfo(rootOfSubnet, ConnectionStatus.COMMON, previousDir, null));

    }

    default IEnergyStorage getTailEnergyHandler(BlockPos rootTerminal){
        Direction nextDir = getConnectionInfo(rootTerminal).next;
        if(nextDir == null) return null;
        IEnergyStorage energyStorage = getNeighborHandler(nextDir);
        if(energyStorage == null) return null;
        if(energyStorage instanceof ElectricCableEnergyStorge electricCableES){
            return electricCableES.electricCable.getTailEnergyHandler(rootTerminal);
        }
        else {
            return energyStorage;
        }
    }




    class ElectricCableEnergyStorge extends EnergyStorage {
        IElectricCableConnectionBE electricCable;
        Direction facing;

        public ElectricCableEnergyStorge(int transferLimit, IElectricCableConnectionBE electricCable, Direction facing) {
            super(0, transferLimit);
            this.electricCable = electricCable;
            this.facing = facing;
        }

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            BlockPos thisPos = electricCable.getBE().getBlockPos();
            if(electricCable.getConnectionInfo(thisPos) == null){
                electricCable.getConnectionInfoList().add(new ConnectionInfo(thisPos,ConnectionStatus.ROOT, facing, null));
                electricCable.updateNode();
            }
            IEnergyStorage tailEnergyStorge = electricCable.getTailEnergyHandler(electricCable.getBE().getBlockPos());
            int transferLimit = electricCable.getTransferLimit();
            if(tailEnergyStorge != null)
                return tailEnergyStorge.receiveEnergy(Math.min(toReceive, transferLimit), simulate);
            else
                return 0;
        }
    }


    default ConnectionInfo getConnectionInfo(BlockPos root){
        List<ConnectionInfo> connectionInfoList = getConnectionInfoList();
        for(ConnectionInfo info : connectionInfoList){
            if(info.rootTerminal.equals(root)) return info;
        }
        return null;
    }

    class ConnectionInfo{
        BlockPos rootTerminal;
        ConnectionStatus status;
        Direction previous;
        Direction next;

        public ConnectionInfo(BlockPos rootTerminal, ConnectionStatus status, Direction previous, Direction next){
            this.rootTerminal = rootTerminal;
            this.status = status;
            this.previous = previous;
            this.next = next;
        }

        public void removeNext(Direction dir){
            if(this.next == null) return;
            if(this.next.equals(dir)){
                this.next = null;
            }
        }

        public void addNext(Direction dir){
            if(this.next!=null && this.next != dir) throw new RuntimeException("Electric Cable: duplicate add next");
            this.next = dir;
        }

        public void setStatus(ConnectionStatus status) {
            this.status = status;
        }
    }

    enum ConnectionStatus{
        ROOT,
        COMMON,
        INVALID
    }

}
