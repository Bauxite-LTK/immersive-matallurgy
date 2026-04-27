package net.bauxite_ltk.immersive_metallurgy.block.multiblock;

import blusunrize.immersiveengineering.api.multiblocks.ClientMultiblocks;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;

import javax.annotation.Nullable;
import java.util.*;

public class IMMultiblockProperties implements ClientMultiblocks.MultiblockManualData{

    /** Including <code>null</code> at the last index */
    static final Direction[] DIRECTIONS;
    static{
        Direction[] source = Direction.values();
        Direction[] destination = new Direction[source.length + 1];
        System.arraycopy(source, 0, destination, 0, source.length);
        destination[destination.length - 1] = null;
        DIRECTIONS = destination;
    }

    private final IETemplateMultiblock multiblock;
    @Nullable
    private NonNullList<ItemStack> materials;
    private final ItemStack renderStack;
    @Nullable
    private final Vec3 renderOffset;

    public IMMultiblockProperties(IETemplateMultiblock multiblock){
        this(multiblock, null);
    }

    public IMMultiblockProperties(IETemplateMultiblock multiblock, double offX, double offY, double offZ){
        this(multiblock, new Vec3(offX, offY, offZ));
    }

    private IMMultiblockProperties(IETemplateMultiblock multiblock, @Nullable Vec3 renderOffset){
        this.multiblock = multiblock;
        this.renderStack = new ItemStack(multiblock.getBlock());
        this.renderOffset = renderOffset;
    }

    /** Skipping normal rendering behaviour */
    protected boolean usingCustomRendering(){
        return false;
    }

    @Override
    public NonNullList<ItemStack> getTotalMaterials(){
        if(this.materials != null)
            return this.materials;

        //@formatter:off
        final List<StructureTemplate.StructureBlockInfo> structure = this.multiblock.getStructure(Minecraft.getInstance().level);
        this.materials = structure.stream()
                .map(info -> new ItemStack(info.state().getBlock().asItem(), 1))
                .collect(HashMap<Item, Integer>::new, (map, stack) -> map.compute(stack.getItem(), (item, count) -> count == null ? 1 : count + 1), HashMap::putAll)
                .entrySet().stream()
                .map(e -> new ItemStack(e.getKey(), e.getValue()))
                .collect(NonNullList::create, AbstractList::add, AbstractCollection::addAll);
        //@formatter:on

        return this.materials;
    }

    @Override
    public boolean canRenderFormedStructure(){
        return this.renderOffset != null;
    }

    /** Allowing custom accessories to be rendered. Unused if {@link #usingCustomRendering()} returns true */
    public void renderExtras(PoseStack matrix, MultiBufferSource buffer){
    }

    /** Only used when {@link #usingCustomRendering()} returns true */
    public void renderCustomFormedStructure(PoseStack matrix, MultiBufferSource buffer){
    }

    static final RandomSource RANDOM_SOURCE = RandomSource.create();
    @Override
    public final void renderFormedStructure(PoseStack matrix, MultiBufferSource buffer){
        Objects.requireNonNull(this.renderOffset);

        if(usingCustomRendering()){
            renderCustomFormedStructure(matrix, buffer);
            return;
        }

        matrix.pushPose();
        {
            matrix.translate(this.renderOffset.x - 0.5, this.renderOffset.y - 0.5, this.renderOffset.z - 0.5);

            matrix.pushPose();
            {
                PoseStack.Pose last = matrix.last();

                BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(this.renderStack, null, null, 0);

                VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.cutout());
                for(Direction direction: DIRECTIONS){
                    RANDOM_SOURCE.setSeed(42L);
                    List<BakedQuad> quads = bakedmodel.getQuads(null, direction, RANDOM_SOURCE, ModelData.EMPTY, null);
                    for(BakedQuad quad: quads){
                        vertexConsumer.putBulkData(last, quad, 1.0F, 1.0F, 1.0F, 1.0F, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
                    }
                }
            }
            matrix.popPose();

            matrix.pushPose();
            {
                renderExtras(matrix, buffer);
            }
            matrix.popPose();
        }
        matrix.popPose();
    }
}
