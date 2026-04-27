package net.bauxite_ltk.immersive_metallurgy.event;

import net.bauxite_ltk.immersive_metallurgy.ImmersiveMetallurgy;
import net.bauxite_ltk.immersive_metallurgy.tags.IMTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityEvent;

@EventBusSubscriber(modid = ImmersiveMetallurgy.MOD_ID, value = Dist.CLIENT)
public class IMFluidEvent {
    @SubscribeEvent
    public static void onEntityEnterFluid(EntityEvent.EnteringSection event) {
        Entity entity = event.getEntity();
        Level level = entity.level();

        if (!level.isClientSide && entity instanceof LivingEntity living) {
            BlockPos pos = entity.blockPosition();
            FluidState fluidState = level.getFluidState(pos);

            // 检查实体是否进入危险流体
            if (fluidState.is(IMTags.Fluids.TEMPERATURE_MOLTEN_FLUID)) {
                level.playSound(null, pos, SoundEvents.LAVA_POP,
                        SoundSource.BLOCKS, 0.5F, 1.0F);

                // 发送消息给玩家
                if (living instanceof Player player) {
                    player.displayClientMessage(
                            Component.literal("熔融液体温度过高！")
                                    .withStyle(ChatFormatting.RED),
                            true
                    );
                }
            }
        }
    }
}
