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

public class BallMillRecipeSerializer extends IERecipeSerializer<BallMillRecipe> {
    public static final DualMapCodec<RegistryFriendlyByteBuf, BallMillRecipe> CODECS = DualCompositeMapCodecs.composite(
            optionalFluidOutput("result_fluid"), r -> r.outputFluid,
            optionalItemOutput("result_item"), r -> r.outputItem,
            IEDualCodecs.SIZED_FLUID_INGREDIENT.optionalFieldOf("input_fluid"), r -> Optional.ofNullable(r.inputFluid),
            IngredientWithSize.CODECS.fieldOf("input_item"), r -> r.inputItem,
            DualCodecs.INT.fieldOf("time"), MultiblockRecipe::getBaseTime,
            DualCodecs.INT.fieldOf("energy"), MultiblockRecipe::getBaseEnergy,
            BallMillRecipe::new
    );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, BallMillRecipe> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return IMMultiblockLogic.BALL_MILL.iconStack();
    }
}
