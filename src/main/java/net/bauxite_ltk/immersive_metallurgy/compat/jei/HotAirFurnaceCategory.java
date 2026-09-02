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
import net.bauxite_ltk.immersive_metallurgy.crafting.HotAirFurnaceRecipe;
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

public class HotAirFurnaceCategory extends IERecipeCategory<HotAirFurnaceRecipe> {

    private final IDrawableStatic tankOverlay;

    public HotAirFurnaceCategory(IGuiHelper helper) {
        super(helper, JEIRecipeTypes.HOT_AIR_FURNACE, "block.immersive_metallurgy.hot_air_furnace");
        ResourceLocation background = IMUtils.modRL("textures/gui/hot_air_furnace.png");
        setBackground(helper.createDrawable(background, 23, 9, 130, 59));
        setIcon(IMMultiblockLogic.HOT_AIR_FURNACE.iconStack());
        tankOverlay = helper.createDrawable(background, 179, 33, 16, 47);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, HotAirFurnaceRecipe recipe, IFocusGroup focuses)
    {
        int metalTankSize = Math.max(FluidType.BUCKET_VOLUME/5, recipe.inputFluidGas.amount());
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 9)
                .setFluidRenderer(metalTankSize, false, 8, 41)
                .setOverlay(tankOverlay, 0, 0)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(recipe.inputFluidGas.getFluids()))
                .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);

        int gasTankSize = Math.max(FluidType.BUCKET_VOLUME/5, recipe.outputFluidAir.getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 9)
                .setFluidRenderer(gasTankSize, false, 8, 41)
                .setOverlay(tankOverlay, 0, 0)
                .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.outputFluidAir)
                .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);


    }

    @Override
    public void draw(HotAirFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IDrawable background = this.getBackground();
        int bWidth = background.getWidth();
        int bHeight = background.getHeight();
        Font font = Minecraft.getInstance().font;


        int time = recipe.getTotalProcessTime();

        guiGraphics.pose().pushPose();
        {
            //guiGraphics.pose().translate(-8, 0, 0);


            String text1 = I18n.get("desc.immersiveengineering.info.ticks", Helper.fDecimal(time));
            guiGraphics.drawString(font, text1, bWidth / 2 - font.width(text1) / 2, bHeight - font.lineHeight*2 , 0xFFCCCCCC, false);
        }
        guiGraphics.pose().popPose();

    }
}
