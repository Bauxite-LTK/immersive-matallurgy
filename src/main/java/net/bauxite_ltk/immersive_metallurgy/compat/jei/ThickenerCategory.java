package net.bauxite_ltk.immersive_metallurgy.compat.jei;

import blusunrize.immersiveengineering.common.util.compat.jei.IERecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.bauxite_ltk.immersive_metallurgy.crafting.FlotationCellRecipe;
import net.bauxite_ltk.immersive_metallurgy.crafting.ThickenerRecipe;
import net.bauxite_ltk.immersive_metallurgy.util.Helper;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
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

    @Override
    public void draw(ThickenerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
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
            guiGraphics.drawString(font, text0, bWidth / 2 - font.width(text0) / 2, bHeight - (font.lineHeight * 2), 0xFFCCCCCC, false);

            String text1 = I18n.get("desc.immersiveengineering.info.ticks", Helper.fDecimal(time));
            guiGraphics.drawString(font, text1, bWidth / 2 - font.width(text1) / 2, bHeight - font.lineHeight, 0xFFCCCCCC, false);
        }
        guiGraphics.pose().popPose();
    }
}
