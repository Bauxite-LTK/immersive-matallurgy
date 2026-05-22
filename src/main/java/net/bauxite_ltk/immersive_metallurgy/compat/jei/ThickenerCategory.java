package net.bauxite_ltk.immersive_metallurgy.compat.jei;

import blusunrize.immersiveengineering.common.util.compat.jei.IERecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.bauxite_ltk.immersive_metallurgy.crafting.ThickenerRecipe;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.Arrays;

public class ThickenerCategory extends IERecipeCategory<ThickenerRecipe> {

    private final IDrawableStatic tankOverlay;

    public ThickenerCategory(IGuiHelper helper) {
        super(helper, JEIRecipeTypes.THICKENER, "block.immersive_metallurgy.thickener");
        ResourceLocation background = IMUtils.modRL("textures/gui/thickener.png");
        setBackground(helper.createDrawable(background, 8, 9, 151-7, 71-8));
        setIcon(IMMultiblockLogic.THICKENER.iconStack());
        tankOverlay = helper.createDrawable(background, 179, 33, 16, 47);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ThickenerRecipe recipe, IFocusGroup focuses)
    {

        int tankSize = Math.max(FluidType.BUCKET_VOLUME/2, recipe.inputFluid.amount());
        builder.addSlot(RecipeIngredientRole.INPUT, 14-8, 18-9)
                .setFluidRenderer(tankSize, false, 16, 47)
                .setOverlay(tankOverlay, 0, 0)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(recipe.inputFluid.getFluids()))
                .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);

        IRecipeSlotBuilder outputBuilder = builder.addSlot(RecipeIngredientRole.OUTPUT, 131-8, 48-9);
        if(!recipe.outputItem.get().isEmpty())
            outputBuilder.addItemStack(recipe.outputItem.get());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 131-8, 12-9)
                .setFluidRenderer(tankSize, false, 16, 25)
                .setOverlay(tankOverlay, 0, 0)
                .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.outputFluid)
                .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);

    }
}
