package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.utils.codec.IEDualCodecs;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.bauxite_ltk.immersive_metallurgy.item.IMItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;


public class ContinuousCastingMachineFuelRecipeSerializer extends IERecipeSerializer<ContinuousCastingMachineFuelRecipe> {

    public static final DualMapCodec<RegistryFriendlyByteBuf, ContinuousCastingMachineFuelRecipe> CODECS = DualCompositeMapCodecs.composite(
            IEDualCodecs.SIZED_FLUID_INGREDIENT.fieldOf("input_gas"), f -> f.inputGas,
            DualCodecs.INT.fieldOf("burn_time"), f -> f.burnTime,
            ContinuousCastingMachineFuelRecipe::new
    );


    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, ContinuousCastingMachineFuelRecipe> codecs() {
        return CODECS;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(IMItems.WATER_GAS_BUCKET.asItem());
    }
}
