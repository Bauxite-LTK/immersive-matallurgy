package net.bauxite_ltk.immersive_metallurgy.event;


import blusunrize.immersiveengineering.api.client.ieobj.IEOBJCallbacks;
import net.bauxite_ltk.immersive_metallurgy.Config;
import net.bauxite_ltk.immersive_metallurgy.ImmersiveMetallurgy;
import net.bauxite_ltk.immersive_metallurgy.block.liquid.CanSolidifyLiquidBlockEntity;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.bauxite_ltk.immersive_metallurgy.callback.ElectricCableCallbacks;
import net.bauxite_ltk.immersive_metallurgy.fluid.FluidRendererExtension;
import net.bauxite_ltk.immersive_metallurgy.fluid.IMFluids;
import net.bauxite_ltk.immersive_metallurgy.gui.IMMenuTypes;
import net.bauxite_ltk.immersive_metallurgy.gui.multiblock.BallMillScreen;
import net.bauxite_ltk.immersive_metallurgy.gui.multiblock.FlotationCellScreen;
import net.bauxite_ltk.immersive_metallurgy.gui.multiblock.HydrocycloneScreen;
import net.bauxite_ltk.immersive_metallurgy.gui.multiblock.ThickenerScreen;
import net.bauxite_ltk.immersive_metallurgy.render.BallMillRender;
import net.bauxite_ltk.immersive_metallurgy.render.FlotationCellRender;
import net.bauxite_ltk.immersive_metallurgy.render.IMDynamicModel;
import net.bauxite_ltk.immersive_metallurgy.render.ThickenerRender;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
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
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;

import java.util.Objects;
import java.util.function.Supplier;

// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ImmersiveMetallurgy.MOD_ID, value = Dist.CLIENT)
public class IMClient {

    private static final ResourceLocation MOLTEN_STILL = IMUtils.modRL("block/molten_still");
    private static final ResourceLocation MOLTEN_FLOW = IMUtils.modRL("block/molten_flow");


    public static void modConstruction(){
        IEOBJCallbacks.register(IMUtils.modRL("electric_cable_lv"), ElectricCableCallbacks.INSTANCE);
        ImmersiveMetallurgy.LOGGER.info("ImmersiveMetallurgy register callbacks");
    }


    @SubscribeEvent
    public static void registerExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(
                new FluidRendererExtension(0xFFf18579, CanSolidifyLiquidBlockEntity::getColorFromTickRemain, MOLTEN_STILL, MOLTEN_FLOW, null, null),
                IMFluids.MOLTEN_PIG_IRON.getType());

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
        registerBERenderNoContext(event, IMMultiblockLogic.THICKENER.masterBE(), ThickenerRender::new);}

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
    public static void onConfigReloading(ModConfigEvent.Reloading event){
        Config.MACHINES.populateAPI();
    }
}
