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

public class HotAirFurnaceRecipeSerializer extends IERecipeSerializer<HotAirFurnaceRecipe> {
    public static final DualMapCodec<RegistryFriendlyByteBuf, HotAirFurnaceRecipe> CODECS = DualCompositeMapCodecs.composite(
            IEDualCodecs.FLUID_STACK.fieldOf("result_fluid"), r -> r.outputFluidAir,
            IEDualCodecs.SIZED_FLUID_INGREDIENT.fieldOf("input_fluid"), r -> r.inputFluidGas,
            DualCodecs.INT.fieldOf("time"), MultiblockRecipe::getBaseTime,
            DualCodecs.INT.fieldOf("energy"), MultiblockRecipe::getBaseEnergy,
            HotAirFurnaceRecipe::new
    );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, HotAirFurnaceRecipe> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return IMMultiblockLogic.HOT_AIR_FURNACE.iconStack();
    }
}
