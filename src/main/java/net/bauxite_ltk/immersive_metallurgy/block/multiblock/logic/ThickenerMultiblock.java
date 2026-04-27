package net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic;

import blusunrize.immersiveengineering.api.multiblocks.ClientMultiblocks;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockProperties;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ThickenerMultiblock extends IETemplateMultiblock {
    public ThickenerMultiblock() {
        super(IMUtils.modRL("multiblocks/thickener"),
                ThickenerLogic.MASTER_OFFSET, new BlockPos(4, 1, 8), new BlockPos(9, 8, 9),
                IMMultiblockLogic.THICKENER);
    }

    @Override
    public float getManualScale() {
        return 8;
    }

    @Override
    public void initializeClient(Consumer<ClientMultiblocks.MultiblockManualData> consumer){
        consumer.accept(new IMMultiblockProperties(this, 4.5,0.5,4.5));
    }
}
