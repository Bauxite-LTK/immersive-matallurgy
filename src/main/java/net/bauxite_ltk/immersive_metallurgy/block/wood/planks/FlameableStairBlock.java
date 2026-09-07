package net.bauxite_ltk.immersive_metallurgy.block.wood.planks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FlameableStairBlock extends StairBlock {
    final int flammability;
    final int fireSpreadSpeed;

    public FlameableStairBlock(BlockState baseState,Properties properties, int flammability, int fireSpreadSpeed) {
        super(baseState, properties);
        this.flammability = flammability;
        this.fireSpreadSpeed = fireSpreadSpeed;
    }


    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return flammability;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return fireSpreadSpeed;
    }
}
