package net.bauxite_ltk.immersive_metallurgy.block.transporter.casting_channel;

import blusunrize.immersiveengineering.common.blocks.IEEntityBlock;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlockEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CastingChannelBlock extends IEEntityBlock<CastingChannelBlockEntity> {

    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light_level", 0, 15);

    public CastingChannelBlock(Properties blockProps) {
        super(IMBlockEntities.CASTING_CHANNEL, blockProps
                .lightLevel(state -> state.getValue(LIGHT_LEVEL))
                .sound(SoundType.NETHERITE_BLOCK)
        );
        registerDefaultState(getStateDefinition().any().setValue(LIGHT_LEVEL,0));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(LIGHT_LEVEL);
    }
}
