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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class BallMillRecipe extends MultiblockRecipe {
    public static DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<BallMillRecipe>> SERIALIZER;
    public static final CachedRecipeList<BallMillRecipe> RECIPES = new CachedRecipeList<>(IMRecipeType.BALL_MILL);

    public static Supplier<RecipeMultiplier> MULTIPLIERS = () -> new RecipeMultiplier(()->1.0, ()->1.0);;


    public final FluidStack outputFluid;
    public TagOutput outputItem;

    @Nullable
    public final SizedFluidIngredient inputFluid;
    public IngredientWithSize inputItem;

    protected <T extends Recipe<?>> BallMillRecipe(FluidStack  outputFluid, TagOutput outputItem,
                                                   Optional<SizedFluidIngredient> inputFluid, IngredientWithSize inputItem,
                                                   int time, int energy){
        this(outputFluid, outputItem, inputFluid.orElse(null), inputItem, time, energy);
    }


    protected <T extends Recipe<?>> BallMillRecipe(FluidStack outputFluid, TagOutput outputItem,
                                                   @Nullable SizedFluidIngredient inputFluid, IngredientWithSize inputItem,
                                                    int time, int energy) {
        super(TagOutput.EMPTY, IMRecipeType.BALL_MILL, time, energy, MULTIPLIERS);
        this.outputFluid = outputFluid;
        this.inputFluid = inputFluid;
        this.inputItem = inputItem;
        this.outputItem = outputItem;

        setInputListWithSizes(Lists.newArrayList(this.inputItem));
        this.outputList = new TagOutputList(this.outputItem);
        if(this.inputFluid!=null){
            this.fluidInputList = Lists.newArrayList(this.inputFluid);
        }
        this.fluidOutputList = Lists.newArrayList(this.outputFluid);

    }

    @Override
    protected IERecipeSerializer<?> getIESerializer() {
        return SERIALIZER.get();
    }

    public BallMillRecipe setInputSize(int size)
    {
        this.inputItem = this.inputItem.withSize(size);
        return this;
    }

    public static RecipeHolder<BallMillRecipe> findRecipe(Level level, ItemStack inputItem, FluidStack inputFluid)
    {
        if(inputItem.isEmpty())
            return null;
        for(RecipeHolder<BallMillRecipe> recipe : RECIPES.getRecipes(level)){
            if(!inputFluid.isEmpty()){
                if(recipe.value().inputFluid!=null && recipe.value().inputFluid.test(inputFluid)){
                    if(recipe.value().inputItem.test(inputItem))
                        return recipe;
                }
            }
            else{
                if(recipe.value().inputFluid==null && recipe.value().inputItem.test(inputItem)){
                    return recipe;
                }
            }
        }
        return null;
    }

    @Override
    public int getMultipleProcessTicks() {
        return 4;
    }
}
