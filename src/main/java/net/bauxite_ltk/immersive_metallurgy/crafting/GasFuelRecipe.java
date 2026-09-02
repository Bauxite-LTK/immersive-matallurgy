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

public class GasFuelRecipe extends IESerializableRecipe {
    public static DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<GasFuelRecipe>> SERIALIZER;
    public static final CachedRecipeList<GasFuelRecipe> RECIPES = new CachedRecipeList<>(IMRecipeType.GAS_FUEL);



    @Nonnull
    public final int burnTime;
    public final SizedFluidIngredient inputGas;


    protected <T extends Recipe<?>> GasFuelRecipe(SizedFluidIngredient inputGas, int burnTime) {
        super(TagOutput.EMPTY, IMRecipeType.GAS_FUEL);

        this.burnTime = burnTime;
        this.inputGas = inputGas;

    }


    @Override
    protected IERecipeSerializer<GasFuelRecipe> getIESerializer()
    {
        return SERIALIZER.get();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider access)
    {
        return ItemStack.EMPTY;
    }

    public int getBurnTime(){
        return burnTime;
    }

    public static RecipeHolder<GasFuelRecipe> findRecipe(Level level, FluidStack inputGas)
    {
        if(inputGas.isEmpty())
            return null;
        for(RecipeHolder<GasFuelRecipe> recipe : RECIPES.getRecipes(level)){
            if(recipe.value().inputGas.test(inputGas)){
                return recipe;
            }
        }
        return null;
    }
}
