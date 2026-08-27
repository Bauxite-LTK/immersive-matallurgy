package net.bauxite_ltk.immersive_metallurgy.render;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.MultiblockOrientation;
import blusunrize.immersiveengineering.client.render.tile.BERenderUtils;
import blusunrize.immersiveengineering.client.render.tile.IEMultiblockRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.BallMillLogic;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.ContinuousCastingMachineLogic;
import net.bauxite_ltk.immersive_metallurgy.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class ContinuousCastingMachineRenderer extends IEMultiblockRenderer<ContinuousCastingMachineLogic.State> {
    public static final String NAME = "continuous_casting_machine_metal";
    public static IMDynamicModel METAL;


    @Override
    public void render(@NotNull IMultiblockContext<ContinuousCastingMachineLogic.State> ctx, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = METAL.get();
        final MultiblockOrientation orientation = ctx.getLevel().getOrientation();

        boolean active = ctx.getState().shouldRenderActive();
        if(!active) return;
        if(ctx.getState().tanks.inputMetal().getFluidAmount() <= 0) return;

        matrixStack.pushPose();

        //rotateForFacing(matrixStack, orientation.front());
        matrixStack.translate(0.5, 0.5, 0.5);
        rotateForFacingNoCentering(matrixStack, orientation.front());
        matrixStack.translate(-0.5, -0.5, -0.5);


        bufferIn = BERenderUtils.mirror(orientation, matrixStack, bufferIn);
        VertexConsumer buffer = bufferIn.getBuffer(RenderType.solid());

        blockRenderer.getModelRenderer().renderModel(
                matrixStack.last(), buffer, null, model,
                1, 1, 1,
                255, combinedOverlayIn, ModelData.EMPTY, RenderType.solid()
        );

        matrixStack.popPose();
    }
}
