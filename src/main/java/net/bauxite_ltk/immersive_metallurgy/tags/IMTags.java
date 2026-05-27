package net.bauxite_ltk.immersive_metallurgy.tags;

import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class IMTags {

    public static class Fluids{
        public static final TagKey<Fluid> TEMPERATURE_MOLTEN_FLUID = createTag("temperature_molten_fluid");
        public static final TagKey<Fluid> SAP_FLUID = createTag("sap_fluid");

        private static TagKey<Fluid> createTag(String name){
            return FluidTags.create(IMUtils.modRL(name));
        }
    }

    public static class Items{
        public static final TagKey<Item> PURE_ORE_CHUNKS = createTag("pure_ore_chunks");
        public static final TagKey<Item> PURE_COARSE_POWDERS = createTag("pure_coarse_powders");
        public static final TagKey<Item> ORE_FINES = createTag("ore_fines");
        public static final TagKey<Item> CONCENTRATE_PELLETS = createTag("concentrates_pellets");


        private static TagKey<Item> createTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(IMUtils.MOD_ID, name));
        }
    }
}
