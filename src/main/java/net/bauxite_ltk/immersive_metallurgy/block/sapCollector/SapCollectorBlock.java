package net.bauxite_ltk.immersive_metallurgy.block.sapCollector;

import blusunrize.immersiveengineering.common.blocks.IEEntityBlock;
import com.mojang.serialization.MapCodec;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlockEntities;
import net.bauxite_ltk.immersive_metallurgy.block.liquid.CanSolidifyLiquidBlockEntity;
import net.bauxite_ltk.immersive_metallurgy.fluid.IMFluids;
import net.bauxite_ltk.immersive_metallurgy.item.IMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class SapCollectorBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final MapCodec<SapCollectorBlock> CODEC = simpleCodec(SapCollectorBlock::new);

    public SapCollectorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return IMBlockEntities.SAP_COLLECTOR.get().create(blockPos, blockState);
    }


    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        BlockEntityTicker<T> baseTicker = (level1, blockPos1, blockState1, blockEntity1) -> {

            //ImmersiveMetallurgy.LOGGER.info("Ticker Method Execute");
            if(blockEntity1 instanceof SapCollectorBlockEntity sapCollector){
                //ImmersiveMetallurgy.LOGGER.info("Ticker Method Server Tick Execute");
                if(!level1.isClientSide) sapCollector.serverTick();
                else sapCollector.clientTick();
            }
        };
        return baseTicker;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.or(
                Block.box(1,0,1,15,1,15),
                Block.box(1,1,1,15,8,3),
                Block.box(1,1,13,15,8,15),
                Block.box(1,1,3,3,8,13),
                Block.box(13,1,3,15,8,13)
        );

    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        //if(level.isClientSide) return ItemInteractionResult.SUCCESS;
        if(level.getBlockEntity(pos) instanceof SapCollectorBlockEntity sapCollectorBlockEntity){

            if(player.getItemInHand(hand).is(Items.GLASS_BOTTLE)){
                FluidStack fluidInTank = sapCollectorBlockEntity.tank.getFluidInTank(0);

                if(fluidInTank.getAmount()>=250){
                    if(fluidInTank.is(IMFluids.MASON_PINE_SAP.source())){
                        sapCollectorBlockEntity.tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
                        sapCollectorBlockEntity.syncToClient();
                        player.getItemInHand(hand).shrink(1);
                        player.addItem(IMItems.MASON_PINE_SAP_BOTTLE.toStack(1));
                        level.playLocalSound(pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1, 1,true);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
