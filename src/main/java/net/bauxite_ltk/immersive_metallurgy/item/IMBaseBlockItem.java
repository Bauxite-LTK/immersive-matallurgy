package net.bauxite_ltk.immersive_metallurgy.item;

import blusunrize.immersiveengineering.common.blocks.BlockItemIE;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class IMBaseBlockItem extends BlockItemIE {
    private final String tooltip;

    public IMBaseBlockItem(Block b, Item.Properties properties) {
        super(b, properties);
        this.tooltip = null;
    }

    public IMBaseBlockItem(Block b, String tooltip, Item.Properties properties){
        super(b, properties);
        this.tooltip = tooltip;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(tooltip != null){
            tooltipComponents.add(Component.translatable("tooltip.immersive_metallurgy." + tooltip));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
