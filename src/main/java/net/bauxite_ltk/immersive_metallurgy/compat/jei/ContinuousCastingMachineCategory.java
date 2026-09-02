package net.bauxite_ltk.immersive_metallurgy.compat.jei;

import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import blusunrize.immersiveengineering.common.util.compat.jei.IERecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.ContinuousCastingMachineLogic;
import net.bauxite_ltk.immersive_metallurgy.crafting.ContinuousCastingMachineRecipe;
import net.bauxite_ltk.immersive_metallurgy.gui.info.IMFuelInfoArea;
import net.bauxite_ltk.immersive_metallurgy.gui.info.TemperatureInfoArea;
import net.bauxite_ltk.immersive_metallurgy.util.Helper;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.Arrays;

public class ContinuousCastingMachineCategory extends IERecipeCategory<ContinuousCastingMachineRecipe> {

    private final IDrawableStatic tankOverlay;

    public ContinuousCastingMachineCategory(IGuiHelper helper) {
        super(helper, JEIRecipeTypes.CONTINUOUS_CASTING_MACHINE, "block.immersive_metallurgy.continuous_casting_machine");
        ResourceLocation background = IMUtils.modRL("textures/gui/continuous_casting_machine.png");
        setBackground(helper.createDrawable(background, 6, 11, 149, 59));
        setIcon(IMMultiblockLogic.CONTINUOUS_CASTING_MACHINE.iconStack());
        tankOverlay = helper.createDrawable(background, 179, 33, 16, 47);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ContinuousCastingMachineRecipe recipe, IFocusGroup focuses)
    {


        builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 18)
                .addItemStack(recipe.outputItem.get());

        int metalTankSize = Math.max(FluidType.BUCKET_VOLUME/2, recipe.inputMetal.amount());
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 9)
                .setFluidRenderer(metalTankSize, false, 8, 41)
                .setOverlay(tankOverlay, 0, 0)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(recipe.inputMetal.getFluids()))
                .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);

        int waterTankSize = Math.max(FluidType.BUCKET_VOLUME, recipe.getTotalProcessTime() * 50);
        builder.addSlot(RecipeIngredientRole.INPUT, 70, 7)
                .setFluidRenderer(waterTankSize, false, 8, 23)
                .setOverlay(tankOverlay, 0, 0)
                .addIngredient(NeoForgeTypes.FLUID_STACK, new FluidStack(Fluids.WATER,recipe.getTotalProcessTime() * 50))
                .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);

    }

    @Override
    public void draw(ContinuousCastingMachineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IDrawable background = this.getBackground();
        int bWidth = background.getWidth();
        int bHeight = background.getHeight();
        Font font = Minecraft.getInstance().font;


        int time = recipe.getTotalProcessTime();
        int energy = recipe.getTotalProcessEnergy() / time;
        int fuelTick = recipe.getTotalProcessTime();

        guiGraphics.pose().pushPose();
        {
            //guiGraphics.pose().translate(-8, 0, 0);

            String text0 = I18n.get("desc.immersive_metallurgy.info.ift", Helper.fDecimal(energy));
            guiGraphics.drawString(font, text0, 28, bHeight - font.lineHeight, 0xFFCCCCCC, false);

            String text1 = I18n.get("desc.immersiveengineering.info.ticks", Helper.fDecimal(time));
            guiGraphics.drawString(font, text1, 28, font.lineHeight/4 , 0xFFCCCCCC, false);
        }
        guiGraphics.pose().popPose();

        new IMFuelInfoArea(GetterAndSetter.getterOnly(()->fuelTick), fuelTick,110,2).draw(guiGraphics);

    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<ContinuousCastingMachineRecipe> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
        if(new Rect2i(110,2,12,34).contains((int)mouseX,(int)mouseY)){
            tooltip.add(Component.translatable(
                    "desc.immersive_metallurgy.info.fuel_cost",
                    Helper.fDecimal(recipe.value().getBaseTime())
                    ));
        }
    }
}
