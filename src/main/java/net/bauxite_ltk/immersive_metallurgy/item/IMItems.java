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

    public static final DeferredItem<Item> MOLTEN_GOLD_BUCKET =
            ITEMS.register("bucket/molten_gold",
                    () -> new BucketItem(
                            IMFluids.MOLTEN_GOLD.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );

    public static final DeferredItem<Item> MOLTEN_COPPER_BUCKET =
            ITEMS.register("bucket/molten_copper",
                    () -> new BucketItem(
                            IMFluids.MOLTEN_COPPER.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );

    public static final DeferredItem<Item> MOLTEN_SILVER_BUCKET =
            ITEMS.register("bucket/molten_silver",
                    () -> new BucketItem(
                            IMFluids.MOLTEN_SILVER.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );

    public static final DeferredItem<Item> MOLTEN_LEAD_BUCKET =
            ITEMS.register("bucket/molten_lead",
                    () -> new BucketItem(
                            IMFluids.MOLTEN_LEAD.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );

    public static final DeferredItem<Item> MOLTEN_NICKEL_BUCKET =
            ITEMS.register("bucket/molten_nickel",
                    () -> new BucketItem(
                            IMFluids.MOLTEN_NICKEL.getSource(),
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


    /*
    -----------------------------------------------------------
      Slurry Fluid Bucket
      Ore: RAW IRON
    -----------------------------------------------------------
    */
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



    /*
    -----------------------------------------------------------
      Slurry Fluid Bucket
      Ore: RAW GOLD
    -----------------------------------------------------------
    */
    public static final DeferredItem<Item> RAW_GOLD_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_gold_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_GOLD_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_GOLD_PROCESSED_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_gold_processed_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_GOLD_PROCESSED_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_GOLD_CONCENTRATE_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_gold_concentrate_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_GOLD_CONCENTRATE_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_GOLD_TAILING_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_gold_tailing_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_GOLD_TAILING_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );


    /*
    -----------------------------------------------------------
      Slurry Fluid Bucket
      Ore: RAW COPPER
    -----------------------------------------------------------
    */
    public static final DeferredItem<Item> RAW_COPPER_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_copper_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_COPPER_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_COPPER_PROCESSED_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_copper_processed_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_COPPER_PROCESSED_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_COPPER_CONCENTRATE_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_copper_concentrate_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_COPPER_CONCENTRATE_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_COPPER_TAILING_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_copper_tailing_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_COPPER_TAILING_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );



    /*
    -----------------------------------------------------------
      Slurry Fluid Bucket
      Ore: RAW SILVER
    -----------------------------------------------------------
    */
    public static final DeferredItem<Item> RAW_SILVER_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_silver_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_SILVER_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_SILVER_PROCESSED_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_silver_processed_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_SILVER_PROCESSED_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_SILVER_CONCENTRATE_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_silver_concentrate_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_SILVER_CONCENTRATE_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_SILVER_TAILING_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_silver_tailing_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_SILVER_TAILING_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );



    /*
    -----------------------------------------------------------
      Slurry Fluid Bucket
      Ore: RAW LEAD
    -----------------------------------------------------------
    */
    public static final DeferredItem<Item> RAW_LEAD_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_lead_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_LEAD_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_LEAD_PROCESSED_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_lead_processed_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_LEAD_PROCESSED_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_LEAD_CONCENTRATE_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_lead_concentrate_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_LEAD_CONCENTRATE_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_LEAD_TAILING_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_lead_tailing_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_LEAD_TAILING_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );


    /*
    -----------------------------------------------------------
      Slurry Fluid Bucket
      Ore: RAW NICKEL
    -----------------------------------------------------------
    */
    public static final DeferredItem<Item> RAW_NICKEL_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_nickel_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_NICKEL_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_NICKEL_PROCESSED_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_nickel_processed_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_NICKEL_PROCESSED_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_NICKEL_CONCENTRATE_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_nickel_concentrate_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_NICKEL_CONCENTRATE_SLURRY.getSource(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
                    )
            );
    public static final DeferredItem<Item> RAW_NICKEL_TAILING_SLURRY_BUCKET =
            ITEMS.register("bucket/raw_nickel_tailing_slurry",
                    () -> new BucketItem(
                            IMFluids.RAW_NICKEL_TAILING_SLURRY.getSource(),
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


        public static final DeferredItem<Item> RAW_GOLD_ORE_CHUNK = ITEMS.registerSimpleItem("pure_ore_chunk_raw_gold");
        public static final DeferredItem<Item> RAW_GOLD_ORE_COARSE_POWDER = ITEMS.registerSimpleItem("pure_coarse_powder_raw_gold");
        public static final DeferredItem<Item> RAW_GOLD_ORE_FINES = ITEMS.registerSimpleItem("ore_fines_raw_gold");
        public static final DeferredItem<Item> RAW_GOLD_CONCENTRATE_PELLET = ITEMS.registerSimpleItem("concentrate_pellet_raw_gold");

        public static final DeferredItem<Item> RAW_COPPER_ORE_CHUNK = ITEMS.registerSimpleItem("pure_ore_chunk_raw_copper");
        public static final DeferredItem<Item> RAW_COPPER_ORE_COARSE_POWDER = ITEMS.registerSimpleItem("pure_coarse_powder_raw_copper");
        public static final DeferredItem<Item> RAW_COPPER_ORE_FINES = ITEMS.registerSimpleItem("ore_fines_raw_copper");
        public static final DeferredItem<Item> RAW_COPPER_CONCENTRATE_PELLET = ITEMS.registerSimpleItem("concentrate_pellet_raw_copper");

        public static final DeferredItem<Item> RAW_SILVER_ORE_CHUNK = ITEMS.registerSimpleItem("pure_ore_chunk_raw_silver");
        public static final DeferredItem<Item> RAW_SILVER_ORE_COARSE_POWDER = ITEMS.registerSimpleItem("pure_coarse_powder_raw_silver");
        public static final DeferredItem<Item> RAW_SILVER_ORE_FINES = ITEMS.registerSimpleItem("ore_fines_raw_silver");
        public static final DeferredItem<Item> RAW_SILVER_CONCENTRATE_PELLET = ITEMS.registerSimpleItem("concentrate_pellet_raw_silver");

        public static final DeferredItem<Item> RAW_LEAD_ORE_CHUNK = ITEMS.registerSimpleItem("pure_ore_chunk_raw_lead");
        public static final DeferredItem<Item> RAW_LEAD_ORE_COARSE_POWDER = ITEMS.registerSimpleItem("pure_coarse_powder_raw_lead");
        public static final DeferredItem<Item> RAW_LEAD_ORE_FINES = ITEMS.registerSimpleItem("ore_fines_raw_lead");
        public static final DeferredItem<Item> RAW_LEAD_CONCENTRATE_PELLET = ITEMS.registerSimpleItem("concentrate_pellet_raw_lead");


        public static final DeferredItem<Item> RAW_NICKEL_ORE_CHUNK = ITEMS.registerSimpleItem("pure_ore_chunk_raw_nickel");
        public static final DeferredItem<Item> RAW_NICKEL_ORE_COARSE_POWDER = ITEMS.registerSimpleItem("pure_coarse_powder_raw_nickel");
        public static final DeferredItem<Item> RAW_NICKEL_ORE_FINES = ITEMS.registerSimpleItem("ore_fines_raw_nickel");
        public static final DeferredItem<Item> RAW_NICKEL_CONCENTRATE_PELLET = ITEMS.registerSimpleItem("concentrate_pellet_raw_nickel");


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
