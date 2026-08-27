package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.crafting.TagOutputList;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import blusunrize.immersiveengineering.api.utils.codec.IEDualCodecs;
import com.google.common.collect.Lists;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ContinuousCastingMachineRecipeSerializer extends IERecipeSerializer<ContinuousCastingMachineRecipe> {

    public static final DualMapCodec<RegistryFriendlyByteBuf, ContinuousCastingMachineRecipe> CODEC = DualCompositeMapCodecs.composite(
            TagOutput.CODECS.fieldOf("result_metal_item"), f -> f.outputItem,
            IEDualCodecs.SIZED_FLUID_INGREDIENT.fieldOf("input_metal_liquid"), f -> f.inputMetal,
            DualCodecs.INT.fieldOf("time"), MultiblockRecipe::getBaseTime,
            DualCodecs.INT.fieldOf("energy"), MultiblockRecipe::getBaseEnergy,
            ContinuousCastingMachineRecipe::new
    );

    @Override
    public ItemStack getIcon() {
        return IMMultiblockLogic.CONTINUOUS_CASTING_MACHINE.iconStack();
    }

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, ContinuousCastingMachineRecipe> codecs() {
        return CODEC;
    }
}
