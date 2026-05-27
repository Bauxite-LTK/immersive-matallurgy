package net.bauxite_ltk.immersive_metallurgy.worldgen;

import net.bauxite_ltk.immersive_metallurgy.block.IMBlocks;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.bauxite_ltk.immersive_metallurgy.worldgen.tree.MasonPineFoliagePlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.PineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;

public class IMConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?,?>> MASON_PINE_KEY = registerKey("mason_pine");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?,?>> context){

        register(context, MASON_PINE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(IMBlocks.MASON_PINE_LOG_LIVE.get()),
                        new StraightTrunkPlacer(10, 5, 0),
                        BlockStateProvider.simple(IMBlocks.MASON_PINE_LEAVES.get()),
                        new MasonPineFoliagePlacer(ConstantInt.of(1), ConstantInt.of(2), UniformInt.of(7,11)),
                        new TwoLayersFeatureSize(2,0,2)
                ).build()
        );
    }

    public static ResourceKey<ConfiguredFeature<?,?>> registerKey(String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, IMUtils.modRL(name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>>
    void register(BootstrapContext<ConfiguredFeature<?,?>> context,
                  ResourceKey<ConfiguredFeature<?,?>> key, F feature, FC configuration){
        context.register(key, new ConfiguredFeature<>(feature,configuration));
    }
}
