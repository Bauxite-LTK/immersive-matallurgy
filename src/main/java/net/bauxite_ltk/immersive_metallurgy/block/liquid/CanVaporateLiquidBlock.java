package net.bauxite_ltk.immersive_metallurgy.block.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CanVaporateLiquidBlock extends LiquidBlock implements EntityBlock {

    int vaporateTicks;
    Supplier<BlockEntityType<CanVaporateLiquidBlockEntity>> instanceBE;

    public CanVaporateLiquidBlock(FlowingFluid fluid, Properties properties, Supplier<BlockEntityType<CanVaporateLiquidBlockEntity>> instanceBE , int vaporateTicks) {
        super(fluid, properties);
        this.vaporateTicks = vaporateTicks;
        this.instanceBE = instanceBE;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        CanVaporateLiquidBlockEntity be = instanceBE.get().create(blockPos, blockState);
        if(be != null){
            be.setVaporateProperties(vaporateTicks);
        }
        return be;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        BlockEntityTicker<T> baseTicker = (level1, blockPos1, blockState1, blockEntity1) -> {

            //ImmersiveMetallurgy.LOGGER.info("Ticker Method Execute");
            if(blockEntity1 instanceof CanVaporateLiquidBlockEntity VaporateLiquidBE){
                //ImmersiveMetallurgy.LOGGER.info("Ticker Method Server Tick Execute");
                if(!level1.isClientSide) VaporateLiquidBE.serverTick();
                else VaporateLiquidBE.clientTick();
            }
        };
        return baseTicker;
    }
}
