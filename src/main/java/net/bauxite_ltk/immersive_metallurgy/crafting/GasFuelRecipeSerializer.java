package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.utils.codec.IEDualCodecs;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.bauxite_ltk.immersive_metallurgy.item.IMItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;


public class GasFuelRecipeSerializer extends IERecipeSerializer<GasFuelRecipe> {

    public static final DualMapCodec<RegistryFriendlyByteBuf, GasFuelRecipe> CODECS = DualCompositeMapCodecs.composite(
            IEDualCodecs.SIZED_FLUID_INGREDIENT.fieldOf("input_gas"), f -> f.inputGas,
            DualCodecs.INT.fieldOf("burn_time"), f -> f.burnTime,
            GasFuelRecipe::new
    );


    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, GasFuelRecipe> codecs() {
        return CODECS;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(IMItems.WATER_GAS_BUCKET.asItem());
    }
}
