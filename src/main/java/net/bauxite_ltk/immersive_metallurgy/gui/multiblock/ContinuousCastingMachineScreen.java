package net.bauxite_ltk.immersive_metallurgy.gui.multiblock;

import blusunrize.immersiveengineering.api.energy.AveragingEnergyStorage;
import blusunrize.immersiveengineering.client.gui.IEContainerScreen;
import blusunrize.immersiveengineering.client.gui.info.FluidInfoArea;
import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import com.google.common.collect.ImmutableList;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.ContinuousCastingMachineLogic;
import net.bauxite_ltk.immersive_metallurgy.gui.info.IMEnergyInfoArea;
import net.bauxite_ltk.immersive_metallurgy.gui.info.IMFuelInfoArea;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.List;

public class ContinuousCastingMachineScreen extends IEContainerScreen<ContinuousCastingMachineMenu> {
    private static final ResourceLocation TEXTURE = makeTextureLocation("continuous_casting_machine");
    private static final ResourceLocation INPUT_METAL_TANK = IMUtils.modRL("continuous_casting_machine/input_metal_tank");
    private static final ResourceLocation INPUT_SMALL_TANK = IMUtils.modRL("continuous_casting_machine/input_small_tank");
    private static final ResourceLocation PROGRESS = IMUtils.modRL("continuous_casting_machine/progress");


    public ContinuousCastingMachineScreen(ContinuousCastingMachineMenu container, Inventory inventoryPlayer, Component title) {
        super(container, inventoryPlayer, title, TEXTURE);
    }

    @Nonnull
    @Override
    protected List<InfoArea> makeInfoAreas()
    {
        return ImmutableList.of(
                new FluidInfoArea(menu.tanks.inputGas(), new Rect2i(leftPos+98, topPos+18, 8, 23), 18, 32, INPUT_SMALL_TANK),
                new FluidInfoArea(menu.tanks.inputWater(), new Rect2i(leftPos+76, topPos+18, 8, 23), 18, 32, INPUT_SMALL_TANK),
                new FluidInfoArea(menu.tanks.inputMetal(), new Rect2i(leftPos+16, topPos+20, 8, 41), 18, 49, INPUT_METAL_TANK),
                new IMFuelInfoArea(menu.fuelTicks, ContinuousCastingMachineLogic.MAX_FUEL_TICKS, leftPos+116,topPos+13),
                new IMEnergyInfoArea(menu.energy, leftPos+155,topPos+13)
                );
    }

    public static ResourceLocation makeTextureLocation(String name) {
        return IMUtils.modRL( "textures/gui/"+name+".png");
    }

    @Override
    protected void drawContainerBackgroundPre(@Nonnull GuiGraphics graphics, float f, int mx, int my)
    {
        float process = menu.guiProgress.get();
        //ImmersiveMetallurgy.LOGGER.info("progress:{}", process);
        if(process > 0)
        {
            int w = (int)Math.max(1, process*115);
            graphics.blitSprite(PROGRESS, 115, 51, 0, 0, leftPos+31, topPos+16, w, 51);
        }
    }
}
