package net.bauxite_ltk.immersive_metallurgy.datagen;

import net.bauxite_ltk.immersive_metallurgy.block.IMBlocks;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.IMMultiblockLogic;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.BallMillLogic;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Set;
import java.util.function.BiConsumer;

public class IMBlockLootTableProvider extends BlockLootSubProvider {

    protected IMBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(IMBlocks.ELECTRIC_CABLE_LV.get());
        dropSelf(IMBlocks.ELECTRIC_CABLE_MV.get());
        dropSelf(IMBlocks.MASON_PINE_LOG.get());
        dropOther(IMBlocks.MASON_PINE_LOG_LIVE.get(), IMBlocks.MASON_PINE_LOG.get());
        dropOther(IMBlocks.MASON_PINE_LOG_SAPPY.get(), IMBlocks.MASON_PINE_LOG.get());
        dropSelf(IMBlocks.MASON_PINE_WOOD.get());
        dropSelf(IMBlocks.STRIPPED_MASON_PINE_LOG.get());
        dropSelf(IMBlocks.STRIPPED_MASON_PINE_WOOD.get());
        dropSelf(IMBlocks.MASON_PINE_PLANKS.get());
        dropSelf(IMBlocks.MASON_PINE_SAPLING.get());
        dropSelf(IMBlocks.SAP_COLLECTOR.get());
        add(IMBlocks.MASON_PINE_LEAVES.get(),
                createLeavesDrops(IMBlocks.MASON_PINE_SAPLING.get(),
                        IMBlocks.MASON_PINE_SAPLING.get(),
                        NORMAL_LEAVES_SAPLING_CHANCES));


    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return IMBlocks.BLOCKS.getEntries().stream().map(Holder::value).filter(IMBlockLootTableProvider::isExceptedFromDataGen)::iterator;
    }

    private static boolean isExceptedFromDataGen(Block block){
        return true;
    }
}
