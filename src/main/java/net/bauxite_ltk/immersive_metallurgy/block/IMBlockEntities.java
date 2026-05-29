package net.bauxite_ltk.immersive_metallurgy.block;

import com.google.common.collect.ImmutableSet;
import net.bauxite_ltk.immersive_metallurgy.block.liquid.CanSolidifyLiquidBlockEntity;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.ElectricCableBlockEntity;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.casting_channel.CastingChannelBlockEntity;
import net.bauxite_ltk.immersive_metallurgy.block.sapCollector.SapCollectorBlockEntity;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class IMBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, IMUtils.MOD_ID
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CanSolidifyLiquidBlockEntity>> CAN_SOLIDIFY_LIQUID = BLOCK_ENTITIES.register(
            "can_solidify_liquid", makeType(CanSolidifyLiquidBlockEntity::new, IMBlocks.MOLTEN_PIG_IRON));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SapCollectorBlockEntity>> SAP_COLLECTOR = BLOCK_ENTITIES.register(
            "sap_collector", makeType(SapCollectorBlockEntity::new, IMBlocks.SAP_COLLECTOR)
    );


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricCableBlockEntity>> ELECTRIC_CABLE_LV = BLOCK_ENTITIES.register(
            "electric_cable_lv", makeType(ElectricCableBlockEntity::forLv, IMBlocks.ELECTRIC_CABLE_LV)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricCableBlockEntity>> ELECTRIC_CABLE_MV = BLOCK_ENTITIES.register(
            "electric_cable_mv", makeType(ElectricCableBlockEntity::forMv, IMBlocks.ELECTRIC_CABLE_MV)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CastingChannelBlockEntity>> CASTING_CHANNEL = BLOCK_ENTITIES.register(
            "casting_channel", makeType(CastingChannelBlockEntity::new, IMBlocks.CASTING_CHANNEL)
    );


    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> makeType(BlockEntityType.BlockEntitySupplier<T> create, Supplier<? extends Block> valid)
    {
        return makeTypeMultipleBlocks(create, ImmutableSet.of(valid));
    }

    @SafeVarargs
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> makeTypeMultipleBlocks(
            BlockEntityType.BlockEntitySupplier<T> create, Collection<? extends Supplier<? extends Block>>... valid
    )
    {
        return () -> new BlockEntityType<>(
                create,
                Arrays.stream(valid)
                        .flatMap(Collection::stream)
                        .map(Supplier::get)
                        .collect(Collectors.toSet()),
                null
        );
    }

    public static void init(IEventBus modEventBus){
        BLOCK_ENTITIES.register(modEventBus);
    }
}
