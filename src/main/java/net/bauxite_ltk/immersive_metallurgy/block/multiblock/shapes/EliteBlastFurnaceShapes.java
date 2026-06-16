package net.bauxite_ltk.immersive_metallurgy.block.multiblock.shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class EliteBlastFurnaceShapes implements Function<BlockPos, VoxelShape> {

    public static final Function<BlockPos, VoxelShape> SHAPE_GETTER = new EliteBlastFurnaceShapes();

    private EliteBlastFurnaceShapes()
    {
    }

    @Override
    public VoxelShape apply(BlockPos posInMultiBlock) {
        if(new BoundingBox(0,0,0,4,0,1).isInside(posInMultiBlock))
            return Shapes.block();
        if(new BoundingBox(1,0,2,3,0,2).isInside(posInMultiBlock))
            return Shapes.block();
        if(posInMultiBlock.equals(new BlockPos(0,0,2)) || posInMultiBlock.equals(new BlockPos(4,0,2)))
            return Shapes.box(0,0,0,1,0.5,1);
        if(posInMultiBlock.equals(new BlockPos(0,0,3)) || posInMultiBlock.equals(new BlockPos(3,0,3)))
            return Shapes.or(
                    Shapes.box(0,0,0,1,0.5,1),
                    Shapes.box(0.5,0.5,0,1,1,1)
            );
        if(posInMultiBlock.equals(new BlockPos(1,0,3)) || posInMultiBlock.equals(new BlockPos(4,0,3)))
            return Shapes.or(
                    Shapes.box(0,0,0,1,0.5,1),
                    Shapes.box(0,0.5,0,0.5,1,1)
            );
        if(posInMultiBlock.equals(new BlockPos(2,0,3)))
            return Shapes.block();


        if(new BoundingBox(1,1,0,3,1,3).isInside(posInMultiBlock))
            return Shapes.box(0.02,0.02,0.02,0.98,0.98,0.98);
        if(new BoundingBox(0,1,0,0,1,3).isInside(posInMultiBlock)){
            if(posInMultiBlock.getZ() == 1)
                return Shapes.box(0.02,0.02,0.02,0.98,0.98,0.98);
            if(posInMultiBlock.getZ() == 2)
                return Shapes.box(0.5,0,0,1,1,1);
            return Shapes.or(
                    Shapes.box(0.5,0,0,1,1,1),
                    Shapes.box(5f/16,0,5f/16,0.5,1,11f/16)
            );
        }
        if(new BoundingBox(4,1,0,4,1,3).isInside(posInMultiBlock)){
            if(posInMultiBlock.getZ() == 1)
                return Shapes.box(0.02,0.02,0.02,0.98,0.98,0.98);
            if(posInMultiBlock.getZ() == 2)
                return Shapes.box(0,0,0,0.5,1,1);
            return Shapes.or(
                    Shapes.box(0,0,0,0.5,1,1),
                    Shapes.box(0.5,0,5f/16,11f/16,1,11f/16)
            );
        }


        if(new BoundingBox(1,2,0,3,2,0).isInside(posInMultiBlock))
            return Shapes.box(0,0, 7f/16, 1,1,1);
        if(new BoundingBox(0,2,0,0,2,3).isInside(posInMultiBlock)){
            if(posInMultiBlock.getZ() == 0 || posInMultiBlock.getZ() == 3)
                return Shapes.or(
                        Shapes.box(0.5,0,0,1,0.5,1),
                        Shapes.box(0,0.5,0,1,1,1)
                );
            return Shapes.box(0,0.5,0,1,1,1);
        }
        if(new BoundingBox(4,2,0,4,2,3).isInside(posInMultiBlock)){
            if(posInMultiBlock.getZ() == 0 || posInMultiBlock.getZ() == 3)
                return Shapes.or(
                        Shapes.box(0,0,0,0.5,0.5,1),
                        Shapes.box(0,0.5,0,1,1,1)
                );
            return Shapes.box(0,0.5,0,1,1,1);
        }
        if(new BoundingBox(1,2,1,3,2,2).isInside(posInMultiBlock))
            return Shapes.box(0.02,0.02,0.02,0.98,0.98,0.98);
        if(new BoundingBox(1,2,3,3,2,3).isInside(posInMultiBlock)){
            if(posInMultiBlock.getX()==2)
                return Shapes.block();
            return Shapes.box(0,0,0,1,1,7d/16);
        }



        if(posInMultiBlock.equals(new BlockPos(0,3,0)) || posInMultiBlock.equals(new BlockPos(0,3,3)))
            return Shapes.or(
                    Shapes.box(0.5,0,0,1,1,1),
                    Shapes.box(5f/16,0,5f/16,0.5,1,11f/16)
            );
        if(posInMultiBlock.equals(new BlockPos(4,3,0)) || posInMultiBlock.equals(new BlockPos(4,3,3)))
            return Shapes.or(
                    Shapes.box(0,0,0,0.5,1,1),
                    Shapes.box(0.5,0,5f/16,11f/16,1,11f/16)
            );
        if(new BoundingBox(1,3,0,3,3,0).isInside(posInMultiBlock))
            return Shapes.box(0,0, 7f/16, 1,1,1);
        if(new BoundingBox(1,3,1,3,3,2).isInside(posInMultiBlock))
            return Shapes.box(0.02,0.02,0.02,0.98,0.98,0.98);



        if(posInMultiBlock.equals(new BlockPos(0,4,0)) || posInMultiBlock.equals(new BlockPos(0,4,3)))
            return Shapes.or(
                    Shapes.box(0.5,0,0,1,1,1),
                    Shapes.box(5f/16,0,5f/16,0.5,1,11f/16)
            );
        if(posInMultiBlock.equals(new BlockPos(4,4,0)) || posInMultiBlock.equals(new BlockPos(4,4,3)))
            return Shapes.or(
                    Shapes.box(0,0,0,0.5,1,1),
                    Shapes.box(0.5,0,5f/16,11f/16,1,11f/16)
            );
        if(new BoundingBox(1,4,1,3,4,2).isInside(posInMultiBlock))
            return Shapes.box(0.02,0.02,0.02,0.98,0.98,0.98);
        if(new BoundingBox(1,4,0,3,4,0).isInside(posInMultiBlock))
            return Shapes.box(0,0, 7f/16, 1,1,1);



        if(new BoundingBox(0,5,0,0,5,3).isInside(posInMultiBlock)){
            if(posInMultiBlock.getZ() == 0 || posInMultiBlock.getZ() == 3)
                return Shapes.or(
                        Shapes.box(0.5,0,0,1,0.5,1),
                        Shapes.box(0,0.5,0,1,1,1)
                );
            return Shapes.box(0,0.5,0,1,1,1);
        }
        if(new BoundingBox(4,5,0,4,5,3).isInside(posInMultiBlock)){
            if(posInMultiBlock.getZ() == 0 || posInMultiBlock.getZ() == 3)
                return Shapes.or(
                        Shapes.box(0,0,0,0.5,0.5,1),
                        Shapes.box(0,0.5,0,1,1,1)
                );
            return Shapes.box(0,0.5,0,1,1,1);
        }
        if(new BoundingBox(1,5,0,3,5,0).isInside(posInMultiBlock)){
            if(posInMultiBlock.getX()==2)
                return Shapes.or(
                        Shapes.box(0,0,0.5,1,0.5,1),
                        Shapes.box(0,0.5,0,1,1,1)
                );
            return Shapes.empty();
        }
        if(new BoundingBox(1,5,1,3,5,2).isInside(posInMultiBlock))
            return Shapes.box(0.02,0.02,0.02,0.98,0.98,0.98);
        if(new BoundingBox(1,5,3,3,5,3).isInside(posInMultiBlock)){
            if(posInMultiBlock.getX()==2)
                return Shapes.or(
                        Shapes.box(0,0,0,1,0.5,0.5),
                        Shapes.box(0,0.5,0,1,1,1)
                );
            return Shapes.empty();
        }

        if(posInMultiBlock.equals(new BlockPos(1,6,0)))
            return Shapes.or(
                    Shapes.box(0.5,0.02,0,1,0.98,0.5),
                    Shapes.box(0,0.02,0.5,1,0.98,1)
            );
        if(posInMultiBlock.equals(new BlockPos(3,6,0)))
            return Shapes.or(
                    Shapes.box(0,0.02,0,0.5,0.98,0.5),
                    Shapes.box(0,0.02,0.5,1,0.98,1)
            );
        if(posInMultiBlock.equals(new BlockPos(1,6,2)))
            return Shapes.or(
                    Shapes.box(0,0.02,0,1,0.98,0.5),
                    Shapes.box(0.5,0.02,0.5,1,0.98,1)
            );
        if(posInMultiBlock.equals(new BlockPos(3,6,2)))
            return Shapes.or(
                    Shapes.box(0,0,0,1,1,0.5),
                    Shapes.box(0,0,0.5,0.5,1,1)
            );
        if(new BoundingBox(2,6,0,2,6,3).isInside(posInMultiBlock)){
            return Shapes.box(0.02,0.02,0.02,0.98,0.98,0.98);
        }
        if(posInMultiBlock.equals(new BlockPos(1,6,1)))
            return Shapes.box(0.02,0.02,0.02,0.98,0.98,0.98);
        if(posInMultiBlock.equals(new BlockPos(3,6,1)))
            return Shapes.box(0.02,0.02,0.02,0.98,0.98,0.98);


        return Shapes.empty();
    }
}
