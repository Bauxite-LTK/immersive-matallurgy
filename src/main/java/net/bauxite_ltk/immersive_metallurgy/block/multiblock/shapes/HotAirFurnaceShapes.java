package net.bauxite_ltk.immersive_metallurgy.block.multiblock.shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class HotAirFurnaceShapes implements Function<BlockPos, VoxelShape> {
    public static final Function<BlockPos, VoxelShape> SHAPE_GETTER = new HotAirFurnaceShapes();

    @Override
    public VoxelShape apply(BlockPos posInMultiBlock) {
        return Shapes.box(0,0,0,1,0.5,1);
    }
}
