package net.bauxite_ltk.immersive_metallurgy.block;

import blusunrize.immersiveengineering.common.blocks.BlockItemIE;
import net.bauxite_ltk.immersive_metallurgy.block.liquid.CanSolidifyLiquidBlock;
import net.bauxite_ltk.immersive_metallurgy.block.metal.ElectricCableBlock;
import net.bauxite_ltk.immersive_metallurgy.fluid.IMFluids;
import net.bauxite_ltk.immersive_metallurgy.item.IMItems;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IMBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(IMUtils.MOD_ID);

    public static final DeferredBlock<LiquidBlock> MOLTEN_PIG_IRON = registerNoItem("fluid/molten_pig_iron", () -> new CanSolidifyLiquidBlock(IMFluids.MOLTEN_PIG_IRON.getFlowing(), BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA).noLootTable(), Blocks.IRON_BLOCK, 100));





    private static final Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_NO_OVERLAY =
            () -> Block.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(3, 15)
                    .requiresCorrectToolForDrops()
                    .isViewBlocking((state, blockReader, pos) -> false);

    public static final Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_NO_OCCLUSION = () -> METAL_PROPERTIES_NO_OVERLAY.get().noOcclusion().forceSolidOn();

    private static final Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_DYNAMIC = () -> METAL_PROPERTIES_NO_OCCLUSION.get().dynamicShape();

    public static final DeferredBlock<ElectricCableBlock> ELECTRIC_CABLE = registerBlockIE("electric_cable_lv", () -> new ElectricCableBlock(METAL_PROPERTIES_DYNAMIC.get()));


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
