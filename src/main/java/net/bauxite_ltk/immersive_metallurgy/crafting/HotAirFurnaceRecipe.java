package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class HotAirFurnaceRecipe extends MultiblockRecipe {
    public static DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<HotAirFurnaceRecipe>> SERIALIZER;
    public static final CachedRecipeList<HotAirFurnaceRecipe> RECIPES = new CachedRecipeList<>(IMRecipeType.HOT_AIR_FURNACE);

    public static Supplier<RecipeMultiplier> MULTIPLIERS = () -> new RecipeMultiplier(()->1.0, ()->1.0);;

    public final FluidStack outputFluidAir;
    public final SizedFluidIngredient inputFluidGas;

    protected <T extends Recipe<?>> HotAirFurnaceRecipe(FluidStack outputFluidAir, SizedFluidIngredient inputFluidGas,
                                                        int baseTime, int baseEnergy) {
        super(TagOutput.EMPTY, IMRecipeType.HOT_AIR_FURNACE, baseTime, baseEnergy, MULTIPLIERS);
        this.inputFluidGas = inputFluidGas;
        this.outputFluidAir = outputFluidAir;


        this.fluidInputList = Lists.newArrayList(this.inputFluidGas);
        this.fluidOutputList = Lists.newArrayList(this.outputFluidAir);

    }

    @Override
    protected IERecipeSerializer<?> getIESerializer() {
        return SERIALIZER.get();
    }


    public static RecipeHolder<HotAirFurnaceRecipe> findRecipe(Level level, FluidStack inputGas)
    {
        if(inputGas.isEmpty())
            return null;
        for(RecipeHolder<HotAirFurnaceRecipe> recipe : RECIPES.getRecipes(level)){
            if(recipe.value().inputFluidGas.test(inputGas)){
                return recipe;
            }
        }
        return null;
    }

    @Override
    public int getMultipleProcessTicks() {
        return 0;
    }


    public static SortedMap<Component, Float> getInputValuesSorted(Level level, Fluid out, boolean inverse)
    {
        SortedMap<Component, Float> map = new TreeMap<>(
                Comparator.comparing(
                        (Function<Component, String>) Component::getString,
                        inverse?Comparator.reverseOrder(): Comparator.naturalOrder()
                )
        );
        for(RecipeHolder<HotAirFurnaceRecipe> holder : RECIPES.getRecipes(level))
        {
            HotAirFurnaceRecipe recipe = holder.value();
            if(recipe.outputFluidAir!=null&&recipe.outputFluidAir.getFluid()==out && !recipe.inputFluidGas.ingredient().hasNoFluids())
            {
                FluidStack is = recipe.inputFluidGas.getFluids()[0];
                map.put(is.getHoverName(), (float)recipe.inputFluidGas.amount() * 20f/recipe.getBaseTime());
            }
        }
        return map;
    }
}
