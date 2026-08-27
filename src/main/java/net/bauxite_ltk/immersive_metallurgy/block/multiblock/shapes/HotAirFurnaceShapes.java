package net.bauxite_ltk.immersive_metallurgy.block.multiblock.shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class HotAirFurnaceShapes implements Function<BlockPos, VoxelShape> {
    public static final Function<BlockPos, VoxelShape> SHAPE_GETTER = new HotAirFurnaceShapes();

    @Override
    public VoxelShape apply(BlockPos posInMultiBlock) {
        if(new BlockPos(0,0,0).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,1,0.5,1),
                    Shapes.box(5f/16,0.5,5f/16,1,11f/16,1),
                    Shapes.box(0.5,11f/16,0.5,1,1,1)
            );
        }

        if(new BlockPos(1,0,0).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,1,0.5,1),
                    Shapes.box(0,0.5,4f/16,1,1,1)
            );
        }

        if(new BlockPos(2,0,0).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,1,0.5,1),
                    Shapes.box(0,0.5,5f/16,11f/16,11f/16,1),
                    Shapes.box(0,11f/16,0.5,0.5,1,1)
            );
        }

        if(new BlockPos(0,0,1).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,1,0.5,1),
                    Shapes.box(4f/16,0.5,0,1,1,1)
            );
        }
        if(new BlockPos(1,0,1).equals(posInMultiBlock)){
            return Shapes.block();
        }
        if(new BlockPos(2,0,1).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,1,0.5,1),
                    Shapes.box(0,0.5,0,12f/16,1,1)
            );
        }

        if(new BlockPos(0,0,2).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,1,0.5,1),
                    Shapes.box(5f/16,0.5,0,1,11f/16,11f/16),
                    Shapes.box(0.5,11f/16,0,1,1,0.5)
            );
        }
        if(new BlockPos(1,0,2).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,1,0.5,1),
                    Shapes.box(0,0.5,0,1,1,12f/16)
            );
        }
        if(new BlockPos(2,0,2).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,1,0.5,1),
                    Shapes.box(0,0.5,0,11f/16,11f/16,11f/16),
                    Shapes.box(0,11f/16,0,0.5,1,0.5)
            );
        }

        if(new BlockPos(0,1,0).equals(posInMultiBlock)){
            return Shapes.box(0.5,0,0.5,1,1,1);
        }
        if(new BlockPos(1,1,0).equals(posInMultiBlock)){
            return Shapes.box(0,0,0.5,1,1,1);
        }
        if(new BlockPos(2,1,0).equals(posInMultiBlock)){
            return Shapes.box(0,0,0.5,0.5,1,1);
        }
        if(new BlockPos(0,1,1).equals(posInMultiBlock)){
            return Shapes.box(0.5,0,0,1,1,1);
        }
        if(new BlockPos(1,1,1).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,1,1,1);
        }
        if(new BlockPos(2,1,1).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,0.5,1,1);
        }
        if(new BlockPos(0,1,2).equals(posInMultiBlock)){
            return Shapes.box(0.5,0,0,1,1,0.5);
        }
        if(new BlockPos(1,1,2).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,1,1,0.5);
        }
        if(new BlockPos(2,1,2).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,0.5,1,0.5);
        }

        if(new BlockPos(0,2,0).equals(posInMultiBlock)){
            return Shapes.box(0.5,0,0.5,1,1,1);
        }
        if(new BlockPos(1,2,0).equals(posInMultiBlock)){
            return Shapes.box(0,0,0.5,1,1,1);
        }
        if(new BlockPos(2,2,0).equals(posInMultiBlock)){
            return Shapes.box(0,0,0.5,0.5,1,1);
        }
        if(new BlockPos(0,2,1).equals(posInMultiBlock)){
            return Shapes.box(0.5,0,0,1,1,1);
        }
        if(new BlockPos(1,2,1).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,1,1,1);
        }
        if(new BlockPos(2,2,1).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,0.5,1,1);
        }
        if(new BlockPos(0,2,2).equals(posInMultiBlock)){
            return Shapes.box(0.5,0,0,1,1,0.5);
        }
        if(new BlockPos(1,2,2).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,1,1,0.5);
        }
        if(new BlockPos(2,2,2).equals(posInMultiBlock)){
            return Shapes.box(0,0,0,0.5,1,0.5);
        }


        if(new BlockPos(0,3,0).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0.5,0,0.5,1,4f/16,1),
                    Shapes.box(6f/16,4f/16,6f/16,1,8f/16,1),
                    Shapes.box(3f/16,8f/16,3f/16,1,1,1)
            );
        }
        if(new BlockPos(1,3,0).equals(posInMultiBlock)){
            return Shapes.block();
        }
        if(new BlockPos(2,3,0).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0.5,0.5,4f/16,1),
                    Shapes.box(0,4f/16,6f/16,10f/16,8f/16,1),
                    Shapes.box(0,8f/16,3f/16,13f/16,1,1)
            );
        }
        if(new BlockPos(0,3,1).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0.5,0,0,1,0.5,1),
                    Shapes.box(0,0.5,0,1,1,1)
            );
        }
        if(new BlockPos(1,3,1).equals(posInMultiBlock)){
            return Shapes.block();
        }
        if(new BlockPos(2,3,1).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,0.5,0.5,1),
                    Shapes.box(0,0.5,0,1,1,1)
            );
        }
        if(new BlockPos(0,3,2).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0.5,0,0,1,4f/16,0.5),
                    Shapes.box(6f/16,4f/16,0,1,8f/16,10f/16),
                    Shapes.box(3f/16,8f/16,0,1,1,13f/16)
            );
        }
        if(new BlockPos(1,3,2).equals(posInMultiBlock)){
            return Shapes.block();
        }
        if(new BlockPos(2,3,2).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,0.5,4f/16,0.5),
                    Shapes.box(0,4f/16,0,10f/16,8f/16,10f/16),
                    Shapes.box(0,8f/16,0,13f/16,1,13f/16)
            );
        }



        if(new BlockPos(0,4,0).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(6f/16,0,6f/16,1,8f/16,1),
                    Shapes.box(11f/16,8f/16,11f/16,1,1,1)
            );
        }
        if(new BlockPos(1,4,0).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,6f/16,1,8f/16,1),
                    Shapes.box(0,8f/16,11f/16,1,1,1)
            );
        }
        if(new BlockPos(2,4,0).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,6f/16,10f/16,8f/16,1),
                    Shapes.box(0,8f/16,11f/16,5f/16,1,1)
            );
        }
        if(new BlockPos(0,4,1).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(6f/16,0,0,1,8f/16,1),
                    Shapes.box(11f/16,8f/16,0,1,1,1)
            );
        }
        if(new BlockPos(1,4,1).equals(posInMultiBlock)){
            return Shapes.block();
        }
        if(new BlockPos(2,4,1).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,10f/16,8f/16,1),
                    Shapes.box(0,8f/16,0,5f/16,1,1)
            );
        }
        if(new BlockPos(0,4,2).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(6f/16,0,0,1,8f/16,10f/16),
                    Shapes.box(11f/16,8f/16,0,1,1,5f/16)
            );
        }
        if(new BlockPos(1,4,2).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,1,8f/16,10f/16),
                    Shapes.box(0,8f/16,0,1,1,5f/16)
            );
        }
        if(new BlockPos(2,4,2).equals(posInMultiBlock)){
            return Shapes.or(
                    Shapes.box(0,0,0,10f/16,8f/16,10f/16),
                    Shapes.box(0,8f/16,0,5f/16,1,5f/16)
            );
        }

        if(new BlockPos(1,5,1).equals(posInMultiBlock)){
            return Shapes.block();
        }

        return Shapes.box(0,0,0,1,0.5,1);
    }


}
