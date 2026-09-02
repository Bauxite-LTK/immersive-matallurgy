package net.bauxite_ltk.immersive_metallurgy.compat.jei;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import blusunrize.immersiveengineering.common.register.IEMultiblockLogic;
import blusunrize.immersiveengineering.common.util.compat.jei.DoubleIcon;
import blusunrize.immersiveengineering.common.util.compat.jei.IERecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
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
import net.bauxite_ltk.immersive_metallurgy.crafting.ContinuousCastingMachineRecipe;
import net.bauxite_ltk.immersive_metallurgy.crafting.GasFuelRecipe;
import net.bauxite_ltk.immersive_metallurgy.crafting.GasFuelRecipeSerializer;
import net.bauxite_ltk.immersive_metallurgy.gui.info.IMFuelInfoArea;
import net.bauxite_ltk.immersive_metallurgy.item.IMItems;
import net.bauxite_ltk.immersive_metallurgy.util.Helper;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.Arrays;

public class GasFuelCategory extends IERecipeCategory<GasFuelRecipe> {


    public GasFuelCategory(IGuiHelper helper) {
        super(helper, JEIRecipeTypes.GAS_FUEL, "gui.immersive_metallurgy.gas_fuel");
        ResourceLocation background = IMUtils.modRL("textures/gui/gas_fuel_recipe.png");
        setBackground(helper.createDrawable(background, 0, 0, 98, 42));
        setIcon(new DoubleIcon(
                helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, IMItems.WATER_GAS_BUCKET.toStack()),
                helper.createDrawable(background, 99, 1, 6, 7),
                1f
        ));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GasFuelRecipe recipe, IFocusGroup focuses)
    {

        int gasTankSize = Math.max(FluidType.BUCKET_VOLUME/10, recipe.inputGas.amount());
        builder.addSlot(RecipeIngredientRole.INPUT, 15, 9)
                .setFluidRenderer(gasTankSize, false, 8, 23)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(recipe.inputGas.getFluids()))
                .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);

    }

    @Override
    public void draw(GasFuelRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IDrawable background = this.getBackground();
        int bWidth = background.getWidth();
        int bHeight = background.getHeight();
        Font font = Minecraft.getInstance().font;


        int fuelTick = recipe.getBurnTime();

        guiGraphics.pose().pushPose();
        {
            //guiGraphics.pose().translate(-8, 0, 0);

            String text0 = I18n.get("desc.immersive_metallurgy.info.fuel_amount", Helper.fDecimal(fuelTick));
            guiGraphics.drawString(font, text0, 51 -(font.width(text0) / 2), bHeight - font.lineHeight, 0xFFCCCCCC, false);

        }
        guiGraphics.pose().popPose();

        new IMFuelInfoArea(GetterAndSetter.getterOnly(()->fuelTick), 50,74,4).draw(guiGraphics);

    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<GasFuelRecipe> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
        if(new Rect2i(74,1,12,34).contains((int)mouseX,(int)mouseY)){
            tooltip.add(Component.literal(
                    I18n.get(
                            "desc.immersive_metallurgy.info.fuel_amount",
                            Helper.fDecimal(recipe.value().getBurnTime())
                    )
            ));
        }
    }
}
