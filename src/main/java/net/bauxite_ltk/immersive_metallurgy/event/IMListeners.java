package net.bauxite_ltk.immersive_metallurgy.event;

import blusunrize.immersiveengineering.api.utils.SafeChunkUtils;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlocks;
import net.bauxite_ltk.immersive_metallurgy.block.metal.ElectricCableBlockEntity;
import net.bauxite_ltk.immersive_metallurgy.render.ElectricCableSelectionRenderer;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.level.BlockEvent;


public class IMListeners {
    @SubscribeEvent
    public void forElectricCable(BlockEvent.NeighborNotifyEvent event){
        Level eventLevel = (Level)event.getLevel();
        if(eventLevel.isClientSide) return;
        BlockPos updatePos = event.getPos();
        IMUtils.LOGGER.info("[forElectricCable] updatePos:{}", updatePos);
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                for(int k = -1; k <= 1; k++){
                    BlockPos targetPos = updatePos.offset(i,j,k);
                    if(targetPos == updatePos) continue;
                    if(SafeChunkUtils.getSafeBE(eventLevel, targetPos) instanceof ElectricCableBlockEntity targetElectricCable){
                        if(targetElectricCable.updateBackCornerConnection(updatePos)) {
                            targetElectricCable.updateAllRootNode();
                            //targetElectricCable.invalidateCapabilities();
                            Level world = targetElectricCable.getLevelNonnull();
                            world.sendBlockUpdated(targetPos, targetElectricCable.getBlockState(), targetElectricCable.getBlockState(), 3);
                        }
                        if(SafeChunkUtils.getSafeBE(eventLevel, updatePos) instanceof ElectricCableBlockEntity thisElectricCable){
                            if(thisElectricCable.updateBackCornerConnection(targetPos)) {
                                thisElectricCable.updateAllRootNode();
                                //thisElectricCable.invalidateCapabilities();
                                Level world = thisElectricCable.getLevelNonnull();
                                world.sendBlockUpdated(updatePos, thisElectricCable.getBlockState(), thisElectricCable.getBlockState(), 3);
                            }
                        }
                    }

                }
            }
        }
    }



//    @SubscribeEvent
//    public void forElectricCablePlacement(ClientTickEvent.Post tickEvent){
//        Minecraft mc = Minecraft.getInstance();
//        Player player = mc.player;
//        if(player!=null){
//            ItemStack mainHandItem = player.getMainHandItem();
//            ElectricCableSelectionRenderer.ClientData.setHeldItem(mainHandItem);
//            if (mainHandItem.is(Items.STICK)) { // 以木棍为例
//                // 获取玩家视线方向
//                HitResult hitResult = mc.hitResult;
//
//                if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
//                    BlockHitResult blockHit = (BlockHitResult) hitResult;
//                    BlockPos targetPos = blockHit.getBlockPos();
//                    BlockState targetState = mc.level.getBlockState(targetPos);
//
//                    // 检查是否为目标方块
//                    if (targetState.is(IMBlocks.ELECTRIC_CABLE_LV)) {
//                        ElectricCableSelectionRenderer.ClientData.setTargetedBlock(targetPos, blockHit.getDirection());
//                    } else {
//                        ElectricCableSelectionRenderer.ClientData.clearTargetedBlock();
//                    }
//                } else {
//                    ElectricCableSelectionRenderer.ClientData.clearTargetedBlock();
//                }
//            } else {
//                ElectricCableSelectionRenderer.ClientData.clearTargetedBlock();
//            }
//        }
//    }

}
