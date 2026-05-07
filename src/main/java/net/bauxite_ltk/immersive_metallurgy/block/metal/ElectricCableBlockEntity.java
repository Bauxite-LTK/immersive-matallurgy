package net.bauxite_ltk.immersive_metallurgy.block.metal;

import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.utils.DirectionUtils;
import blusunrize.immersiveengineering.api.utils.SafeChunkUtils;
import blusunrize.immersiveengineering.api.utils.shapes.CachedVoxelShapes;
import blusunrize.immersiveengineering.common.blocks.BlockCapabilityRegistration;
import blusunrize.immersiveengineering.common.blocks.IEBaseBlockEntity;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.PlacementLimitation;
import blusunrize.immersiveengineering.common.blocks.metal.FluidPipeBlockEntity;
import blusunrize.immersiveengineering.common.blocks.ticking.IEServerTickableBE;
import blusunrize.immersiveengineering.common.register.IEItems;
import blusunrize.immersiveengineering.common.util.IEBlockCapabilityCaches;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.bauxite_ltk.immersive_metallurgy.ImmersiveMetallurgy;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class ElectricCableBlockEntity extends IEBaseBlockEntity implements IElectricCableConnectionBE ,IEServerTickableBE,
        IEBlockInterfaces.IStateBasedDirectional, IEBlockInterfaces.IPlacementInteraction,
        IEBlockInterfaces.ICollisionBounds, IEBlockInterfaces.ISelectionBounds {


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

        boolean wasConnected = (connections & (byte) mask)!=0;
        ImmersiveMetallurgy.LOGGER.info("Pos:{}", getBlockPos());
        ImmersiveMetallurgy.LOGGER.info("connections:{}", connections);
        ImmersiveMetallurgy.LOGGER.info("mask:{}", mask);
        ImmersiveMetallurgy.LOGGER.info("wasConnected:{}", (connections & (byte) mask));

        if(wasConnected){
            ImmersiveMetallurgy.LOGGER.info("execute wasConnected updateConnectionByte");
            boolean doRemove = false;
            IEnergyStorage energyStorage = neighbors.get(dir).getCapability();
            IElectricCableConnectionBE iElectricCable = getNeighborIElectricCable(dir);
            if(!sideConfig.getBoolean(dir)) doRemove = true;
            else if(energyStorage == null) doRemove = true;
            else if(iElectricCable == null && !isDirectionTerminal(dir)) doRemove = true;
            if(doRemove){
                connections &= (byte) ~mask;
                connectionAndAttachment.remove(dir);
            }
        }
        else if(getConnectionCount() < 2){
            ImmersiveMetallurgy.LOGGER.info("execute regular updateConnectionByte");
            IEnergyStorage energyStorage = neighbors.get(dir).getCapability();
            IElectricCableConnectionBE be = getNeighborIElectricCable(dir);
            if(energyStorage!=null && sideConfig.getBoolean(dir)){
                ImmersiveMetallurgy.LOGGER.info("regular 1");
                if(be == null && isDirectionTerminal(dir)){
                    connections |= (byte) mask;
                }
                else if(be instanceof ElectricCableBlockEntity electricCable){
                    ImmersiveMetallurgy.LOGGER.info("regular 2");
                    byte neighborConnections = electricCable.getConnectionByte();
                    ImmersiveMetallurgy.LOGGER.info("regular 3 {}", electricCable.getConnectionCount() < 2);
                    ImmersiveMetallurgy.LOGGER.info("regular 4 {}", ((neighborConnections >> dir.getOpposite().get3DDataValue()) & 1 ) != 0);
                    if(electricCable.getConnectionCount() < 2
                             || ((neighborConnections >> dir.getOpposite().get3DDataValue()) & 1 ) != 0)
                    {
                        for (Direction attachDir : getAttachDirections()) {
                            if (electricCable.getAttachDirections().contains(attachDir)) {
                                connections |= (byte) mask;
                                connectionAndAttachment.put(dir, attachDir);
                                ImmersiveMetallurgy.LOGGER.info("connected neighbor cable");
                            }
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
        //ImmersiveMetallurgy.LOGGER.info("{} onNeighborBlockChange, dir: {}", getBlockPos(), dir);
        if(updateConnectionByte(dir))
        {
            updateTerminal(true);
            updateAllRootNode();
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
            if(setSide){
                setSideWithoutUpdate(mainDir, mainTerminal, true);
                setSideWithoutUpdate(mainDir.getOpposite(), false, true);
            }
        }
        else{
            mainTerminal = !connectionAndAttachment.values().contains(mainDir);
            subTerminal = !connectionAndAttachment.values().contains(subDir);
            if(setSide){
                setSideWithoutUpdate(mainDir, mainTerminal, true);
                setSideWithoutUpdate(mainDir.getOpposite(), false, true);
                setSideWithoutUpdate(subDir, subTerminal, true);
                setSideWithoutUpdate(subDir.getOpposite(), false, true);
            }
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

    public List<Direction> getAttachDirections(){
        List<Direction> directions = new ArrayList<>();
        if(mainDir != null) directions.add(mainDir);
        if(subDir != null) directions.add(subDir);
        return directions;
    }

    public List<Direction> getTerminalDirections(){
        List<Direction> directions = new ArrayList<>();
        if(isDirectionTerminal(mainDir)) directions.add(mainDir);
        if(isDirectionTerminal(subDir)) directions.add(subDir);
        return directions;
    }

    public int getConnectionCount(){
        int count = 0;
        for(int i = 0; i< 6; i++){
            if(((connections >> i) & 1) != 0) count++;
        }
        return count;
    }

    public Map<Direction, Direction> getConnectionAndAttachment() {
        return connectionAndAttachment;
    }

    @Override
    public void onBEPlaced(BlockPlaceContext ctx) {
        if (getLevel() != null && getLevel().isClientSide) return;
        mainDir = getFacing();
        mainTerminal = true;
        sideConfig.put(mainDir.getOpposite(), false);
        invalidateHandler(mainDir.getOpposite());
        level.blockEvent(getBlockPos(), getBlockState().getBlock(), 0, 0);
        //setSide(mainDir.getOpposite(), false);

        //ImmersiveMetallurgy.LOGGER.info("mainAttachment: {}", mainDir);
        for(Direction d : DirectionUtils.VALUES){
            updateConnectionByte(d);
        }
        updateTerminal(true);
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

    @Override
    public int getTransferLimit() {
        return transferLimit;
    }


    public byte getAvailableConnectionByte()
    {
        byte availableConnections = connections;
        int mask = 1;
        for(Direction dir : DirectionUtils.VALUES)
        {
            if((availableConnections&mask)==0)
            {
                if(level.getBlockEntity(getBlockPos().relative(dir)) instanceof ElectricCableBlockEntity)
                    availableConnections |= mask;
                else
                {
                    IEnergyStorage handler = neighbors.get(dir).getCapability();
                    if(handler!=null)
                        availableConnections |= mask;
                }
            }
            mask <<= 1;
        }
        return availableConnections;
    }


    private static final CachedVoxelShapes<BoundingBoxKey> SHAPES = new CachedVoxelShapes<>(ElectricCableBlockEntity::getBoxes);

    @Override
    public VoxelShape getCollisionShape(CollisionContext ctx)
    {
        return SHAPES.get(new BoundingBoxKey(this,false, mainDir, subDir, mainTerminal, subTerminal, connectionAndAttachment));
    }

    @Override
    public VoxelShape getSelectionShape(@Nullable CollisionContext ctx)
    {
        //TODO needs to be a more generic check!
        boolean hammer = ctx!=null&&ctx.isHoldingItem(IEItems.Tools.HAMMER.get());
        if(ctx instanceof EntityCollisionContext ecc){
            if(ecc.getEntity() instanceof Player player){
                hammer = player.getItemInHand(InteractionHand.MAIN_HAND).getTags()
                        .anyMatch(tagKey -> tagKey.equals(IETags.hammers));
            }
        }
        return SHAPES.get(new BoundingBoxKey( this, hammer, mainDir, subDir, mainTerminal, subTerminal, connectionAndAttachment));
    }


    private static List<AABB> getBoxes(BoundingBoxKey key)
    {
        List<AABB> list = Lists.newArrayList();
        byte availableConnections = key.availableConnections;
        byte activeConnections = key.connections;
        for(Direction d : DirectionUtils.VALUES)
        {
            int i = d.get3DDataValue();
            if(((availableConnections >> i) & 1)==1)
            {
                if(((activeConnections >> i) & 1)==1||key.showToolView)
                {
                    switch (d){
                        case Direction.UP:{
                            list.add(new AABB(3d/16, 0, 0, 13d/16,1,1));
                            break;
                        }
                        case Direction.NORTH:{
                            list.add(new AABB(3d/16, 0, 0, 13d/16, 8d/16,3d/16));
                            break;
                        }
                        case Direction.SOUTH:{
                            list.add(new AABB(3d/16, 0, 13d/16, 13d/16, 8d/16,1));
                            break;
                        }
                        case Direction.EAST:{
                            list.add(new AABB(13d/16, 0, 3d/16, 1, 8d/16,13d/16));
                            break;
                        }
                        case Direction.WEST:{
                            list.add(new AABB(0, 0, 3d/16, 3d/16, 8d/16,13d/16));
                            break;
                        }
                    }
//                    if(key.connectionStyles.get(d)== ConnectionStyle.TO_UP_NORTH)
//                        list.add(new AABB(
//                                i==4?0: i==5?0.875: 0.125, i==0?0: i==1?0.875: 0.125, i==2?0: i==3?0.875: 0.125,
//                                i==4?0.125: i==5?1: 0.875, i==0?0.125: i==1?1: 0.875, i==2?0.125: i==3?1: 0.875
//                        ));
                }
            }
        }
        list.add(new AABB(3d/16, 0, 3d/16, 13d/16, 8d/16, 13d/16));
        return list;
    }

    private static class BoundingBoxKey
    {
        private final boolean showToolView;
        private final byte connections;
        private final byte availableConnections;
        private final Map<Direction, ConnectionStyle> connectionStyles = new EnumMap<>(Direction.class);

        Direction mainDir;
        Direction subDir;
        boolean isMainTerminal;
        boolean isSubTerminal;
        Map<Direction, Direction> connectionAndAttachment;

        private BoundingBoxKey(ElectricCableBlockEntity te, boolean showToolView,
                               Direction mainDir, Direction subDir,
                               boolean isMainTerminal, boolean isSubTerminal,
                               Map<Direction, Direction> connectionAndAttachment)
        {
            this.showToolView = showToolView;
            this.connections = te.connections;
            this.availableConnections = te.getAvailableConnectionByte();
            for(Direction d : DirectionUtils.VALUES)
                connectionStyles.put(d, te.getConnectionStyle(d));
            this.mainDir = mainDir;
            this.subDir = subDir;
            this.isMainTerminal = isMainTerminal;
            this.isSubTerminal = isSubTerminal;
            this.connectionAndAttachment = connectionAndAttachment;
        }

        @Override
        public boolean equals(Object o)
        {
            if(this==o) return true;
            if(o==null||getClass()!=o.getClass()) return false;
            BoundingBoxKey that = (BoundingBoxKey)o;
            return showToolView==that.showToolView&&
                    connections==that.connections&&
                    availableConnections==that.availableConnections&&
                    connectionStyles.equals(that.connectionStyles);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(showToolView, connections, availableConnections, connectionStyles);
        }

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
