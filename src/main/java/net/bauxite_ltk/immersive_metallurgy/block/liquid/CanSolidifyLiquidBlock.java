package net.bauxite_ltk.immersive_metallurgy.block.liquid;

import net.bauxite_ltk.immersive_metallurgy.ImmersiveMetallurgy;
import net.bauxite_ltk.immersive_metallurgy.block.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CanSolidifyLiquidBlock extends LiquidBlock implements EntityBlock {

    int solidifyTicks;
    Block solid;


    public CanSolidifyLiquidBlock( FlowingFluid fluid, Properties properties, Block solid, int solidifyTicks) {
        super(fluid, properties);
        this.solid = solid;
        this.solidifyTicks = solidifyTicks;
    }



    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        entity.lavaHurt();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        CanSolidifyLiquidBlockEntity be = IMBlockEntities.CAN_SOLIDIFY_LIQUID.get().create(blockPos, blockState);
        if(be != null){
            be.setSolidProperties(solid, solidifyTicks);
        }
        return be;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        BlockEntityTicker<T> baseTicker = (level1, blockPos1, blockState1, blockEntity1) -> {

            //ImmersiveMetallurgy.LOGGER.info("Ticker Method Execute");
            if(blockEntity1 instanceof CanSolidifyLiquidBlockEntity solidifyLiquidBE){
                //ImmersiveMetallurgy.LOGGER.info("Ticker Method Server Tick Execute");
                if(!level1.isClientSide) solidifyLiquidBE.serverTick();
                else solidifyLiquidBE.clientTick();
            }
        };
        return baseTicker;
    }
}
