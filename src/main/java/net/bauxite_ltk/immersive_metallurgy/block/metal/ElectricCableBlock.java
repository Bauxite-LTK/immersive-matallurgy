package net.bauxite_ltk.immersive_metallurgy.block.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.IEEntityBlock;
import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ElectricCableBlock extends IEEntityBlock<ElectricCableBlockEntity> {

    public static final EnumProperty<Direction> DEFAULT_FACING_PROP = IEProperties.FACING_ALL;

    public ElectricCableBlock(Properties blockProps) {
        super(IMBlockEntities.ELECTRIC_CABLE, blockProps
                .sound(SoundType.NETHERITE_BLOCK)
        );
    }



    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        BlockEntity te = world.getBlockEntity(pos);
        if(te instanceof ElectricCableBlockEntity cable)
        {
            if(world.isEmptyBlock(pos.relative(cable.getFacing())))
            {
                popResource(world, pos, new ItemStack(this));
                cable.getLevelNonnull().removeBlock(pos, false);
            }
        }
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(DEFAULT_FACING_PROP, BlockStateProperties.WATERLOGGED);
    }

}
