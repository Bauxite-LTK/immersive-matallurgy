package net.bauxite_ltk.immersive_metallurgy.gui.info;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import blusunrize.immersiveengineering.client.utils.GuiHelper;
import blusunrize.immersiveengineering.common.fluids.PotionFluid;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import org.joml.Matrix4f;

import java.util.List;
import java.util.function.Consumer;

import static blusunrize.immersiveengineering.api.client.TextUtils.applyFormat;
import static blusunrize.immersiveengineering.client.ClientUtils.mc;

public class FlotationCellAddFluidInfoArea extends InfoArea {
    private final IFluidTank tank;
    private final Rect2i area;
    private final int xOff;
    private final int yOff;
    private final int overlayWidth;
    private final int overlayHeight;
    private final ResourceLocation overlayTexture;

    public FlotationCellAddFluidInfoArea(IFluidTank tank, Rect2i area, int xOff, int yOff, int overlayWidth, int overlayHeight, ResourceLocation overlayTexture) {
        super(area);
        this.tank = tank;
        this.area = area;
        this.xOff = xOff;
        this.yOff = yOff;
        this.overlayWidth = overlayWidth;
        this.overlayHeight = overlayHeight;
        this.overlayTexture = overlayTexture;
    }

    @Override
    public void fillTooltipOverArea(int mouseX, int mouseY, List<Component> tooltip)
    {
        fillTooltip(tank.getFluid(), tank.getCapacity(), tooltip::add);
    }

    public static void fillTooltip(FluidStack fluid, int tankCapacity, Consumer<Component> tooltip)
    {

        if(!fluid.isEmpty())
            tooltip.accept(applyFormat(
                    fluid.getHoverName(),
                    fluid.getFluid().getFluidType().getRarity(fluid).color()
            ));
        else
            tooltip.accept(Component.translatable("gui.immersiveengineering.empty"));
        if(fluid.getFluid() instanceof PotionFluid potion)
            potion.addInformation(fluid, tooltip);

        if(mc().options.advancedItemTooltips&&!fluid.isEmpty())
        {
            if(!Screen.hasShiftDown())
                tooltip.accept(Component.translatable(Lib.DESC_INFO+"holdShiftForInfo"));
            else
            {
                //TODO translation keys
                tooltip.accept(applyFormat(Component.literal("Fluid Registry: "+ BuiltInRegistries.FLUID.getKey(fluid.getFluid())), ChatFormatting.DARK_GRAY));
                tooltip.accept(applyFormat(Component.literal("Density: "+fluid.getFluid().getFluidType().getDensity(fluid)), ChatFormatting.DARK_GRAY));
                tooltip.accept(applyFormat(Component.literal("Temperature: "+fluid.getFluid().getFluidType().getTemperature(fluid)), ChatFormatting.DARK_GRAY));
                tooltip.accept(applyFormat(Component.literal("Viscosity: "+fluid.getFluid().getFluidType().getViscosity(fluid)), ChatFormatting.DARK_GRAY));
            }
        }

        if(tankCapacity > 0)
            tooltip.accept(applyFormat(Component.literal(fluid.getAmount()+"/"+tankCapacity+"mB"), ChatFormatting.GRAY));
        else if(tankCapacity==0)
            tooltip.accept(applyFormat(Component.literal(fluid.getAmount()+"mB"), ChatFormatting.GRAY));
        //don't display amount for tankCapacity < 0, i.e. for ghost fluid stacks
    }


    @Override
    public void draw(GuiGraphics graphics)
    {
        FluidStack fluid = tank.getFluid();
        float capacity = tank.getCapacity();
        graphics.pose().pushPose();
        MultiBufferSource.BufferSource buffer = graphics.bufferSource();
        if(!fluid.isEmpty())
        {
            int fluidHeight = (int)(area.getHeight()*(fluid.getAmount()/capacity));
            // TODO broken?
            GuiHelper.drawRepeatedFluidSpriteGui(buffer, graphics.pose(), fluid, area.getX(), area.getY()+area.getHeight()-fluidHeight, area.getWidth(), fluidHeight);
        }
        VertexConsumer builder = buffer.getBuffer(RenderType.guiOverlay());
        TextureAtlasSprite sprite = mc().getGuiSprites().getSprite(overlayTexture);

        RenderSystem.setShaderTexture(0, sprite.atlasLocation());
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        drawRepeatedSprite(builder, graphics.pose(),
                area.getX()+xOff, area.getY()+yOff, overlayWidth, overlayHeight,
                sprite.contents().width(), sprite.contents().height(),
                sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(),
                0.5f,0.5f,0.5f,1f);
        RenderSystem.disableBlend();
        graphics.pose().popPose();
    }

    public static void drawRepeatedSprite(VertexConsumer builder, PoseStack transform, float x, float y, float w,
                                          float h, int iconWidth, int iconHeight, float uMin, float uMax, float vMin, float vMax,
                                          float r, float g, float b, float alpha)
    {
        int iterMaxW = (int)(w/iconWidth);
        int iterMaxH = (int)(h/iconHeight);
        float leftoverW = w%iconWidth;
        float leftoverH = h%iconHeight;
        float leftoverWf = leftoverW/(float)iconWidth;
        float leftoverHf = leftoverH/(float)iconHeight;
        float iconUDif = uMax-uMin;
        float iconVDif = vMax-vMin;
        for(int ww = 0; ww < iterMaxW; ww++)
        {
            for(int hh = 0; hh < iterMaxH; hh++)
                drawTexturedColoredRect(builder, transform, x+ww*iconWidth, y+hh*iconHeight, iconWidth, iconHeight,
                        r, g, b, alpha, uMin, uMax, vMin, vMax);
            drawTexturedColoredRect(builder, transform, x+ww*iconWidth, y+iterMaxH*iconHeight, iconWidth, leftoverH,
                    r, g, b, alpha, uMin, uMax, vMin, (vMin+iconVDif*leftoverHf));
        }
        if(leftoverW > 0)
        {
            for(int hh = 0; hh < iterMaxH; hh++)
                drawTexturedColoredRect(builder, transform, x+iterMaxW*iconWidth, y+hh*iconHeight, leftoverW, iconHeight,
                        r, g, b, alpha, uMin, (uMin+iconUDif*leftoverWf), vMin, vMax);
            drawTexturedColoredRect(builder, transform, x+iterMaxW*iconWidth, y+iterMaxH*iconHeight, leftoverW, leftoverH,
                    r, g, b, alpha, uMin, (uMin+iconUDif*leftoverWf), vMin, (vMin+iconVDif*leftoverHf));
        }
    }



    public static void drawTexturedColoredRect(
            VertexConsumer builder, PoseStack transform,
            float x, float y, float w, float h,
            float r, float g, float b, float alpha,
            float u0, float u1, float v0, float v1
    ) {
        Matrix4f matrix = transform.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        // 简化版本，直接使用传入的builder
        bufferbuilder.addVertex(matrix, x, y + h, 0).setUv(u0,v1);
        bufferbuilder.addVertex(matrix, x + w, y + h, 0).setUv(u1,v1);
        bufferbuilder.addVertex(matrix, x + w, y, 0).setUv(u1,v0);
        bufferbuilder.addVertex(matrix, x, y, 0).setUv(u0,v0);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
    }
}
