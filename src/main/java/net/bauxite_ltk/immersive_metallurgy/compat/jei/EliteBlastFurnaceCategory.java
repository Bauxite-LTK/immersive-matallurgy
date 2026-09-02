package net.bauxite_ltk.immersive_metallurgy.compat.jei;

import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
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
import net.bauxite_ltk.immersive_metallurgy.crafting.EliteBlastFurnaceRecipe;
import net.bauxite_ltk.immersive_metallurgy.crafting.FlotationCellRecipe;
import net.bauxite_ltk.immersive_metallurgy.gui.info.TemperatureInfoArea;
import net.bauxite_ltk.immersive_metallurgy.util.Helper;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.Arrays;
import java.util.Collections;

public class EliteBlastFurnaceCategory extends IERecipeCategory<EliteBlastFurnaceRecipe> {

    private final IDrawableStatic tankOverlay;

    public EliteBlastFurnaceCategory(IGuiHelper helper) {
        super(helper, JEIRecipeTypes.ELITE_BLAST_FURNACE, "block.immersive_metallurgy.elite_blast_furnace");
        ResourceLocation background = IMUtils.modRL("textures/gui/elite_blast_furnace_recipe.png");
        setBackground(helper.createDrawable(background, 0, 0, 132, 62));
        setIcon(IMMultiblockLogic.ELITE_BLAST_FURNACE.iconStack());
        tankOverlay = helper.createDrawable(background, 179, 33, 16, 47);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EliteBlastFurnaceRecipe recipe, IFocusGroup focuses)
    {
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 8)
                .addItemStacks(Arrays.asList(recipe.inputOre.getMatchingStacks()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 32)
                .addItemStack(recipe.outputSlag.get());

        int metalTankSize = Math.max(FluidType.BUCKET_VOLUME/5, recipe.outputMetal.getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 57, 21)
                .setFluidRenderer(metalTankSize, false, 8, 23)
                .setOverlay(tankOverlay, 0, 0)
                .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.outputMetal)
                .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);

        int gasTankSize = Math.max(FluidType.BUCKET_VOLUME/2, recipe.outputGas.getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 103, 12)
                .setFluidRenderer(gasTankSize, false, 8, 41)
                .setOverlay(tankOverlay, 0, 0)
                .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.outputGas)
                .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);


    }

    @Override
    public void draw(EliteBlastFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IDrawable background = this.getBackground();
        int bWidth = background.getWidth();
        int bHeight = background.getHeight();
        Font font = Minecraft.getInstance().font;


        int time = recipe.getTotalProcessTime();
        int temperature = recipe.getTemperature();

        guiGraphics.pose().pushPose();
        {
            //guiGraphics.pose().translate(-8, 0, 0);

            String text0 = I18n.get("desc.immersive_metallurgy.info.temperature", Helper.fDecimal(temperature));
            guiGraphics.drawString(font, text0, 20, bHeight - font.lineHeight, 0xFFCCCCCC, false);

            String text1 = I18n.get("desc.immersiveengineering.info.ticks", Helper.fDecimal(time));
            guiGraphics.drawString(font, text1, bWidth / 2 - font.width(text1) / 2, font.lineHeight/4 , 0xFFCCCCCC, false);
        }
        guiGraphics.pose().popPose();

        new TemperatureInfoArea(GetterAndSetter.getterOnly(()->temperature),10,1).draw(guiGraphics);

    }
}
