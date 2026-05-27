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

public class ThickenerRecipe extends MultiblockRecipe {
    public static DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<ThickenerRecipe>> SERIALIZER;
    public static final CachedRecipeList<ThickenerRecipe> RECIPES = new CachedRecipeList<>(IMRecipeType.THICKENER);
    //TODO Add Multiplier Config TO Ball Mill
    public static Supplier<RecipeMultiplier> MULTIPLIERS = () -> new RecipeMultiplier(()->1.0, ()->1.0);


    public final FluidStack outputFluid;
    @Nonnull
    public TagOutput outputItem;

    public final SizedFluidIngredient inputFluid;



    protected <T extends Recipe<?>> ThickenerRecipe(FluidStack outputFluid, @Nonnull TagOutput outputItem,
                                                    SizedFluidIngredient inputFluid,int time, int energy) {

        super(TagOutput.EMPTY, IMRecipeType.THICKENER, time, energy, MULTIPLIERS);
        this.outputFluid = outputFluid;
        this.inputFluid = inputFluid;
        this.outputItem = outputItem;

        this.outputList = new TagOutputList(this.outputItem);
        this.fluidInputList = Lists.newArrayList(this.inputFluid);
        this.fluidOutputList = Lists.newArrayList(this.outputFluid);

    }

    @Override
    protected IERecipeSerializer<?> getIESerializer() {
        return SERIALIZER.get();
    }


    public static RecipeHolder<ThickenerRecipe> findRecipe(Level level, FluidStack inputFluid)
    {
        if(inputFluid.isEmpty())
            return null;
        for(RecipeHolder<ThickenerRecipe> recipe : RECIPES.getRecipes(level)){
            if(recipe.value().inputFluid.test(inputFluid)){
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
