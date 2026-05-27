package net.bauxite_ltk.immersive_metallurgy.item;

import net.bauxite_ltk.immersive_metallurgy.fluid.IMFluids;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.world.item.*;
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

    public static final DeferredItem<Item> MASON_PINE_SAP_BUCKET =
            ITEMS.register("bucket/mason_pine_sap",
                    () -> new BucketItem(
                            IMFluids.MASON_PINE_SAP.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );

    public static final DeferredItem<Item> TURPENTINE_OIL_BUCKET =
            ITEMS.register("bucket/turpentine_oil",
                    () -> new BucketItem(
                            IMFluids.TURPENTINE_OIL.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );

    public static final DeferredItem<Item> TERPINEOL_BUCKET =
            ITEMS.register("bucket/terpineol",
                    () -> new BucketItem(
                            IMFluids.TERPINEOL.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );

    public static final DeferredItem<Item> RAW_IRON_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_iron_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_IRON_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );

    public static final DeferredItem<Item> RAW_IRON_PROCESSED_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_iron_processed_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_IRON_PROCESSED_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );

    public static final DeferredItem<Item> RAW_IRON_CONCENTRATE_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_iron_concentrate_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_IRON_CONCENTRATE_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );

    public static final DeferredItem<Item> RAW_IRON_TAILING_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_iron_tailing_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_IRON_TAILING_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );

    public static final DeferredItem<Item> MASON_PINE_SAP_BOTTLE =
            ITEMS.registerItem("mason_pine_sap_bottle", Item::new, new Item.Properties().stacksTo(16));

    public static final DeferredItem<Item> COLOPHONY_BOTTLE = ITEMS.registerItem("colophony_bottle", Item::new, new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));

    public static final class Ores{
        public static final DeferredItem<Item> RAW_IRON_ORE_CHUNK = ITEMS.registerSimpleItem("pure_ore_chunk_raw_iron");
        public static final DeferredItem<Item> RAW_IRON_ORE_COARSE_POWDER = ITEMS.registerSimpleItem("pure_coarse_powder_raw_iron");
        public static final DeferredItem<Item> RAW_IRON_ORE_FINES = ITEMS.registerSimpleItem("ore_fines_raw_iron");
        public static final DeferredItem<Item> RAW_IRON_CONCENTRATE_PELLET = ITEMS.registerSimpleItem("concentrate_pellet_raw_iron");
    }

    public static final DeferredItem<Item> COLOPHONY = ITEMS.register("colophony", ()-> new FurnaceFuelItem(new Item.Properties(), 1600));



    public static void init(IEventBus modEventBus){
        try {
            Class.forName("net.bauxite_ltk.immersive_metallurgy.item.IMItems$Ores");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ITEMS.register(modEventBus);
    }

}
