package net.bauxite_ltk.immersive_metallurgy.datagen;


import net.bauxite_ltk.immersive_metallurgy.block.IMBlocks;
import net.bauxite_ltk.immersive_metallurgy.item.IMItems;
import net.bauxite_ltk.immersive_metallurgy.tags.IMTags;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class IMItemTagProvider extends ItemTagsProvider {
    public IMItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, IMUtils.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(IMTags.Items.PURE_ORE_CHUNKS).add(IMItems.Ores.RAW_IRON_ORE_CHUNK.get());
        tag(IMTags.Items.PURE_COARSE_POWDERS).add(IMItems.Ores.RAW_IRON_ORE_COARSE_POWDER.get());
        tag(IMTags.Items.ORE_FINES).add(IMItems.Ores.RAW_IRON_ORE_FINES.get());
        tag(IMTags.Items.CONCENTRATE_PELLETS).add(IMItems.Ores.RAW_IRON_CONCENTRATE_PELLET.get());

        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(IMBlocks.MASON_PINE_LOG.get().asItem())
                .add(IMBlocks.MASON_PINE_LOG_LIVE.get().asItem())
                .add(IMBlocks.MASON_PINE_LOG_SAPPY.get().asItem())
                .add(IMBlocks.MASON_PINE_WOOD.get().asItem())
                .add(IMBlocks.STRIPPED_MASON_PINE_LOG.get().asItem())
                .add(IMBlocks.STRIPPED_MASON_PINE_WOOD.get().asItem());

        this.tag(ItemTags.LOGS)
                .add(IMBlocks.MASON_PINE_LOG.get().asItem())
                .add(IMBlocks.MASON_PINE_LOG_LIVE.get().asItem())
                .add(IMBlocks.MASON_PINE_LOG_SAPPY.get().asItem())
                .add(IMBlocks.MASON_PINE_WOOD.get().asItem())
                .add(IMBlocks.STRIPPED_MASON_PINE_LOG.get().asItem())
                .add(IMBlocks.STRIPPED_MASON_PINE_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS)
                .add(IMBlocks.MASON_PINE_PLANKS.get().asItem());

        this.tag(ItemTags.SAPLINGS).add(IMBlocks.MASON_PINE_SAPLING.asItem());
    }
}
