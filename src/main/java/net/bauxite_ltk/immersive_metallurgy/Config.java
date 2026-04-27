package net.bauxite_ltk.immersive_metallurgy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import net.bauxite_ltk.immersive_metallurgy.crafting.BallMillRecipe;
import net.bauxite_ltk.immersive_metallurgy.crafting.FlotationCellRecipe;
import net.bauxite_ltk.immersive_metallurgy.crafting.HydrocycloneRecipe;
import net.bauxite_ltk.immersive_metallurgy.crafting.ThickenerRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {


    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final Machines MACHINES = new Machines(BUILDER);

    public static class Machines
    {
        public final MultiblockRecipe.RecipeMultiplier ballMillConfig;
        public final MultiblockRecipe.RecipeMultiplier hydrocycloneConfig;
        public final MultiblockRecipe.RecipeMultiplier flotationCellConfig;
        public final MultiblockRecipe.RecipeMultiplier thickenerConfig;

        Machines(ModConfigSpec.Builder builder)
        {
            builder.push("machines");

            ballMillConfig = addMachineEnergyTimeModifiers(builder, "ball mill");
            hydrocycloneConfig = addMachineEnergyTimeModifiers(builder, "hydrocyclone");
            flotationCellConfig = addMachineEnergyTimeModifiers(builder, "flotation cell");
            thickenerConfig = addMachineEnergyTimeModifiers(builder, "thickener");

            builder.pop();
        }

        private MultiblockRecipe.RecipeMultiplier addMachineEnergyTimeModifiers(ModConfigSpec.Builder builder, String machine)
        {
            return addMachineEnergyTimeModifiers(builder, machine, true);
        }

        private MultiblockRecipe.RecipeMultiplier addMachineEnergyTimeModifiers(ModConfigSpec.Builder builder, String machine, boolean popCategory)
        {
            builder.push(machine.replace(' ', '_'));
            ModConfigSpec.DoubleValue energy = builder
                    .comment("A modifier to apply to the energy costs of every "+machine+" recipe")
                    .worldRestart()
                    .defineInRange("energyModifier", 1, 1e-3, 1e3);
            ModConfigSpec.DoubleValue time = builder
                    .comment("A modifier to apply to the time of every "+machine+" recipe")
                    .defineInRange("timeModifier", 1, 1e-3, 1e3);
            if(popCategory)
                builder.pop();
            return new MultiblockRecipe.RecipeMultiplier(time::get, energy::get);
        }


        public void populateAPI()
        {
            BallMillRecipe.MULTIPLIERS = () -> new MultiblockRecipe.RecipeMultiplier(ballMillConfig.timeModifier(),ballMillConfig.energyModifier());
            HydrocycloneRecipe.MULTIPLIERS = () -> new MultiblockRecipe.RecipeMultiplier(hydrocycloneConfig.timeModifier(),hydrocycloneConfig.energyModifier());
            FlotationCellRecipe.MULTIPLIERS = () -> new MultiblockRecipe.RecipeMultiplier(flotationCellConfig.timeModifier(),flotationCellConfig.energyModifier());
            ThickenerRecipe.MULTIPLIERS = () -> new MultiblockRecipe.RecipeMultiplier(thickenerConfig.timeModifier(),thickenerConfig.energyModifier());
        }
    }

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
