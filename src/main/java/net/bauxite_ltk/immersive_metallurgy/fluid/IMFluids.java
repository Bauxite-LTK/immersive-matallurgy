package net.bauxite_ltk.immersive_metallurgy.fluid;

import com.google.common.base.Suppliers;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlocks;
import net.bauxite_ltk.immersive_metallurgy.item.IMItems;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.*;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class IMFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, IMUtils.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, IMUtils.MOD_ID);

    public static final FluidHolder<BaseFlowingFluid> MOLTEN_PIG_IRON =
            registerMolten("pig_iron",
                    IMBlocks.MOLTEN_PIG_IRON,
                    IMItems.MOLTEN_PIG_IRON_BUCKET);

    public static final FluidHolder<BaseFlowingFluid> MASON_PINE_SAP =
            register(
                    "mason_pine_sap",
                    properties -> properties
                            .block(IMBlocks.MASON_PINE_SAP)
                            .bucket(IMItems.MASON_PINE_SAP_BUCKET),
                    waterLike()
                            .descriptionId("fluid.immersive_metallurgy.mason_pine_sap"),
                    BaseFlowingFluid.Source::new,
                    BaseFlowingFluid.Flowing::new
            );

    public static final FluidHolder<BaseFlowingFluid> TURPENTINE_OIL =
            register(
                    "turpentine_oil",
                    properties -> properties
                            .block(IMBlocks.TURPENTINE_OIL)
                            .bucket(IMItems.TURPENTINE_OIL_BUCKET),
                    waterLike()
                            .descriptionId("fluid.immersive_metallurgy.turpentine_oil"),
                    BaseFlowingFluid.Source::new,
                    BaseFlowingFluid.Flowing::new
            );

    public static final FluidHolder<BaseFlowingFluid> TERPINEOL =
            register(
                    "terpineol",
                    properties -> properties
                            .block(IMBlocks.TERPINEOL)
                            .bucket(IMItems.TERPINEOL_BUCKET),
                    waterLike()
                            .descriptionId("fluid.immersive_metallurgy.terpineol"),
                    BaseFlowingFluid.Source::new,
                    BaseFlowingFluid.Flowing::new
            );

    public static final FluidHolder<BaseFlowingFluid> RAW_IRON_SLURRY =
            registerOreSlurry("raw_iron",
                    IMBlocks.RAW_IRON_SLURRY,
                    IMItems.RAW_IRON_SLURRY_BUCKET);

    public static final FluidHolder<BaseFlowingFluid> RAW_IRON_PROCESSED_SLURRY =
            registerOreSlurry("raw_iron_processed",
                    IMBlocks.RAW_IRON_PROCESSED_SLURRY,
                    IMItems.RAW_IRON_PROCESSED_SLURRY_BUCKET);

    public static final FluidHolder<BaseFlowingFluid> RAW_IRON_CONCENTRATE_SLURRY =
            registerOreSlurry("raw_iron_concentrate",
                    IMBlocks.RAW_IRON_CONCENTRATE_SLURRY,
                    IMItems.RAW_IRON_CONCENTRATE_SLURRY_BUCKET);

    public static final FluidHolder<BaseFlowingFluid> RAW_IRON_TAILING_SLURRY =
            registerOreSlurry("raw_iron_tailing",
                    IMBlocks.RAW_IRON_TAILING_SLURRY,
                    IMItems.RAW_IRON_TAILING_SLURRY_BUCKET);

    private static FluidType.Properties waterLike()
    {
        return FluidType.Properties.create()
                .adjacentPathType(PathType.WATER)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .canConvertToSource(false)
                .canDrown(true)
                .canExtinguish(true)
                .canHydrate(true)
                .canPushEntity(true)
                .canSwim(true)
                .supportsBoating(true);
    }

    private static FluidType.Properties lavaLike()
    {
        return FluidType.Properties.create()
                .adjacentPathType(PathType.LAVA)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                .lightLevel(15)
                .density(7000)
                .viscosity(6000)
                .temperature(1300)
                .canConvertToSource(false)
                .canDrown(false)
                .canExtinguish(false)
                .canHydrate(false)
                .canPushEntity(false)
                .canSwim(false)
                .supportsBoating(false);
    }


    public static < L extends LiquidBlock, I extends Item>
    FluidHolder<BaseFlowingFluid> registerOreSlurry(
            String oreName, DeferredBlock<L> liquidBlock, DeferredItem<I> bucketItem)
    {
        return register(
                oreName + "_slurry",
                properties -> properties
                        .block(liquidBlock)
                        .bucket(bucketItem),
                waterLike()
                        .descriptionId("fluid.immersive_metallurgy." + oreName + "_slurry"),
                BaseFlowingFluid.Source::new,
                BaseFlowingFluid.Flowing::new);
    }

    public static < L extends LiquidBlock, I extends Item>
    FluidHolder<BaseFlowingFluid> registerMolten(
            String originalName, DeferredBlock<L> liquidBlock, DeferredItem<I> bucketItem)
    {
        return register(
                "molten_" + originalName,
                properties -> properties
                        .block(liquidBlock)
                        .bucket(bucketItem),
                lavaLike()
                        .descriptionId("fluid.immersive_metallurgy." + "molten_" + originalName),
                MoltenFluid.Source::new,
                MoltenFluid.Flowing::new);
    }


    private static <F extends FlowingFluid> FluidHolder<F> register(String name, Consumer<BaseFlowingFluid.Properties> builder, FluidType.Properties typeProperties, Function<BaseFlowingFluid.Properties, F> sourceFactory, Function<BaseFlowingFluid.Properties, F> flowingFactory)
    {
        // Names `metal/foo` to `metal/flowing_foo`
        final int index = name.lastIndexOf('/');
        final String flowingName = index == -1 ? "flowing_" + name : name.substring(0, index) + "/flowing_" + name.substring(index + 1);

        return registerFluid(FLUID_TYPES, FLUIDS, name, name, flowingName, builder, () -> new FluidType(typeProperties), sourceFactory, flowingFactory);
    }

    public static <F extends FlowingFluid> FluidHolder<F> registerFluid(
            DeferredRegister<FluidType> fluidTypes,
            DeferredRegister<Fluid> fluids,
            String typeName,
            String sourceName,
            String flowingName,
            Consumer<BaseFlowingFluid.Properties> builder,
            Supplier<FluidType> typeFactory,
            Function<BaseFlowingFluid.Properties, F> sourceFactory,
            Function<BaseFlowingFluid.Properties, F> flowingFactory)
    {
        // The type need a reference to both source and flowing
        // In addition, the properties' builder cannot be invoked statically, as it has hard references to registry objects, which may not be populated based on class load order - it must be invoked at registration time.
        // So, first we prepare the source and flowing registry objects, referring to the properties box (which will be opened during registration, which is ok)
        // Then, we populate the properties box lazily, (since it's a mutable lazy), so the properties inside are only constructed when the box is opened (again, during registration)
        final Mutable<Supplier<BaseFlowingFluid.Properties>> typeBox = new MutableObject<>();
        final DeferredHolder<Fluid, F> source = fluids.register(sourceName, () -> sourceFactory.apply(typeBox.getValue().get()));
        final DeferredHolder<Fluid, F> flowing = fluids.register(flowingName, () -> flowingFactory.apply(typeBox.getValue().get()));

        final DeferredHolder<FluidType, FluidType> fluidType = fluidTypes.register(typeName, typeFactory);

        typeBox.setValue(Suppliers.memoize(() -> {
            final BaseFlowingFluid.Properties lazyProperties = new BaseFlowingFluid.Properties(fluidType, source, flowing);
            builder.accept(lazyProperties);
            return lazyProperties;
        }));

        return new FluidHolder<>(fluidType, flowing, source);
    }

    public static void init(IEventBus modEventBus){
        FLUID_TYPES.register(modEventBus);
        FLUIDS.register(modEventBus);
    }
}
