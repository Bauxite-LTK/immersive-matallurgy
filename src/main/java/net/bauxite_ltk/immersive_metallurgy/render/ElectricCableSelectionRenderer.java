package net.bauxite_ltk.immersive_metallurgy.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bauxite_ltk.immersive_metallurgy.block.metal.ElectricCableBlockEntity;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Deprecated
public class ElectricCableSelectionRenderer implements BlockEntityRenderer<ElectricCableBlockEntity> {

    private static final ResourceLocation HIGHLIGHT_TEXTURE = IMUtils.modRL("textures/electric_cable_lv.png");

    public static class ClientData {
        private static BlockPos targetedBlockPos = null;
        private static Direction targetedBlockFace = null;
        private static ItemStack heldItem = ItemStack.EMPTY;

        public static void setTargetedBlock(BlockPos pos, Direction face) {
            targetedBlockPos = pos;
            targetedBlockFace = face;
        }

        public static void clearTargetedBlock() {
            targetedBlockPos = null;
            targetedBlockFace = null;
        }

        public static BlockPos getTargetedBlockPos() { return targetedBlockPos; }
        public static Direction getTargetedBlockFace() { return targetedBlockFace; }
        public static ItemStack getHeldItem() { return heldItem; }
        public static void setHeldItem(ItemStack item) { heldItem = item; }
    }

    @Override
    public void render(ElectricCableBlockEntity electricCableBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        BlockPos targetPos = ClientData.getTargetedBlockPos();
        Direction targetFace = ClientData.getTargetedBlockFace();
        ItemStack heldItem = ClientData.getHeldItem();

        // 检查条件：手持特定物品且对准此方块
        if (targetPos != null && targetFace != null &&
                electricCableBlockEntity.getBlockPos().equals(targetPos) &&
                heldItem.is(Items.STICK)) {

            poseStack.pushPose();

            // 计算渲染位置（轻微偏移避免z-fighting）
            float offset = 0.001f;
            renderFaceOverlay(poseStack, multiBufferSource, targetFace, offset, i, i1);

            poseStack.popPose();
        }
    }

    private void renderFaceOverlay(PoseStack poseStack, MultiBufferSource bufferSource,
                                   Direction face, float offset, int packedLight, int packedOverlay) {
        VertexConsumer vertexConsumer = bufferSource.getBuffer(
                RenderType.entityTranslucent(HIGHLIGHT_TEXTURE)
        );

        // 根据面的方向计算顶点位置
        float minX = 0, maxX = 1;
        float minY = 0, maxY = 1;
        float minZ = 0, maxZ = 1;

        // 根据面对应的方向调整坐标
        switch (face) {
            case DOWN -> { minY = offset; maxY = offset; }
            case UP -> { minY = 1 - offset; maxY = 1 - offset; }
            case NORTH -> { minZ = offset; maxZ = offset; }
            case SOUTH -> { minZ = 1 - offset; maxZ = 1 - offset; }
            case WEST -> { minX = offset; maxX = offset; }
            case EAST -> { minX = 1 - offset; maxX = 1 - offset; }
        }

        // 创建面顶点
        Matrix4f matrix = poseStack.last().pose();
        PoseStack.Pose normal = poseStack.last();

        // 根据方向计算法线
        Vector3f normalVec = face.step();

        // 添加顶点（两个三角形组成一个面）
        addVertex(vertexConsumer, matrix, normal, minX, minY, minZ, 0, 0, normalVec, packedLight, packedOverlay);
        addVertex(vertexConsumer, matrix, normal, maxX, minY, minZ, 1, 0, normalVec, packedLight, packedOverlay);
        addVertex(vertexConsumer, matrix, normal, maxX, maxY, minZ, 1, 1, normalVec, packedLight, packedOverlay);
        addVertex(vertexConsumer, matrix, normal, minX, maxY, minZ, 0, 1, normalVec, packedLight, packedOverlay);
    }

    private void addVertex(VertexConsumer consumer, Matrix4f matrix, PoseStack.Pose normal,
                           float x, float y, float z, float u, float v,
                           Vector3f normalVec, int packedLight, int packedOverlay) {
        int lightU = packedLight & 0xFFFF;
        int lightV = (packedLight >> 16) & 0xFFFF;


        consumer.addVertex(matrix, x, y, z)
                .setColor(255, 255, 255, 128) // 半透明白色
                .setUv(u, v)
                //.setUv1(lightU,lightV)
                .setNormal(normal, normalVec.x(), normalVec.y(), normalVec.z())
                .setLight(packedLight)
                .setOverlay(packedOverlay);
    }
}
