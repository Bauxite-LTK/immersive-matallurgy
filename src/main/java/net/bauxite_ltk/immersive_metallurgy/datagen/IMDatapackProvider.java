package net.bauxite_ltk.immersive_metallurgy.datagen;

import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.bauxite_ltk.immersive_metallurgy.worldgen.IMBiomeModifiers;
import net.bauxite_ltk.immersive_metallurgy.worldgen.IMConfiguredFeatures;
import net.bauxite_ltk.immersive_metallurgy.worldgen.IMPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class IMDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, IMConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, IMPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, IMBiomeModifiers::bootstrap);

    public IMDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(IMUtils.MOD_ID));
    }
}
