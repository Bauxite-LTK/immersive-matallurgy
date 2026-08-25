package net.bauxite_ltk.immersive_metallurgy.crafting;

import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IMRecipeType {
    private static final DeferredRegister<RecipeType<?>> REGISTER = DeferredRegister.create(
            Registries.RECIPE_TYPE, IMUtils.MOD_ID
    );

    public static final IERecipeTypes.TypeWithClass<BallMillRecipe> BALL_MILL = register("ball_mill", BallMillRecipe.class);
    public static final IERecipeTypes.TypeWithClass<FlotationCellRecipe> FLOTATION_CELL = register("flotation_cell", FlotationCellRecipe.class);
    public static final IERecipeTypes.TypeWithClass<HydrocycloneRecipe> HYDROCYCLONE = register("hydrocyclone", HydrocycloneRecipe.class);
    public static final IERecipeTypes.TypeWithClass<ThickenerRecipe> THICKENER = register("thickener", ThickenerRecipe.class);
    public static final IERecipeTypes.TypeWithClass<EliteBlastFurnaceRecipe> ELITE_BLAST_FURNACE = register("elite_blast_furnace", EliteBlastFurnaceRecipe.class);
    public static final IERecipeTypes.TypeWithClass<HotAirFurnaceRecipe> HOT_AIR_FURNACE = register("hot_air_furnace", HotAirFurnaceRecipe.class);



    private static <T extends Recipe<?>>
    IERecipeTypes.TypeWithClass<T> register(String name, Class<T> type)
    {
        DeferredHolder<RecipeType<?>, RecipeType<T>> regObj = REGISTER.register(name, () -> new RecipeType<>()
        {
        });
        return new IERecipeTypes.TypeWithClass<>(regObj, type);
    }

    public static void init(IEventBus modBus)
    {
        REGISTER.register(modBus);
    }

    public record TypeWithClass<T extends Recipe<?>>(
            DeferredHolder<RecipeType<?>, RecipeType<T>> type, Class<T> recipeClass
    ) implements Supplier<RecipeType<T>>
    {
        public RecipeType<T> get()
        {
            return type.get();
        }
    }
}
