package net.bauxite_ltk.immersive_metallurgy.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class FurnaceFuelItem extends IMBaseItem {

    private final int burnTime;

    public FurnaceFuelItem(int burnTime){
        super(new Properties());
        this.burnTime = burnTime;
    }

    public FurnaceFuelItem(int burnTime, Properties properties) {
        super(properties);
        this.burnTime = burnTime;
    }

    public FurnaceFuelItem(Properties properties, int burnTime, String tooltip) {
        super(tooltip, properties);
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return burnTime;
    }
}
