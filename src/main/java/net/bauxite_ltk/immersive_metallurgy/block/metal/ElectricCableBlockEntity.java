package net.bauxite_ltk.immersive_metallurgy.block.metal;

import blusunrize.immersiveengineering.api.utils.DirectionUtils;
import blusunrize.immersiveengineering.api.utils.SafeChunkUtils;
import blusunrize.immersiveengineering.common.blocks.BlockCapabilityRegistration;
import blusunrize.immersiveengineering.common.blocks.IEBaseBlockEntity;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.PlacementLimitation;
import blusunrize.immersiveengineering.common.blocks.ticking.IEServerTickableBE;
import blusunrize.immersiveengineering.common.util.IEBlockCapabilityCaches;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.bauxite_ltk.immersive_metallurgy.ImmersiveMetallurgy;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ElectricCableBlockEntity extends IEBaseBlockEntity implements IElectricCableConnectionBE ,IEServerTickableBE,
        IEBlockInterfaces.IStateBasedDirectional, IEBlockInterfaces.IPlacementInteraction {


    protected int transferLimit;
    protected byte connections = 0;
    protected Direction mainDir;
    protected Direction subDir;
    protected boolean mainTerminal = false;
    protected boolean subTerminal = false;
    protected Map<Direction, Direction> connectionAndAttachment = new HashMap<>(6);

    protected List<ConnectionInfo> connectionInfoList = new ArrayList<>();

    public Object2BooleanMap<Direction> sideConfig = new Object2BooleanOpenHashMap<>();
    {
        for(Direction d : DirectionUtils.VALUES){
            sideConfig.put(d, true);
        }
    }
    protected final Map<Direction, IEnergyStorage> sidedHandlers = new EnumMap<>(Direction.class);
    protected final Map<Direction, IEBlockCapabilityCaches.IEBlockCapabilityCache<IEnergyStorage>> neighbors = IEBlockCapabilityCaches.allNeighbors(
            Capabilities.EnergyStorage.BLOCK, this
    );
    {
        for(Direction f : DirectionUtils.VALUES)
            sidedHandlers.put(f, new ElectricCableEnergyStorge(1000, this, f));
    }




    public ElectricCableBlockEntity(BlockPos pos, BlockState state, int transferLimit) {
        super(IMBlockEntities.ELECTRIC_CABLE.get(), pos, state);
        this.transferLimit = transferLimit;
    }

    public static ElectricCableBlockEntity forLv(BlockPos pos, BlockState state){
        return new ElectricCableBlockEntity(pos, state, 512);
    }

    public static ElectricCableBlockEntity forMv(BlockPos pos, BlockState state){
        return new ElectricCableBlockEntity(pos, state, 2048);
    }

    public static ElectricCableBlockEntity forHv(BlockPos pos, BlockState state){
        return new ElectricCableBlockEntity(pos, state, 8192);
    }

    @Override
    public void readCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider) {
        boolean changed = false;
        boolean hasMain = nbt.getBoolean("hasMain");
        if(hasMain){
            Direction oldMain = mainDir;
            boolean oldMainTerminal = mainTerminal;
            mainDir = Direction.from3DDataValue(nbt.getInt("mainDir"));
            mainTerminal = nbt.getBoolean("mainTerminal");
            if(oldMain != mainDir || oldMainTerminal != mainTerminal) changed = true;
        }
        else{
            if(mainDir != null) changed = true;
            this.mainDir = null;
        }
        boolean hasSub = nbt.getBoolean("hasSub");
        if(hasSub){
            Direction oldSub = subDir;
            boolean oldSubTerminal = subTerminal;
            this.subDir = Direction.from3DDataValue(nbt.getInt("subDir"));
            subTerminal = nbt.getBoolean("subTerminal");
            if(oldSub != subDir || oldSubTerminal != subTerminal) changed = true;
        }
        else{
            if(subDir != null) changed = true;
            this.subDir = null;
        }

        byte oldConns = connections;
        this.connections = nbt.getByte("connections");
        if(oldConns != connections) changed = true;


        Map<Direction, Direction> oldMap = connectionAndAttachment;
        connectionAndAttachment.clear();
        int[] connectionAttachArray = nbt.getIntArray("connectionAndAttachment");
        for(int i = 0; i < 6; i++){
            if(connectionAttachArray[i] != -1){
                Direction connectionDir = Direction.from3DDataValue(i);
                Direction attachmentDir = Direction.from3DDataValue(connectionAttachArray[i]);
                connectionAndAttachment.put(connectionDir, attachmentDir);
            }
        }
        if(!oldMap.equals(connectionAndAttachment)) changed = true;


        if(level!=null&&level.isClientSide&&changed)
        {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
            markContainingBlockForUpdate(state);
        }
    }

    @Override
    public void writeCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider) {
        nbt.putByte("connections", connections);


        nbt.putBoolean("hasMain", mainDir != null);
        if(mainDir != null){
            nbt.putInt("mainDir", mainDir.get3DDataValue());
            nbt.putBoolean("mainTerminal", isMainTerminal());
        }


        nbt.putBoolean("hasSub", subDir != null);
        if(subDir != null){
            nbt.putInt("subDir", subDir.get3DDataValue());
            nbt.putBoolean("subTerminal", isSubTerminal());
        }


        int[] connectionAttachArray = new int[6];
        Arrays.fill(connectionAttachArray, -1);
        for(Direction connection :  connectionAndAttachment.keySet()){
            Direction attachment = connectionAndAttachment.get(connection);
            connectionAttachArray[connection.get3DDataValue()] = attachment.get3DDataValue();
        }
        nbt.putIntArray("connectionAndAttachment", connectionAttachArray);

    }


    @Override
    public void tickServer() {

    }


    private void invalidateHandler(Direction side)
    {
        IEnergyStorage energyStorage = sidedHandlers.get(side);
        if(energyStorage!=null)
        {
            sidedHandlers.put(side, null);
            invalidateCapabilities();
        }
    }

    private void setValidHandler(Direction side)
    {
        IEnergyStorage energyStorage = sidedHandlers.get(side);
        if(energyStorage==null)
        {
            sidedHandlers.put(side, new ElectricCableEnergyStorge(transferLimit,this, side));
            invalidateCapabilities();
        }
    }

    public void toggleSide(Direction side)
    {
        boolean newSideConnected = !sideConfig.getBoolean(side);
        setSide(side, newSideConnected);
    }

    public void setSide(Direction side, boolean connectable)
    {
        setSide(side, connectable, true);
    }

    public void setSide(Direction side, boolean connectable, boolean firstPipe)
    {
        sideConfig.put(side, connectable);
        if(connectable)
            setValidHandler(side);
        else
            invalidateHandler(side);
        setChanged();
        if(firstPipe)
        {
            BlockEntity neighborTile = level.getBlockEntity(getBlockPos().relative(side));
            if(neighborTile instanceof ElectricCableBlockEntity electricCable)
                electricCable.setSide(side.getOpposite(), connectable, false);
            updateConnectionByte(side); //yes, this is not meant for neighborTile
        }
        level.blockEvent(getBlockPos(), getBlockState().getBlock(), 0, 0);
    }

    public void setSideWithoutUpdate(Direction side, boolean connectable, boolean firstPipe)
    {
        sideConfig.put(side, connectable);
        if(connectable)
            setValidHandler(side);
        else
            invalidateHandler(side);
        setChanged();
        if(firstPipe)
        {
            BlockEntity neighborTile = level.getBlockEntity(getBlockPos().relative(side));
            if(neighborTile instanceof ElectricCableBlockEntity electricCable)
                electricCable.setSideWithoutUpdate(side.getOpposite(), connectable, false);
            updateConnectionByte(side); //yes, this is not meant for neighborTile
        }
        //level.blockEvent(getBlockPos(), getBlockState().getBlock(), 0, 0);
    }

    public boolean updateConnectionByte(Direction dir)
    {
        if(level==null||level.isClientSide||!SafeChunkUtils.isChunkSafe(level, worldPosition.relative(dir)))
            return false;
        final byte oldConn = connections;
        int i = dir.get3DDataValue();
        int mask = 1<<i;
        connections &= (byte) ~mask;
        connectionAndAttachment.remove(dir);

        if(sideConfig.getBoolean(dir))
        {
            IEnergyStorage energyStorage = neighbors.get(dir).getCapability();
            IElectricCableConnectionBE be = getNeighborIElectricCable(dir);
            if(energyStorage!=null){
                if(be == null && isDirectionTerminal(dir)){
                    connections |= (byte) mask;
                }
                else if(be instanceof ElectricCableBlockEntity electricCable){
                    for(Direction attachDir : getTerminalDirections()){
                        if(electricCable.getTerminalDirections().contains(attachDir)){
                            connections |= (byte) mask;
                            connectionAndAttachment.put(dir,attachDir);
                            ImmersiveMetallurgy.LOGGER.info("connected neighbor cable");
                        }
                    }
                }
            }
        }
        return oldConn!=connections;
    }


    @Override
    public void onNeighborBlockChange(BlockPos otherPos)
    {
        super.onNeighborBlockChange(otherPos);
        Direction dir = Direction.getNearest(otherPos.getX()-worldPosition.getX(),
                otherPos.getY()-worldPosition.getY(), otherPos.getZ()-worldPosition.getZ());
        ImmersiveMetallurgy.LOGGER.info("{} onNeighborBlockChange, dir: {}", getBlockPos(), dir);
        if(updateConnectionByte(dir))
        {
            updateTerminal(true);
            Level world = getLevelNonnull();
            world.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    private void updateTerminal(boolean setSide){
        if(subDir == null){
            int mainConnectionCount = 0;
            for(Direction attachDir : connectionAndAttachment.values()){
                if(attachDir == mainDir) mainConnectionCount++;
            }
            mainTerminal = mainConnectionCount <= 1;
            if(setSide) setSideWithoutUpdate(mainDir, mainTerminal, true);
        }
        else{
            mainTerminal = !connectionAndAttachment.values().contains(mainDir);
            subTerminal = !connectionAndAttachment.values().contains(subDir);
            if(setSide) setSideWithoutUpdate(mainDir, mainTerminal, true);
            if(setSide) setSideWithoutUpdate(subDir, subTerminal, true);
        }
    }


    public Direction getMainDir(){
        return mainDir;
    }

    public Direction getSubDir(){
        return subDir;
    }

    public boolean isMainTerminal(){
        updateTerminal(false);
        return mainTerminal;
    }

    public boolean isSubTerminal(){
        updateTerminal(false);
        return subTerminal;
    }



    public boolean isDirectionTerminal(Direction direction){
        if(direction == mainDir && isMainTerminal()) return true;
        if(direction == subDir && isSubTerminal()) return true;
        return false;
    }

    public List<Direction> getTerminalDirections(){
        List<Direction> directions = new ArrayList<>();
        if(isDirectionTerminal(mainDir)) directions.add(mainDir);
        if(isDirectionTerminal(subDir)) directions.add(subDir);
        return directions;
    }

    public Map<Direction, Direction> getConnectionAndAttachment() {
        return connectionAndAttachment;
    }

    @Override
    public void onBEPlaced(BlockPlaceContext ctx) {
        if (getLevel() != null && getLevel().isClientSide) return;
        mainDir = getFacing();
        mainTerminal = true;
        setSide(mainDir.getOpposite(), false);
        ImmersiveMetallurgy.LOGGER.info("mainAttachment: {}", mainDir);
        for(Direction d : DirectionUtils.VALUES){
            updateConnectionByte(d);
        }
        //markContainingBlockForUpdate(null);
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

    @Override
    public List<ConnectionInfo> getConnectionInfoList() {
        return connectionInfoList;
    }

    @Override
    public byte getConnectionByte() {
        return connections;
    }

    @Override
    public BlockEntity getBE() {
        return this;
    }

    @Override
    public IEnergyStorage getNeighborHandler(Direction direction) {
        return neighbors.get(direction).getCapability();
    }

    @Override
    public IElectricCableConnectionBE getNeighborIElectricCable(Direction direction){
        if(direction==null) return null;
        BlockEntity con = null;
        if (level != null) {
            con = SafeChunkUtils.getSafeBE(level, getBlockPos().relative(direction));
        }
        if (con instanceof IElectricCableConnectionBE up) {
            return up;
        }
        return null;
    }





    public static void registerCapabilities(BlockCapabilityRegistration.BECapabilityRegistrar<ElectricCableBlockEntity> registrar)
    {
        registrar.register(Capabilities.EnergyStorage.BLOCK, (be, side) -> {
                if (side != null && be.sideConfig.getBoolean(side))
                    return be.sidedHandlers.get(side);
                else
                    return null;
        });
    }

    public ConnectionStyle getConnectionStyle(Direction face){
        if((connections&(1<<face.get3DDataValue()))==0)
            return ConnectionStyle.NO_CONNECTION;
        return ConnectionStyle.COMMON;
    }


    public enum ConnectionStyle{
        NO_CONNECTION,
        COMMON
    }
}
