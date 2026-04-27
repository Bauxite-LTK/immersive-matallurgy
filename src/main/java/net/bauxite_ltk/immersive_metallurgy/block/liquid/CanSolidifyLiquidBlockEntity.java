package net.bauxite_ltk.immersive_metallurgy.block.liquid;

import blusunrize.immersiveengineering.api.utils.SafeChunkUtils;
import net.bauxite_ltk.immersive_metallurgy.ImmersiveMetallurgy;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


public class CanSolidifyLiquidBlockEntity extends BlockEntity{

    int solidifyTicks = -1;
    int tickRemain = -1;
    Block solid = null;


    public CanSolidifyLiquidBlockEntity(BlockPos pos, BlockState blockState) {
        super(IMBlockEntities.CAN_SOLIDIFY_LIQUID.get(), pos, blockState);
    }

    public void setSolidProperties(Block solid, int solidifyTicks){
        this.solid = solid;
        this.solidifyTicks = solidifyTicks;
        this.tickRemain = solidifyTicks;
        syncToClient();
    }

    public void serverTick(){
        if(tickRemain > 0){
            tickRemain --;
            if(tickRemain % 4 == 0){
                //ImmersiveMetallurgy.LOGGER.info("tick ticksRemain:{}", tickRemain);
                syncToClient();
            }
            return;
        }
        if (level != null) {
            if(level.getFluidState(getBlockPos()).isSource()){
                BlockState newState = solid.defaultBlockState();
                level.setBlockAndUpdate(worldPosition, newState);
            }
            else{
                level.setBlockAndUpdate(getBlockPos(), Blocks.AIR.defaultBlockState());
            }
        }
    }

    public void clientTick(){
        if(tickRemain % 4 == 0){
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.levelRenderer.setBlocksDirty(
                    getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(),
                    getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()
            );
        }
    }

    public void readCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider){
        this.tickRemain = nbt.getInt("ticksRemain");
        //ImmersiveMetallurgy.LOGGER.info("load ticksRemain:{}", tickRemain);
        this.solid = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(nbt.getString("solidBlock")));
        //ImmersiveMetallurgy.LOGGER.info("load solid:{}", solid);
        this.setChanged();
    }

    public void writeCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider){
        nbt.putInt("ticksRemain", tickRemain);
        nbt.putString("solidBlock", BuiltInRegistries.BLOCK.getKey(solid).toString());
    }


    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this, (be, access) -> {
            CompoundTag nbtTagCompound = new CompoundTag();
            this.writeCustomNBT(nbtTagCompound, true, access);
            return nbtTagCompound;
        });
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider)
    {
        this.readCustomNBT(pkt.getTag(), true, provider);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider)
    {
        this.readCustomNBT(tag, true, provider);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider)
    {
        CompoundTag nbt = super.getUpdateTag(provider);
        writeCustomNBT(nbt, true, provider);
        return nbt;
    }


    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        writeCustomNBT(tag, false, registries);
    }


    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        readCustomNBT(tag, false, registries);
    }


    private void syncToClient() {
        if (level != null && !level.isClientSide) {
            setChanged();
            BlockState state = getBlockState();
            //ImmersiveMetallurgy.LOGGER.info("state:{}", state);
            level.sendBlockUpdated(getBlockPos(), state, state, 3);




        }
    }

    public static int getColorFromTickRemain(BlockAndTintGetter blockAndTintGetter, BlockPos pos){

        BlockEntity be = blockAndTintGetter.getBlockEntity(pos);
        if(be instanceof CanSolidifyLiquidBlockEntity solidifyLiquidBE){
            int tickRemain = solidifyLiquidBE.tickRemain;
            int tickTotal = solidifyLiquidBE.solidifyTicks;
            double percent = 0.5 + 0.5*((double)tickRemain/tickTotal);
            ImmersiveMetallurgy.LOGGER.info("getColorFromTickRemain: {}", 0x00ff0080 + Mth.clamp((int)(0xff00 * percent), 0, 0xff00));
            return 0x00ff0080 + Math.clamp((int)(0x0000ff00 * percent) & 0x0000ff00 , 0x00008000, 0x0000ff00);
        }

        return 0;
    }

}
