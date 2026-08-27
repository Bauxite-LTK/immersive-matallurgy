package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;

public class ContinuousCastingMachineFuelRecipe extends IESerializableRecipe {
    public static DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<ContinuousCastingMachineFuelRecipe>> SERIALIZER;
    public static final CachedRecipeList<ContinuousCastingMachineFuelRecipe> RECIPES = new CachedRecipeList<>(IMRecipeType.CONTINUOUS_CASTING_MACHINE_FUEL);



    @Nonnull
    public final int burnTime;
    public final SizedFluidIngredient inputGas;


    protected <T extends Recipe<?>> ContinuousCastingMachineFuelRecipe(SizedFluidIngredient inputGas, int burnTime) {
        super(TagOutput.EMPTY, IMRecipeType.CONTINUOUS_CASTING_MACHINE_FUEL);

        this.burnTime = burnTime;
        this.inputGas = inputGas;

    }


    @Override
    protected IERecipeSerializer<ContinuousCastingMachineFuelRecipe> getIESerializer()
    {
        return SERIALIZER.get();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider access)
    {
        return ItemStack.EMPTY;
    }


    public static RecipeHolder<ContinuousCastingMachineFuelRecipe> findRecipe(Level level, FluidStack inputGas)
    {
        if(inputGas.isEmpty())
            return null;
        for(RecipeHolder<ContinuousCastingMachineFuelRecipe> recipe : RECIPES.getRecipes(level)){
            if(recipe.value().inputGas.test(inputGas)){
                return recipe;
            }
        }
        return null;
    }
}
