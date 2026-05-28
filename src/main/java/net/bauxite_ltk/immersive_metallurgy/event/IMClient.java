package net.bauxite_ltk.immersive_metallurgy.event;


import blusunrize.immersiveengineering.api.client.ieobj.IEOBJCallbacks;
import net.bauxite_ltk.immersive_metallurgy.Config;
import net.bauxite_ltk.immersive_metallurgy.ImmersiveMetallurgy;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlockEntities;
import net.bauxite_ltk.immersive_metallurgy.block.liquid.CanSolidifyLiquidBlockEntity;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.bauxite_ltk.immersive_metallurgy.callback.CastingChannelCallbacks;
import net.bauxite_ltk.immersive_metallurgy.callback.ElectricCableCallbacks;
import net.bauxite_ltk.immersive_metallurgy.fluid.FluidRendererExtension;
import net.bauxite_ltk.immersive_metallurgy.fluid.IMFluids;
import net.bauxite_ltk.immersive_metallurgy.gui.IMMenuTypes;
import net.bauxite_ltk.immersive_metallurgy.gui.multiblock.BallMillScreen;
import net.bauxite_ltk.immersive_metallurgy.gui.multiblock.FlotationCellScreen;
import net.bauxite_ltk.immersive_metallurgy.gui.multiblock.HydrocycloneScreen;
import net.bauxite_ltk.immersive_metallurgy.gui.multiblock.ThickenerScreen;
import net.bauxite_ltk.immersive_metallurgy.particle.DripSapParticles;
import net.bauxite_ltk.immersive_metallurgy.particle.IMParticleTypes;
import net.bauxite_ltk.immersive_metallurgy.render.*;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;

import java.util.Objects;
import java.util.function.Supplier;

// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ImmersiveMetallurgy.MOD_ID, value = Dist.CLIENT)
public class IMClient {

    public static final ResourceLocation WATER_STILL = ResourceLocation.fromNamespaceAndPath("minecraft","block/water_still");
    public static final ResourceLocation WATER_FLOW = ResourceLocation.fromNamespaceAndPath("minecraft","block/water_flow");

    public static final ResourceLocation BUBBLE_STILL = IMUtils.modRL("block/bubble_still");

    public static final ResourceLocation THICKY_WATER_STILL = IMUtils.modRL("block/thicky_water_still");
    public static final ResourceLocation THICKY_WATER_FLOW = IMUtils.modRL("block/thicky_water_flow");


    private static final ResourceLocation MOLTEN_STILL = IMUtils.modRL("block/molten_still");
    private static final ResourceLocation MOLTEN_FLOW = IMUtils.modRL("block/molten_flow");


    public static void modConstruction(){
        IEOBJCallbacks.register(IMUtils.modRL("electric_cable"), ElectricCableCallbacks.INSTANCE);
        IEOBJCallbacks.register(IMUtils.modRL("casting_channel"), CastingChannelCallbacks.INSTANCE);
        //IEOBJCallbacks.register(IMUtils.modRL("electric_cable_mv"), ElectricCableCallbacks.INSTANCE);
        ImmersiveMetallurgy.LOGGER.info("ImmersiveMetallurgy register callbacks");
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(IMFluids.MASON_PINE_SAP.getSource(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(IMFluids.MASON_PINE_SAP.getFlowing(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(IMFluids.TURPENTINE_OIL.getSource(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(IMFluids.TURPENTINE_OIL.getFlowing(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(IMFluids.TERPINEOL.getSource(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(IMFluids.TERPINEOL.getFlowing(), RenderType.translucent());
    }


    @SubscribeEvent
    public static void registerExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(
                new FluidRendererExtension(0xFFf18579, CanSolidifyLiquidBlockEntity::getColorFromTickRemain, MOLTEN_STILL, MOLTEN_FLOW, null, null),
                IMFluids.MOLTEN_PIG_IRON.getType());

        event.registerFluidType(
                new FluidRendererExtension(0xFFb98051, WATER_STILL, WATER_FLOW, null, null),
                IMFluids.MASON_PINE_SAP.getType());

        event.registerFluidType(
                new FluidRendererExtension(0xFFf3c56c, WATER_STILL, WATER_FLOW, null, null),
                IMFluids.TURPENTINE_OIL.getType());

        event.registerFluidType(
                new FluidRendererExtension(0xFFffcb00, WATER_STILL, WATER_FLOW, null, null),
                IMFluids.TERPINEOL.getType());

        event.registerFluidType(
                new FluidRendererExtension(0xFFaf8a63, MOLTEN_STILL, MOLTEN_FLOW, null, null),
                IMFluids.RAW_IRON_SLURRY.getType());

        event.registerFluidType(
                new FluidRendererExtension(0xFFaf8a63, WATER_STILL, WATER_FLOW, null, null),
                IMFluids.RAW_IRON_PROCESSED_SLURRY.getType());

        event.registerFluidType(
                new FluidRendererExtension(0xFFaf8a63, BUBBLE_STILL, WATER_FLOW, null, null),
                IMFluids.RAW_IRON_CONCENTRATE_SLURRY.getType());

        event.registerFluidType(
                new FluidRendererExtension(0xFFaf8a63, THICKY_WATER_STILL, THICKY_WATER_FLOW, null, null),
                IMFluids.RAW_IRON_TAILING_SLURRY.getType());

    }

    @SubscribeEvent
    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event){
        for (Fluid fluid : BuiltInRegistries.FLUID)
        {
            if (Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(fluid)).getNamespace().equals(IMUtils.MOD_ID))
            {
                event.register(new DynamicFluidContainerModel.Colors(), fluid.getBucket());
            }
        }
    }


    @SubscribeEvent
    public static void registerRenders(EntityRenderersEvent.RegisterRenderers event)
    {
        registerBERenderNoContext(event, IMMultiblockLogic.BALL_MILL.masterBE(), BallMillRender::new);
        registerBERenderNoContext(event, IMMultiblockLogic.FLOTATION_CELL.masterBE(), FlotationCellRender::new);
        registerBERenderNoContext(event, IMMultiblockLogic.THICKENER.masterBE(), ThickenerRender::new);
        registerBERenderNoContext(event, IMBlockEntities.SAP_COLLECTOR.get(), SapCollectorRender::new);
        registerBERenderNoContext(event, IMBlockEntities.CASTING_CHANNEL.get(), CastingChannelBlockEntityRender::new);

        //registerBERenderNoContext(event, IMBlockEntities.ELECTRIC_CABLE.get(), ElectricCableSelectionRenderer::new);
    }


    private static <T extends BlockEntity>
    void registerBERenderNoContext(
            EntityRenderersEvent.RegisterRenderers event, Supplier<BlockEntityType<? extends T>> type, Supplier<BlockEntityRenderer<T>> render
    )
    {
        registerBERenderNoContext(event, type.get(), render);
    }

    private static <T extends BlockEntity>
    void registerBERenderNoContext(
            EntityRenderersEvent.RegisterRenderers event, BlockEntityType<? extends T> type, Supplier<BlockEntityRenderer<T>> render
    )
    {
        event.registerBlockEntityRenderer(type, $ -> render.get());
    }

    @SubscribeEvent
    public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event)
    {
        BallMillRender.BARREL = new IMDynamicModel(BallMillRender.NAME);
        FlotationCellRender.BLADE = new IMDynamicModel(FlotationCellRender.NAME);
        ThickenerRender.AGITATOR = new IMDynamicModel(ThickenerRender.NAME);

    }

    @SubscribeEvent
    public static void registerContainersAndScreens(RegisterMenuScreensEvent event)
    {
        event.register(IMMenuTypes.BALL_MILL.getType(), BallMillScreen::new);
        event.register(IMMenuTypes.FLOTATION_CELL.getType(), FlotationCellScreen::new);
        event.register(IMMenuTypes.HYDROCYCLONE.getType(), HydrocycloneScreen::new);
        event.register(IMMenuTypes.THICKENER.getType(), ThickenerScreen::new);

    }

    @SubscribeEvent
    public static void registerProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(
                IMParticleTypes.DRIPPING_SAP.get(),
                DripSapParticles.Provider::new
        );
    }

    @SubscribeEvent
    public static void onConfigReloading(ModConfigEvent.Reloading event){
        Config.MACHINES.populateAPI();
    }
}
