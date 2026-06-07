package net.bauxite_ltk.immersive_metallurgy.block.multiblock.shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class EliteBlastFurnaceShapes implements Function<BlockPos, VoxelShape> {

    public static final Function<BlockPos, VoxelShape> SHAPE_GETTER = new EliteBlastFurnaceShapes();

    private EliteBlastFurnaceShapes()
    {
    }

    @Override
    public VoxelShape apply(BlockPos blockPos) {
        return Shapes.box(0,0,0,1,8d/16,1);
    }
}
