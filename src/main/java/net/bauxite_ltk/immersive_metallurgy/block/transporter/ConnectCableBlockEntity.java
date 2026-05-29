package net.bauxite_ltk.immersive_metallurgy.block.transporter;

import blusunrize.immersiveengineering.common.blocks.IEBaseBlockEntity;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.PlacementLimitation;
import blusunrize.immersiveengineering.common.blocks.ticking.IEServerTickableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConnectCableBlockEntity extends IEBaseBlockEntity implements IEServerTickableBE,
        IEBlockInterfaces.IStateBasedDirectional, IEBlockInterfaces.IPlacementInteraction{

    int transferLimit;
    byte connections = 0;
    List<ConnectionInfo> connectionInfoList = new ArrayList<>(6);

    public ConnectCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int transferLimit) {
        super(type, pos, state);
        this.transferLimit = transferLimit;
    }

    @Override
    public void readCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider) {

    }

    @Override
    public void writeCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider) {

    }


    @Override
    public void tickServer() {

    }

    @Override
    public void onBEPlaced(BlockPlaceContext ctx) {

    }

    @Override
    public @NotNull Property<Direction> getFacingProperty() {
        return ElectricCableBlock.DEFAULT_FACING_PROP;
    }

    @Override
    public @NotNull PlacementLimitation getFacingLimitation() {
        return PlacementLimitation.SIDE_CLICKED;
    }

    @Override
    public boolean mirrorFacingOnPlacement(LivingEntity placer)
    {
        return true;
    }

    @Override
    public boolean canHammerRotate(Direction side, Vec3 hit, LivingEntity entity)
    {
        return false;
    }


    protected static class ConnectionInfo{
        BlockPos rootTerminal;
        ConnectionStatus status;
        BlockPos previous;
        BlockPos next;

        public ConnectionInfo(BlockPos rootTerminal, ConnectionStatus status, BlockPos previous, BlockPos next){
            this.rootTerminal = rootTerminal;
            this.status = status;
            this.previous = previous;
            this.next = next;
        }
    }

    public enum ConnectionStatus{
        ROOT,
        COMMON
    }
}
