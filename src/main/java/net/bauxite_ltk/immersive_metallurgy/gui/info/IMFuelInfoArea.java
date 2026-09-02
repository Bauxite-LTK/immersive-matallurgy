package net.bauxite_ltk.immersive_metallurgy.gui.info;

import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class IMFuelInfoArea extends InfoArea {
    private static final ResourceLocation BAR_TEXTURE = IMUtils.modRL("fire_bar/bar");
    private static final ResourceLocation CONTAINER_TEXTURE = IMUtils.modRL("fire_bar/container");
    private static final int CONTAINER_WIDTH = 12;
    private static final int CONTAINER_HEIGHT = 34;
    private static final int BAR_HEIGHT = 21;
    private static final int BAR_WIDTH = 6;
    private static final int BAR_POS_IN_CONTAINER_X = 3;
    private static final int BAR_POS_IN_CONTAINER_Y = 11;


    private final GetterAndSetter<Integer> fire;
    private final int maxFire;

    public IMFuelInfoArea(GetterAndSetter<Integer> fire, int maxFire, int xMin, int yMin) {
        super(new Rect2i(xMin, yMin, CONTAINER_WIDTH, CONTAINER_HEIGHT));
        this.fire = fire;
        this.maxFire = maxFire;
    }

    @Override
    protected void fillTooltipOverArea(int mouseX, int mouseY, List<Component> tooltip) {
        if(new Rect2i(this.area.getX() + 3,this.area.getY() + 11,6,21).contains(mouseX,mouseY))
            tooltip.add(Component.translatable("desc.immersive_metallurgy.info.fuel_info", fire.get(), maxFire));
    }

    @Override
    public void draw(GuiGraphics graphics) {
        graphics.pose().pushPose();
        int fireBarH = 0;
        if(fire.get() > 0){
            fireBarH = Math.max(1, (int)(((float)fire.get() / maxFire) * BAR_HEIGHT));
        }

        graphics.blitSprite(
                CONTAINER_TEXTURE,
                area.getX(), area.getY(),
                CONTAINER_WIDTH, CONTAINER_HEIGHT
        );
        graphics.blitSprite(
                BAR_TEXTURE,
                BAR_WIDTH, BAR_HEIGHT,
                0, BAR_HEIGHT- fireBarH,
                area.getX() + BAR_POS_IN_CONTAINER_X, area.getY() + BAR_POS_IN_CONTAINER_Y + BAR_HEIGHT - fireBarH,
                BAR_WIDTH, fireBarH
        );
        graphics.pose().popPose();
    }
}
