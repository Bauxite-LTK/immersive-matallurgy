package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.utils.codec.IEDualCodecs;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class FlotationCellRecipeSerializer extends IERecipeSerializer<FlotationCellRecipe> {
    public static final DualMapCodec<RegistryFriendlyByteBuf, FlotationCellRecipe> CODECS = DualCompositeMapCodecs.composite(
            IEDualCodecs.FLUID_STACK.fieldOf("result_concentrate"), r -> r.outputConcentrate,
            optionalFluidOutput("result_tailing"), r -> r.outputTailing,
            IEDualCodecs.SIZED_FLUID_INGREDIENT.fieldOf("input_ore"), r -> r.inputOre,
            IEDualCodecs.SIZED_FLUID_INGREDIENT.fieldOf("input_add"), r -> r.inputAdd,
            DualCodecs.INT.fieldOf("time"), MultiblockRecipe::getBaseTime,
            DualCodecs.INT.fieldOf("energy"), MultiblockRecipe::getBaseEnergy,
            FlotationCellRecipe::new
    );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, FlotationCellRecipe> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return IMMultiblockLogic.FLOTATION_CELL.iconStack();
    }
}
