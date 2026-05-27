package net.bauxite_ltk.immersive_metallurgy.worldgen.tree;

import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class IMTreePlacerTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS =
            DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, IMUtils.MOD_ID);

    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<MasonPineFoliagePlacer>>
            MASON_PINE_FOLIAGE_PLACER = FOLIAGE_PLACERS.register("mason_pine_foliage_placer", () -> new FoliagePlacerType<>(MasonPineFoliagePlacer.CODEC));

    public static final void init(IEventBus modEventBus){
        FOLIAGE_PLACERS.register(modEventBus);
    }
}
