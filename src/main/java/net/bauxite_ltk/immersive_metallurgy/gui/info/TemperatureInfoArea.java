package net.bauxite_ltk.immersive_metallurgy.gui.info;

import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class TemperatureInfoArea extends InfoArea {
    private static final ResourceLocation BAR_TEXTURE = IMUtils.modRL("temperature_area/bar");
    private static final ResourceLocation POINTER_TEXTURE = IMUtils.modRL("temperature_area/pointer");
    private static final int BAR_WIDTH = 12;
    private static final int BAR_HEIGHT = 59;
    private static final int POINTER_WIDTH = 12;
    private static final int POINTER_HEIGHT = 4;
    private static final int MAX_TEMPERATURE = 1800;


    private final GetterAndSetter<Integer> temperature;

    public TemperatureInfoArea(GetterAndSetter<Integer> temperature, int xMin, int yMin) {
        super(new Rect2i(xMin, yMin, 12, 59));
        this.temperature = temperature;
    }

    @Override
    protected void fillTooltipOverArea(int mouseX, int mouseY, List<Component> tooltip) {
        tooltip.add(Component.literal(temperature.get() + " °C"));
    }

    @Override
    public void draw(GuiGraphics graphics) {
        graphics.pose().pushPose();
        final int height = area.getHeight()-4;
        int pointerH = height - (int)(((float)temperature.get()/ MAX_TEMPERATURE) * height);
        graphics.blitSprite(
                BAR_TEXTURE,
                area.getX(), area.getY(),
                BAR_WIDTH, BAR_HEIGHT);
        graphics.blitSprite(
                POINTER_TEXTURE,
                area.getX(), area.getY()+pointerH,
                POINTER_WIDTH,POINTER_HEIGHT
        );
        graphics.pose().popPose();
    }
}
