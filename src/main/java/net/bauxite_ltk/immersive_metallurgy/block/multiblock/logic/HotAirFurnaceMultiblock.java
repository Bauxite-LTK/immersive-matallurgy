package net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic;

import blusunrize.immersiveengineering.api.multiblocks.ClientMultiblocks;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockProperties;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

public class HotAirFurnaceMultiblock extends IETemplateMultiblock {

    public HotAirFurnaceMultiblock() {
        super(IMUtils.modRL("multiblocks/hot_air_furnace"),
                HotAirFurnaceLogic.MASTER_OFFSET, new BlockPos(1, 3, 2), new BlockPos(3, 6, 3),
                IMMultiblockLogic.HOT_AIR_FURNACE);
    }

    @Override
    public float getManualScale() {
        return 16;
    }

    @Override
    public void initializeClient(Consumer<ClientMultiblocks.MultiblockManualData> consumer){
        consumer.accept(new IMMultiblockProperties(this, 1.5,3,1.5));
    }
}
