package net.bauxite_ltk.immersive_metallurgy;

import blusunrize.immersiveengineering.api.EnumMetals;
import blusunrize.immersiveengineering.common.register.IEItems;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlocks;
import net.bauxite_ltk.immersive_metallurgy.item.IMItems;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IMCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IMUtils.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("main_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.immersive_metallurgy")) //The language key for the title of your CreativeModeTab
            .icon(() -> IEItems.Metals.INGOTS.get(EnumMetals.STEEL).get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(IMItems.MOLTEN_PIG_IRON_BUCKET.get());
                output.accept(IMBlocks.ELECTRIC_CABLE_LV.asItem());
                output.accept(IMBlocks.ELECTRIC_CABLE_MV.asItem());
                output.accept(IMItems.Ores.RAW_IRON_ORE_CHUNK);
                output.accept(IMItems.Ores.RAW_IRON_ORE_COARSE_POWDER);
                output.accept(IMItems.Ores.RAW_IRON_ORE_FINES);
                output.accept(IMItems.Ores.RAW_IRON_CONCENTRATE_PELLET);
                output.accept(IMBlocks.MASON_PINE_LOG);
                output.accept(IMBlocks.MASON_PINE_WOOD);
                output.accept(IMBlocks.STRIPPED_MASON_PINE_LOG);
                output.accept(IMBlocks.STRIPPED_MASON_PINE_WOOD);
                output.accept(IMBlocks.MASON_PINE_LEAVES);
                output.accept(IMBlocks.MASON_PINE_PLANKS);

            }).build()
    );


    public static void init(IEventBus modEventBus){
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
