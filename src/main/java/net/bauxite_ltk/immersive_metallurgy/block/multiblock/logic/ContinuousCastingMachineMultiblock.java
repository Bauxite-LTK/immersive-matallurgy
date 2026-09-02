package net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic;

import blusunrize.immersiveengineering.api.multiblocks.ClientMultiblocks;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockProperties;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

public class ContinuousCastingMachineMultiblock extends IETemplateMultiblock {

    public ContinuousCastingMachineMultiblock() {
        super(IMUtils.modRL("multiblocks/continuous_casting_machine"),
                ContinuousCastingMachineLogic.MASTER_OFFSET, new BlockPos(1, 3, 6), new BlockPos(3, 5, 7),
                IMMultiblockLogic.CONTINUOUS_CASTING_MACHINE);
    }

    @Override
    public float getManualScale() {
        return 16;
    }

    @Override
    public void initializeClient(Consumer<ClientMultiblocks.MultiblockManualData> consumer){
        consumer.accept(new IMMultiblockProperties(this, 1.5,2.5,3.5));
    }
}
