package net.bauxite_ltk.immersive_metallurgy.block.wood;

import net.bauxite_ltk.immersive_metallurgy.block.IMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public class FlameableRotatedPillarBlock extends RotatedPillarBlock {

    boolean isProducing = false;

    public FlameableRotatedPillarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        if(context.getItemInHand().getItem() instanceof AxeItem){
            if(state.is(IMBlocks.MASON_PINE_LOG)){
                return IMBlocks.STRIPPED_MASON_PINE_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(IMBlocks.MASON_PINE_WOOD)){
                return IMBlocks.STRIPPED_MASON_PINE_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(IMBlocks.MASON_PINE_LOG_LIVE)){
                if(state.getValue(AXIS).isHorizontal()){
                    return IMBlocks.STRIPPED_MASON_PINE_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
                }
                BlockPos thisPos = context.getClickedPos();
                for(int i = 0; i < 16; i++){
                    if(context.getLevel().getBlockState(thisPos.above(i)).is(IMBlocks.MASON_PINE_LOG_SAPPY)
                        || context.getLevel().getBlockState(thisPos.below(i)).is(IMBlocks.MASON_PINE_LOG_SAPPY)){
                        return IMBlocks.STRIPPED_MASON_PINE_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
                    }
                }
                return IMBlocks.MASON_PINE_LOG_SAPPY.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
        }

        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }
}
