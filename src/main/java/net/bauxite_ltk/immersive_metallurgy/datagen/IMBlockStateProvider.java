package net.bauxite_ltk.immersive_metallurgy.datagen;

import net.bauxite_ltk.immersive_metallurgy.block.IMBlocks;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class IMBlockStateProvider extends BlockStateProvider {

    public IMBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, IMUtils.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(IMBlocks.MASON_PINE_PLANKS);

        logBlock(IMBlocks.MASON_PINE_LOG.get());
        logBlock(IMBlocks.STRIPPED_MASON_PINE_LOG.get());

        axisBlock(IMBlocks.MASON_PINE_LOG_LIVE.get(),
                blockTexture(IMBlocks.MASON_PINE_LOG.get()),
                blockTexture(IMBlocks.MASON_PINE_LOG.get()).withSuffix("_top")
                );

        axisBlock(IMBlocks.MASON_PINE_LOG_SAPPY.get(),
                blockTexture(IMBlocks.MASON_PINE_LOG_SAPPY.get()),
                blockTexture(IMBlocks.MASON_PINE_LOG.get()).withSuffix("_top")
        );


        axisBlock(IMBlocks.MASON_PINE_WOOD.get(),
                blockTexture(IMBlocks.MASON_PINE_LOG.get()),
                blockTexture(IMBlocks.MASON_PINE_LOG.get())
        );

        axisBlock(IMBlocks.STRIPPED_MASON_PINE_WOOD.get(),
                blockTexture(IMBlocks.STRIPPED_MASON_PINE_LOG.get()),
                blockTexture(IMBlocks.STRIPPED_MASON_PINE_LOG.get())
        );

        blockItem(IMBlocks.MASON_PINE_LOG);
        blockItem(IMBlocks.MASON_PINE_LOG_LIVE);
        blockItem(IMBlocks.MASON_PINE_LOG_SAPPY);
        blockItem(IMBlocks.MASON_PINE_WOOD);
        blockItem(IMBlocks.STRIPPED_MASON_PINE_LOG);
        blockItem(IMBlocks.STRIPPED_MASON_PINE_WOOD);

        leavesBlock(IMBlocks.MASON_PINE_LEAVES);
        saplingBlock(IMBlocks.MASON_PINE_SAPLING);

    }

    private void saplingBlock(DeferredBlock<SaplingBlock> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void leavesBlock(DeferredBlock<LeavesBlock> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(), ResourceLocation.parse("minecraft:block/leaves"),
                        "all", blockTexture(blockRegistryObject.get())));
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("immersive_metallurgy:block/" + deferredBlock.getId().getPath()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock, String appendix) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("immersive_metallurgy:block/" + deferredBlock.getId().getPath() + appendix));
    }
}
