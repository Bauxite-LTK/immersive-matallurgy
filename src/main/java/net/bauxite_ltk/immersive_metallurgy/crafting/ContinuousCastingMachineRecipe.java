package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.crafting.TagOutputList;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import com.google.common.collect.Lists;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ContinuousCastingMachineRecipe extends MultiblockRecipe {
    public static DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<ContinuousCastingMachineRecipe>> SERIALIZER;
    public static final CachedRecipeList<ContinuousCastingMachineRecipe> RECIPES = new CachedRecipeList<>(IMRecipeType.CONTINUOUS_CASTING_MACHINE);

    public static Supplier<RecipeMultiplier> MULTIPLIERS = () -> new RecipeMultiplier(()->1.0, ()->1.0);;


    @Nonnull
    public TagOutput outputItem;

    public final SizedFluidIngredient inputMetal;


    protected <T extends Recipe<?>> ContinuousCastingMachineRecipe(TagOutput outputItem,
                                                                   SizedFluidIngredient inputMetal,
                                                                   int baseTime, int energy) {

        super(TagOutput.EMPTY, IMRecipeType.CONTINUOUS_CASTING_MACHINE, baseTime, energy, MULTIPLIERS);

        this.outputItem = outputItem;
        this.inputMetal = inputMetal;


        this.fluidInputList = Lists.newArrayList(this.inputMetal);
        this.outputList = new TagOutputList(this.outputItem);


    }

    @Override
    protected IERecipeSerializer<?> getIESerializer() {
        return SERIALIZER.get();
    }


    public static RecipeHolder<ContinuousCastingMachineRecipe> findRecipe(Level level, FluidStack inputMetal)
    {
        if(inputMetal.isEmpty())
            return null;
        for(RecipeHolder<ContinuousCastingMachineRecipe> recipe : RECIPES.getRecipes(level)){
            if(recipe.value().inputMetal.test(inputMetal)){
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
