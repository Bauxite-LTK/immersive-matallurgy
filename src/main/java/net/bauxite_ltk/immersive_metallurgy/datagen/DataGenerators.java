package net.bauxite_ltk.immersive_metallurgy.datagen;

import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = IMUtils.MOD_ID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(
                event.includeServer(),
                new LootTableProvider(
                        packOutput, Collections.emptySet(),
                        List.of(new LootTableProvider.SubProviderEntry(IMBlockLootTableProvider::new, LootContextParamSets.BLOCK)),
                        lookupProvider
                )
        );

        BlockTagsProvider blockTagsProvider = new IMBlockTagProvider(packOutput,lookupProvider,existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new IMItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));

        generator.addProvider(event.includeClient(), new IMItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new IMBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new IMDatapackProvider(packOutput, lookupProvider));
    }
}
