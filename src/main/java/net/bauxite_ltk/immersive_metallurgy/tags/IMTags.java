package net.bauxite_ltk.immersive_metallurgy.tags;

import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class IMTags {

    public static class Fluids{
        public static final TagKey<Fluid> TEMPERATURE_MOLTEN_FLUID = createTag("temperature_molten_fluid");

        private static TagKey<Fluid> createTag(String name){
            return FluidTags.create(IMUtils.modRL(name));
        }
    }

    public static class Items{
        public static final TagKey<Item> CRYSTAL_CHUNKS = createTag("crystal_chunks");
        public static final TagKey<Item> CONCENTRATES = createTag("concentrates");
        public static final TagKey<Item> ORE_CHUNKS = createTag("ore_chunks");
        public static final TagKey<Item> PURE_COARSE_POWDERS = createTag("pure_coarse_powders");
        public static final TagKey<Item> COARSE_POWDERS = createTag("coarse_powders");
        public static final TagKey<Item> POOR_ORE_CHUNKS = createTag("poor_ore_chunks");
        public static final TagKey<Item> DIRTY_COARSE_POWDERS = createTag("dirty_coarse_powders");

        private static TagKey<Item> createTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(IMUtils.MOD_ID, name));
        }
    }
}
