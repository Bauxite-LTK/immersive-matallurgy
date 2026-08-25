package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import com.google.common.collect.Lists;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;

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
}
