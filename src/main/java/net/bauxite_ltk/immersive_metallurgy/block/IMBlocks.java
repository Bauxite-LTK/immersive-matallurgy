package net.bauxite_ltk.immersive_metallurgy.block;

import blusunrize.immersiveengineering.common.blocks.BlockItemIE;
import net.bauxite_ltk.immersive_metallurgy.block.liquid.CanSolidifyLiquidBlock;
import net.bauxite_ltk.immersive_metallurgy.block.metal.ElectricCableBlock;
import net.bauxite_ltk.immersive_metallurgy.block.wood.FlameableBlock;
import net.bauxite_ltk.immersive_metallurgy.block.wood.FlameableLeavesBlock;
import net.bauxite_ltk.immersive_metallurgy.block.wood.FlameableRotatedPillarBlock;
import net.bauxite_ltk.immersive_metallurgy.fluid.IMFluids;
import net.bauxite_ltk.immersive_metallurgy.item.IMItems;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IMBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(IMUtils.MOD_ID);

    public static final DeferredBlock<LiquidBlock> MOLTEN_PIG_IRON = registerNoItem("fluid/molten_pig_iron", () -> new CanSolidifyLiquidBlock(IMFluids.MOLTEN_PIG_IRON.getFlowing(), BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA).noLootTable(), Blocks.IRON_BLOCK, 100));

    public static final DeferredBlock<FlameableRotatedPillarBlock> MASON_PINE_LOG = registerBlock("tree/mason_pine_log", () -> new FlameableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredBlock<FlameableRotatedPillarBlock> MASON_PINE_WOOD = registerBlock("tree/mason_pine_wood", () -> new FlameableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredBlock<FlameableRotatedPillarBlock> STRIPPED_MASON_PINE_LOG = registerBlock("tree/stripped_mason_pine_log", () -> new FlameableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredBlock<FlameableRotatedPillarBlock> STRIPPED_MASON_PINE_WOOD = registerBlock("tree/stripped_mason_pine_wood", () -> new FlameableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));

    public static final DeferredBlock<FlameableBlock> MASON_PINE_PLANKS = registerBlock("tree/mason_pine_planks", () -> new FlameableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS), 20,5));
    public static final DeferredBlock<LeavesBlock> MASON_PINE_LEAVES = registerBlock("tree/mason_pine_leaves", () -> new FlameableLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES), 60 ,30));
    //public static final DeferredBlock<SaplingBlock> MASON_PINE_SAPLING = registerBlock("tree/mason_pine_sapling", () -> new SaplingBlock( , BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));



    private static final Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_NO_OVERLAY =
            () -> Block.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(3, 15)
                    .requiresCorrectToolForDrops()
                    .isViewBlocking((state, blockReader, pos) -> false);

    public static final Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_NO_OCCLUSION = () -> METAL_PROPERTIES_NO_OVERLAY.get().noOcclusion().forceSolidOn();

    private static final Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_DYNAMIC = () -> METAL_PROPERTIES_NO_OCCLUSION.get().dynamicShape();

    public static final DeferredBlock<ElectricCableBlock> ELECTRIC_CABLE_LV = registerBlockIE("electric_cable_lv", () -> ElectricCableBlock.forLv(METAL_PROPERTIES_DYNAMIC.get()));
    public static final DeferredBlock<ElectricCableBlock> ELECTRIC_CABLE_MV = registerBlockIE("electric_cable_mv", () -> ElectricCableBlock.forMv(METAL_PROPERTIES_DYNAMIC.get()));


    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        IMItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static <T extends Block> DeferredBlock<T> registerBlockIE(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name,block);
        registerBlockItemIE(name, toReturn);
        return toReturn;
    }

    public static <T extends Block> void registerBlockItemIE(String name, DeferredBlock<T> block){
        IMItems.ITEMS.register(name, () -> new BlockItemIE(block.get(), new Item.Properties()));
    }

    private static <T extends Block> DeferredBlock<T> registerNoItem(String name, Supplier<T> block)
    {
        return BLOCKS.register(name, block);
    }

    public static void init(IEventBus modEventBus){
        BLOCKS.register(modEventBus);
    }
}
