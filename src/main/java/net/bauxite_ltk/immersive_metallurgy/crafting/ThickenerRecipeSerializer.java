package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.utils.codec.IEDualCodecs;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class ThickenerRecipeSerializer extends IERecipeSerializer<ThickenerRecipe> {
    public static final DualMapCodec<RegistryFriendlyByteBuf, ThickenerRecipe> CODECS = DualCompositeMapCodecs.composite(
            IEDualCodecs.FLUID_STACK.fieldOf("result_fluid"), r -> r.outputFluid,
            TagOutput.CODECS.fieldOf("result_item"), r -> r.outputItem,
            IEDualCodecs.SIZED_FLUID_INGREDIENT.fieldOf("input_fluid"), r -> r.inputFluid,
            DualCodecs.INT.fieldOf("energy"), MultiblockRecipe::getBaseEnergy,
            ThickenerRecipe::new
    );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, ThickenerRecipe> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return IMMultiblockLogic.THICKENER.iconStack();
    }
}
