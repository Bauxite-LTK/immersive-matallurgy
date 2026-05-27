package net.bauxite_ltk.immersive_metallurgy.worldgen;

import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class IMPlacedFeatures {

    public static void bootstrap(BootstrapContext<PlacedFeature> context){
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
    }

    public static ResourceKey<PlacedFeature> registerKey(String name){
        return ResourceKey.create(Registries.PLACED_FEATURE, IMUtils.modRL(name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>>
    void register(BootstrapContext<PlacedFeature> context,
                  ResourceKey<PlacedFeature> key,
                  Holder<ConfiguredFeature<?,?>> configuration,
                  List<PlacementModifier> modifiers){
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
