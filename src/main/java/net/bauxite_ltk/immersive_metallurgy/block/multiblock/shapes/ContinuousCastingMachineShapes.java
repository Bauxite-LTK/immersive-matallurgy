package net.bauxite_ltk.immersive_metallurgy.block.multiblock.shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class ContinuousCastingMachineShapes implements Function<BlockPos, VoxelShape>{
    public static final Function<BlockPos, VoxelShape> SHAPE_GETTER = new ContinuousCastingMachineShapes();


    @Override
    public VoxelShape apply(BlockPos posInMultiBlock) {
        if(new BoundingBox(0,0,0,2,0,6).isInside(posInMultiBlock)){
            return Shapes.block();
        }
        if(new BoundingBox(0,1,0,2,1,1).isInside(posInMultiBlock)){
            return Shapes.block();
        }
        if(new BoundingBox(0,1,2,2,1,2).isInside(posInMultiBlock)){
            return Shapes.box(0,0,0,1,0.5,1);
        }

        if(new BlockPos(0,1,3).equals(posInMultiBlock)){
            return Shapes.box(2f/16,0,0.5,1,1,1);
        }
        if(new BlockPos(1,1,3).equals(posInMultiBlock)){
            return Shapes.box(0,0,0.5,1,1,1);
        }
        if(new BlockPos(2,1,3).equals(posInMultiBlock)){
            return Shapes.box(0,0,0.5,14f/16,1,1);
        }

        if(new BlockPos(0,1,4).equals(posInMultiBlock)){
            return Shapes.box(0.5,0,0,1,1,1);
        }
        if(new BlockPos(1,1,4).equals(posInMultiBlock)){
            return Shapes.block();
        }
        if(new BlockPos(2,1,4).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,0.5,1,1);
        }

        if(new BlockPos(0,1,5).equals(posInMultiBlock)){
            return Shapes.box(0.5,0,0,1,0.5,1);
        }
        if(new BlockPos(1,1,5).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,1,0.5,1);
        }
        if(new BlockPos(2,1,5).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,0.5,0.5,1);
        }

        if(new BlockPos(0,1,6).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(2f/16,0.5,0,1,1,1),
                    Shapes.box(0,0,0,1,0.5,1)
            );

        }
        if(new BlockPos(1,1,6).equals(posInMultiBlock)){
            return Shapes.block();
        }
        if(new BlockPos(2,1,6).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0.5,0,14f/16,1,1),
                    Shapes.box(0,0,0,1,0.5,1)
            );
        }

        if(new BoundingBox(0,2,0,2,2,0).isInside(posInMultiBlock)){
            return Shapes.block();
        }

        if(new BlockPos(0,2,1).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,0.5,0.5,0.5),
                    Shapes.box(0.5,0,0,1,1,1)
            );
        }
        if(new BlockPos(1,2,1).equals(posInMultiBlock)){
            return Shapes.block();
        }
        if(new BlockPos(2,2,1).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,0.5,1,1),
                    Shapes.box(0.5,0,0,1,0.5,0.5)
            );
        }

        if(new BoundingBox(0,2,0,2,2,2).isInside(posInMultiBlock)){
            return Shapes.empty();
        }

        if(new BlockPos(0,2,3).equals(posInMultiBlock)){
            return Shapes.box(2f/16,0,0.5,1,12f/16,1);
        }
        if(new BlockPos(1,2,3).equals(posInMultiBlock)){
            return Shapes.box(0,0,0.5,1,12f/16,1);
        }
        if(new BlockPos(2,2,3).equals(posInMultiBlock)){
            return Shapes.box(0,0,0.5,14f/16,12f/16,1);
        }

        if(new BlockPos(0,2,4).equals(posInMultiBlock)){
            return Shapes.box(0.5,0,0,1,0.5,0.5);
        }
        if(new BlockPos(1,2,4).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,1,0.5,0.5);
        }
        if(new BlockPos(2,2,4).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,0.5,0.5,0.5);
        }

        if(new BlockPos(0,2,6).equals(posInMultiBlock)){
            return Shapes.box(2f/16,0,0,10f/16,1,1);
        }
        if(new BlockPos(2,2,6).equals(posInMultiBlock)){
            return Shapes.box(6f/16,0,0,14f/16,1,1);
        }



        if(new BoundingBox(0,3,0,2,3,0).isInside(posInMultiBlock)){
            return Shapes.block();
        }

        if(new BlockPos(0,3,1).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0.5,0,0,1,1,1),
                    Shapes.box(4f/16,13f/16,0,8f/16,1,1)
            );
        }
        if(new BlockPos(1,3,1).equals(posInMultiBlock)){
            return Shapes.block();
        }
        if(new BlockPos(2,3,1).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,0.5,1,1),
                    Shapes.box(8f/16,13f/16,0,12f/16,1,1)
            );
        }

        if(new BoundingBox(0,3,2,0,3,5).isInside(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(6f/16,8f/16,10f/16,1,13f/16,14f/16),
                    Shapes.box(11f/16,3f/16,10f/16,1,8f/16,14f/16),
                    Shapes.box(4f/16,13f/16,0,8f/16,1,1)
            );
        }
        if(new BoundingBox(1,3,2,1,3,5).isInside(posInMultiBlock)){
            return Shapes.box(0,3f/16,0,1,9f/16,1);
        }
        if(new BoundingBox(2,3,2,2,3,5).isInside(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,8f/16,10f/16,10f/16,13f/16,14f/16),
                    Shapes.box(0,3f/16,10f/16,5f/16,13f/16,14f/16),
                    Shapes.box(8f/16,13f/16,0,12f/16,1,1)
            );
        }

        if(new BlockPos(0,3,6).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(2f/16,0,0,10f/16,1,1),
                    Shapes.box(8f/16,0,10f/16,1,4f/16,14f/16),
                    Shapes.box(8f/16,12f/16,10f/16,1,1,14f/16)
            );
        }
        if(new BlockPos(1,3,6).equals(posInMultiBlock)){
            return Shapes.box(0,0,0.5,1,1,1);
        }
        if(new BlockPos(2,3,6).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(6f/16,0,0,14f/16,1,1),
                    Shapes.box(0,0,10f/16,8f/16,4f/16,14f/16),
                    Shapes.box(0,12f/16,10f/16,8f/16,1,14f/16)
            );
        }


        if(new BlockPos(0,4,0).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,0.5,1,1),
                    Shapes.box(0.5,0,0,1,3f/16,1),
                    Shapes.box(0.5,3f/16,0,1,1,3f/16),
                    Shapes.box(0.5,3f/16,13f/16,1,1,1)
            );
        }
        if(new BlockPos(1,4,0).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,1,3f/16,1),
                    Shapes.box(0,3f/16,0,1,1,3f/16),
                    Shapes.box(0,3f/16,13f/16,1,1,1)
            );
        }
        if(new BlockPos(2,4,0).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0.5,0,0,1,1,1),
                    Shapes.box(0,0,0,0.5,3f/16,1),
                    Shapes.box(0,3f/16,0,0.5,1,3f/16),
                    Shapes.box(0,3f/16,13f/16,0.5,1,1)
            );
        }


        if(new BoundingBox(0,4,1,0,4,5).isInside(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(4f/16,0,0,8f/16,1f/16,1),
                    Shapes.box(0,0.5,0,1,1,1)
            );
        }
        if(new BoundingBox(1,4,1,1,4,5).isInside(posInMultiBlock)){
            return Shapes.box(0,0.5,0,1,1,1);
        }
        if(new BoundingBox(2,4,1,2,4,5).isInside(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(8f/16,0,0,12f/16,1f/16,1),
                    Shapes.box(0,0.5,0,1,1,1)
            );
        }

        if(new BlockPos(0,4,6).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0.5,0,1,1,1),
                    Shapes.box(2f/16,0,0,10f/16,0.5,1)
            );
        }
        if(new BlockPos(1,4,6).equals(posInMultiBlock)){
            return Shapes.block();
        }
        if(new BlockPos(2,4,6).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0.5,0,1,1,1),
                    Shapes.box(6f/16,0,0,14f/16,0.5,1)
            );
        }


        return Shapes.box(0,0,0,1,0.5,1);
    }
}
