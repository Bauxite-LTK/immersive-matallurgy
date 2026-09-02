package net.bauxite_ltk.immersive_metallurgy.gui.info;

import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;

public class IMEnergyInfoArea extends InfoArea {
    private static final ResourceLocation BAR_TEXTURE = IMUtils.modRL("energy_bar/bar");
    private static final ResourceLocation CONTAINER_TEXTURE = IMUtils.modRL("energy_bar/container");
    private static final int CONTAINER_WIDTH = 12;
    private static final int CONTAINER_HEIGHT = 55;
    private static final int BAR_HEIGHT = 38;
    private static final int BAR_WIDTH = 4;
    private static final int BAR_POS_IN_CONTAINER_X = 4;
    private static final int BAR_POS_IN_CONTAINER_Y = 13;


    private final IEnergyStorage energy;

    public IMEnergyInfoArea(IEnergyStorage energy, int xMin, int yMin) {
        super(new Rect2i(xMin, yMin, CONTAINER_WIDTH, CONTAINER_HEIGHT));
        this.energy = energy;
    }

    @Override
    protected void fillTooltipOverArea(int mouseX, int mouseY, List<Component> tooltip) {
        tooltip.add(Component.literal(energy.getEnergyStored() +"/"+ energy.getMaxEnergyStored()+" IF"));
    }

    @Override
    public void draw(GuiGraphics graphics) {
        graphics.pose().pushPose();
        int energyBarH = 0;
        if(energy.getEnergyStored() > 0){
            energyBarH = Math.max(1, (int)(((float)energy.getEnergyStored() / energy.getMaxEnergyStored()) * BAR_HEIGHT));
        }

        graphics.blitSprite(
                CONTAINER_TEXTURE,
                area.getX(), area.getY(),
                CONTAINER_WIDTH, CONTAINER_HEIGHT
        );
        graphics.blitSprite(
                BAR_TEXTURE,
                BAR_WIDTH, BAR_HEIGHT,
                0, BAR_HEIGHT-energyBarH,
                area.getX() + BAR_POS_IN_CONTAINER_X, area.getY() + BAR_POS_IN_CONTAINER_Y + BAR_HEIGHT - energyBarH,
                BAR_WIDTH, energyBarH
        );
        graphics.pose().popPose();
    }
}
