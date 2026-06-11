package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.utils.codec.IEDualCodecs;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class EliteBlastFurnaceRecipeSerializer extends IERecipeSerializer<EliteBlastFurnaceRecipe> {
    public static final DualMapCodec<RegistryFriendlyByteBuf, EliteBlastFurnaceRecipe> CODECS = DualCompositeMapCodecs.composite(
            optionalFluidOutput("result_metal"), r -> r.outputMetal,
            optionalFluidOutput("result_gas"), r -> r.outputGas,
            optionalItemOutput("result_slag"), r -> r.outputSlag,
            IngredientWithSize.CODECS.fieldOf("input_ore"), r -> r.inputOre,
            DualCodecs.INT.fieldOf("temperature"), r -> r.temperature,
            DualCodecs.INT.fieldOf("time"), MultiblockRecipe::getBaseTime,
            DualCodecs.INT.fieldOf("energy"), MultiblockRecipe::getBaseEnergy,
            EliteBlastFurnaceRecipe::new
    );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, EliteBlastFurnaceRecipe> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return IMMultiblockLogic.ELITE_BLAST_FURNACE.iconStack();
    }
}
