package net.bauxite_ltk.immersive_metallurgy.render;

import blusunrize.immersiveengineering.api.ApiUtils;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = IMUtils.MOD_ID, value = Dist.CLIENT)
public class IMDynamicModel
{
    private static final List<ModelResourceLocation> MODELS = new ArrayList<>();

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional ev)
    {
        for(ModelResourceLocation model : MODELS)
            // TODO check if this works
            ev.register(model);
    }

    private final ModelResourceLocation name;

    public IMDynamicModel(String desc)
    {
        // TODO does this work?
        this.name = new ModelResourceLocation(
                IMUtils.modRL("dynamic/"+desc),
                "standalone");
        MODELS.add(this.name);
    }

    public BakedModel get()
    {
        final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        return blockRenderer.getBlockModelShaper().getModelManager().getModel(name);
    }

    public List<BakedQuad> getNullQuads()
    {
        return getNullQuads(ModelData.EMPTY);
    }

    public List<BakedQuad> getNullQuads(ModelData data)
    {
        return get().getQuads(null, null, ApiUtils.RANDOM_SOURCE, data, null);
    }

    public ResourceLocation getName()
    {
        return name.id();
    }

    public ModelResourceLocation getModelResourceLocation()
    {
        return name;
    }
}
