package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

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


    public static SortedMap<Component, Integer> getBurnTimeValuesSorted(Level level, boolean inverse)
    {
        SortedMap<Component, Integer> map = new TreeMap<>(
                Comparator.comparing(
                        (Function<Component, String>) Component::getString,
                        inverse?Comparator.reverseOrder(): Comparator.naturalOrder()
                )
        );
        for(RecipeHolder<GasFuelRecipe> holder : RECIPES.getRecipes(level))
        {
            GasFuelRecipe recipe = holder.value();
            if(!recipe.inputGas.ingredient().hasNoFluids())
            {
                FluidStack is = recipe.inputGas.getFluids()[0];
                map.put(is.getHoverName(), recipe.burnTime);
            }
        }
        return map;
    }
}
