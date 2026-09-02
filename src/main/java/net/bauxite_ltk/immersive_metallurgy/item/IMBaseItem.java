package net.bauxite_ltk.immersive_metallurgy.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class IMBaseItem extends Item {
    private final String tooltip;
    private final String tooltip2;

    public IMBaseItem(Properties properties) {
        super(properties);
        this.tooltip = null;
        this.tooltip2 = null;
    }

    public IMBaseItem(String tooltip, Properties properties){
        super(properties);
        this.tooltip = tooltip;
        this.tooltip2 = null;
    }

    public IMBaseItem(String tooltip, String tooltip2, Properties properties){
        super(properties);
        this.tooltip = tooltip;
        this.tooltip2 = tooltip2;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(tooltip != null){
            tooltipComponents.add(Component.translatable("tooltip.immersive_metallurgy." + tooltip));
        }
        if(tooltip2 != null){
            tooltipComponents.add(Component.translatable("tooltip.immersive_metallurgy." + tooltip2));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
