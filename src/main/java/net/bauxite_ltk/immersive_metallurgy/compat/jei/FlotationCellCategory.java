package net.bauxite_ltk.immersive_metallurgy.compat.jei;

import blusunrize.immersiveengineering.common.util.compat.jei.IERecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.bauxite_ltk.immersive_metallurgy.crafting.BallMillRecipe;
import net.bauxite_ltk.immersive_metallurgy.crafting.FlotationCellRecipe;
import net.bauxite_ltk.immersive_metallurgy.util.Helper;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.Arrays;

public class FlotationCellCategory extends IERecipeCategory<FlotationCellRecipe> {

    private final IDrawableStatic tankOverlay;

    public FlotationCellCategory(IGuiHelper helper) {
        super(helper, JEIRecipeTypes.FLOTATION_CELL, "block.immersive_metallurgy.flotation_cell");
        ResourceLocation background = IMUtils.modRL("textures/gui/flotation_cell_recipe.png");
        setBackground(helper.createDrawable(background, 0, 0, 113, 63));
        setIcon(IMMultiblockLogic.FLOTATION_CELL.iconStack());
        tankOverlay = helper.createDrawable(background, 179, 33, 16, 47);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FlotationCellRecipe recipe, IFocusGroup focuses)
    {

        int oreTankSize = Math.max(FluidType.BUCKET_VOLUME/5, recipe.inputOre.amount());
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 3)
                .setFluidRenderer(oreTankSize, false, 16, 47)
                .setOverlay(tankOverlay, 0, 0)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(recipe.inputOre.getFluids()))
                .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);

        int addTankSize = Math.max(FluidType.BUCKET_VOLUME/100, recipe.inputAdd.amount());
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 10)
                .setFluidRenderer(addTankSize, false, 4, 34)
                .setOverlay(tankOverlay, 0, 0)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(recipe.inputAdd.getFluids()))
                .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);

        int concentrateTankSize = Math.max(FluidType.BUCKET_VOLUME/10, recipe.outputConcentrate.getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 3)
                .setFluidRenderer(concentrateTankSize, false, 16, 25)
                .setOverlay(tankOverlay, 0, 0)
                .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.outputConcentrate)
                .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);

        if(recipe.outputTailing!=null&&!recipe.outputTailing.isEmpty())
        {
            int tailingTankSize = Math.max(FluidType.BUCKET_VOLUME/10, recipe.outputTailing.getAmount());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 35)
                    .setFluidRenderer(tailingTankSize, false, 16, 25)
                    .setOverlay(tankOverlay, 0, 0)
                    .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.outputTailing)
                    .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);
        }
    }

    @Override
    public void draw(FlotationCellRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IDrawable background = this.getBackground();
        int bWidth = background.getWidth();
        int bHeight = background.getHeight();
        Font font = Minecraft.getInstance().font;


        int time = recipe.getTotalProcessTime();
        int energy = recipe.getTotalProcessEnergy() / time;

        guiGraphics.pose().pushPose();
        {
            //guiGraphics.pose().translate(-8, 0, 0);

            String text0 = I18n.get("desc.immersive_metallurgy.info.ift", Helper.fDecimal(energy));
            guiGraphics.drawString(font, text0, bWidth / 2 - font.width(text0) / 2, font.lineHeight/2, 0xFFCCCCCC, false);

            String text1 = I18n.get("desc.immersiveengineering.info.ticks", Helper.fDecimal(time));
            guiGraphics.drawString(font, text1, bWidth / 2 - font.width(text1) / 2, font.lineHeight + font.lineHeight/2, 0xFFCCCCCC, false);
        }
        guiGraphics.pose().popPose();
    }
}
