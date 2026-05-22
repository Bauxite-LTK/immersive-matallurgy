package net.bauxite_ltk.immersive_metallurgy.item;

import net.bauxite_ltk.immersive_metallurgy.fluid.IMFluids;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IMItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(IMUtils.MOD_ID);

    public static final DeferredItem<Item> MOLTEN_PIG_IRON_BUCKET =
            ITEMS.register("bucket/molten_pig_iron",
                    () -> new BucketItem(
                            IMFluids.MOLTEN_PIG_IRON.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );

    public static final class Ores{
        public static final DeferredItem<Item> RAW_IRON_ORE_CHUNK = ITEMS.registerSimpleItem("pure_ore_chunk/raw_iron");
        public static final DeferredItem<Item> RAW_IRON_ORE_COARSE_POWDER = ITEMS.registerSimpleItem("pure_coarse_powder/raw_iron");
        public static final DeferredItem<Item> RAW_IRON_ORE_FINES = ITEMS.registerSimpleItem("ore_fines/raw_iron");
        public static final DeferredItem<Item> RAW_IRON_CONCENTRATE_PELLET = ITEMS.registerSimpleItem("concentrate_pellet/raw_iron");

    }



    public static void init(IEventBus modEventBus){
        try {
            Class.forName("net.bauxite_ltk.immersive_metallurgy.item.IMItems$Ores");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ITEMS.register(modEventBus);
    }

}
