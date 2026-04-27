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

    public static void init(IEventBus modEventBus){
        ITEMS.register(modEventBus);
    }

}
