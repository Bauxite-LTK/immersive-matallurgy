package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class EliteBlastFurnaceRecipe extends MultiblockRecipe {
    public static DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<EliteBlastFurnaceRecipe>> SERIALIZER;
    public static final CachedRecipeList<EliteBlastFurnaceRecipe> RECIPES = new CachedRecipeList<>(IMRecipeType.ELITE_BLAST_FURNACE);

    public static Supplier<RecipeMultiplier> MULTIPLIERS = () -> new RecipeMultiplier(()->1.0, ()->1.0);;


    public final IngredientWithSize inputOre;
    public final int temperature;
    public final TagOutput outputSlag;
    public FluidStack outputMetal;
    public FluidStack outputGas;




    protected <T extends Recipe<?>> EliteBlastFurnaceRecipe(FluidStack outputMetal, FluidStack outputGas,
                                                            TagOutput outputSlag,
                                                            IngredientWithSize inputOre, int temperature,
                                                            int baseTime, int energy) {

        super(TagOutput.EMPTY, IMRecipeType.ELITE_BLAST_FURNACE, baseTime, energy, MULTIPLIERS);
        this.outputMetal = outputMetal;
        this.outputGas = outputGas;
        this.outputSlag = outputSlag;
        this.inputOre = inputOre;
        this.temperature = temperature;


        setInputListWithSizes(Lists.newArrayList(this.inputOre));
        this.outputList = new TagOutputList(this.outputSlag);


        this.fluidOutputList = Lists.newArrayList(this.outputMetal);
        this.fluidOutputList.add(this.outputGas);

    }

    @Override
    protected IERecipeSerializer<?> getIESerializer() {
        return SERIALIZER.get();
    }


    public static RecipeHolder<EliteBlastFurnaceRecipe> findRecipe(Level level, ItemStack inputOre)
    {
        if(inputOre.isEmpty())
            return null;
        for(RecipeHolder<EliteBlastFurnaceRecipe> recipe : RECIPES.getRecipes(level)){
            if(recipe.value().inputOre.test(inputOre)){
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
