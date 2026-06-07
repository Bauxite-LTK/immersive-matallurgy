package net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic;

import blusunrize.immersiveengineering.api.multiblocks.ClientMultiblocks;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockProperties;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

public class EliteBlastFurnaceMultiblock extends IETemplateMultiblock {
    public EliteBlastFurnaceMultiblock() {
        super(IMUtils.modRL("multiblocks/elite_blast_furnace"),
                EliteBlastFurnaceLogic.MASTER_OFFSET, new BlockPos(2, 2, 3), new BlockPos(5, 7, 4),
                IMMultiblockLogic.ELITE_BLAST_FURNACE);
    }

    @Override
    public float getManualScale() {
        return 16;
    }

    @Override
    public void initializeClient(Consumer<ClientMultiblocks.MultiblockManualData> consumer){
        consumer.accept(new IMMultiblockProperties(this, 2.5,3.5,2.5));
    }
}
