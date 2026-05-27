package net.bauxite_ltk.immersive_metallurgy.block.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class FlameableLeavesBlock extends LeavesBlock {
    final int flammability;
    final int fireSpreadSpeed;



    public FlameableLeavesBlock(Properties properties, int flammability, int fireSpreadSpeed) {
        super(properties);
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
