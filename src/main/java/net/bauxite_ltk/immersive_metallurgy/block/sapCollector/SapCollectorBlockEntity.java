package net.bauxite_ltk.immersive_metallurgy.block.sapCollector;

import blusunrize.immersiveengineering.client.utils.TextUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.util.Utils;
import net.bauxite_ltk.immersive_metallurgy.block.BlockCapabilityRegistration;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlockEntities;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlocks;
import net.bauxite_ltk.immersive_metallurgy.fluid.IMFluids;
import net.bauxite_ltk.immersive_metallurgy.particle.IMParticleTypes;
import net.bauxite_ltk.immersive_metallurgy.tags.IMTags;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SapCollectorBlockEntity extends BlockEntity implements IEBlockInterfaces.IBlockOverlayText {

    public FluidTank tank = new FluidTank(4000);
    public static final int OUTPUT_LIMIT = 100;

    SapCollectorFluidHandler thisHandler = new SapCollectorFluidHandler(this);

    public SapCollectorBlockEntity(BlockPos pos, BlockState blockState) {
        super(IMBlockEntities.SAP_COLLECTOR.get(), pos, blockState);
    }

    static final int TIME_TO_PRODUCE = 20;
    int tickCount = 0;

    public void serverTick(){
        boolean update = false;
        if(tickCount < TIME_TO_PRODUCE) tickCount++;
        else{
            tickCount = 0;
            produceSap();
            update = true;
        }

        if (level != null) {
            IFluidHandler handlerBelow = level.getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos().relative(Direction.DOWN), Direction.UP);
            if(handlerBelow!=null && handlerBelow.getTanks()>0){
                int out = Math.min(OUTPUT_LIMIT, tank.getFluidAmount());
                int canOut = handlerBelow.fill(tank.getFluid().copyWithAmount(out), IFluidHandler.FluidAction.SIMULATE);
                FluidStack drained = tank.drain(canOut, IFluidHandler.FluidAction.EXECUTE);
                if(!drained.isEmpty())
                {
                    //IMUtils.LOGGER.info("drained:{}", drained);
                    int acFill = handlerBelow.fill(drained.copy(), IFluidHandler.FluidAction.EXECUTE);
                    //IMUtils.LOGGER.info("acFilled:{}", acFill);
                    update = true;
                }
            }
        }
        if(update)
        {
            //this.setChanged();
            this.syncToClient();
        }

    }

    public void clientTick(){
    }


    public void readCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider){
        tank.readFromNBT(provider, nbt.getCompound("tank"));
        //ImmersiveMetallurgy.LOGGER.info("load solid:{}", solid);
        this.setChanged();
    }

    public void writeCustomNBT(CompoundTag nbt, boolean descPacket, HolderLookup.Provider provider){
        nbt.put("tank", tank.writeToNBT(provider, new CompoundTag()));
    }

    private void produceSap(){
        Direction attachment = getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite();
        if (level != null && level.getBlockState(getBlockPos().relative(attachment)).is(IMBlocks.MASON_PINE_LOG_SAPPY.get())) {

            tank.fill(new FluidStack(IMFluids.MASON_PINE_SAP.source(), 1), IFluidHandler.FluidAction.EXECUTE);
            //IMUtils.LOGGER.info("fill Sap, sap:{}", tank.getFluid());


            ((ServerLevel)level).sendParticles(IMParticleTypes.DRIPPING_SAP.get(),
                      worldPosition.getX()+0.5+attachment.getStepX()*1d/16,
                    worldPosition.getY()+6/16d,
                    worldPosition.getZ()+0.5+attachment.getStepZ()*1d/16,
            1,0,0,0,1);
            //setChanged();
        }
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


    public void syncToClient() {
        if (level != null && !level.isClientSide) {
            setChanged();
            invalidateCapabilities();
            BlockState state = getBlockState();
            //ImmersiveMetallurgy.LOGGER.info("state:{}", state);
            level.sendBlockUpdated(getBlockPos(), state, state, 3);
        }
    }

    public static void registerCapabilities(BlockCapabilityRegistration.BECapabilityRegistrar<SapCollectorBlockEntity> registrar){
        registrar.register(Capabilities.FluidHandler.BLOCK,SapCollectorBlockEntity::getCapabilities);
    }

    public static IFluidHandler getCapabilities(SapCollectorBlockEntity be, Direction side){
        if(side == Direction.DOWN) return be.thisHandler;
        else return null;
    }

    @Override
    public @Nullable Component[] getOverlayText(@Nullable BlockState blockState, Player player, HitResult rtr, boolean hammer) {
        if(rtr.getType()== HitResult.Type.MISS)
            return null;
        if(Utils.isFluidRelatedItemStack(player.getItemInHand(InteractionHand.MAIN_HAND)) ||
                player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.GLASS_BOTTLE)){
            return new Component[]{
                    TextUtils.formatFluidStack(tank.getFluid())
            };
        }

        return null;
    }

    private static class SapCollectorFluidHandler implements IFluidHandler {
        SapCollectorBlockEntity tile;

        public SapCollectorFluidHandler(SapCollectorBlockEntity be){
            this.tile = be;
        }


        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int i) {
            return tile.tank.getFluidInTank(0);
        }

        @Override
        public int getTankCapacity(int i) {
            return tile.tank.getTankCapacity(0);
        }

        @Override
        public boolean isFluidValid(int i, FluidStack fluidStack) {
            return fluidStack.is(IMTags.Fluids.SAP_FLUID);
        }

        @Override
        public int fill(FluidStack fluidStack, FluidAction fluidAction) {
            return 0;
        }

        @Override
        public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
            return this.drain(fluidStack.getAmount(),fluidAction);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction fluidAction) {
            FluidStack fluidStack = tile.tank.drain(maxDrain, fluidAction);
            tile.setChanged();

            return fluidStack;
        }
    }

}
