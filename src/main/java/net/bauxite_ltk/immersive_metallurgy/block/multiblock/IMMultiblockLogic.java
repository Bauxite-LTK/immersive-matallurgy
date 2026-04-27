package net.bauxite_ltk.immersive_metallurgy.block.multiblock;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.BallMillLogic;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.FlotationCellLogic;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.HydrocycloneLogic;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.ThickenerLogic;
import net.bauxite_ltk.immersive_metallurgy.gui.IMMenuTypes;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IMMultiblockLogic {
    public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(
            BuiltInRegistries.BLOCK, IMUtils.MOD_ID
    );
    private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(
            BuiltInRegistries.ITEM, IMUtils.MOD_ID
    );
    private static final DeferredRegister<BlockEntityType<?>> BE_REGISTER = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, IMUtils.MOD_ID
    );

    public static final MultiblockRegistration<BallMillLogic.State> BALL_MILL =
            metal(new BallMillLogic(), "ball_mill")
            .structure(() -> IMMultiblocks.BALL_MILL)
            .gui(IMMenuTypes.BALL_MILL)
            .redstone(s -> s.rsState, BallMillLogic.REDSTONE_POS)
            .comparator(BallMillLogic.makeComparator())
            .build();

    public static final MultiblockRegistration<FlotationCellLogic.State> FLOTATION_CELL =
            metal(new FlotationCellLogic(), "flotation_cell")
            .structure(() -> IMMultiblocks.FLOTATION_CELL)
            .gui(IMMenuTypes.FLOTATION_CELL)
            .redstone(s -> s.rsState, FlotationCellLogic.REDSTONE_POS)
            .build();

    public static final MultiblockRegistration<HydrocycloneLogic.State> HYDROCYCLONE =
            metal(new HydrocycloneLogic(), "hydrocyclone")
                    .structure(() -> IMMultiblocks.HYDROCYCLONE)
                    .gui(IMMenuTypes.HYDROCYCLONE)
                    .redstone(s -> s.rsState, HydrocycloneLogic.REDSTONE_POS)
                    .build();

    public static final MultiblockRegistration<ThickenerLogic.State> THICKENER =
            metal(new ThickenerLogic(), "thickener")
                    .structure(() -> IMMultiblocks.THICKENER)
                    .gui(IMMenuTypes.THICKENER)
                    .redstone(s -> s.rsState, ThickenerLogic.REDSTONE_POS)
                    .build();

    private static <S extends IMultiblockState>
    IMMultiblockBuilder<S> metal(IMultiblockLogic<S> logic, String name)
    {
        return new IMMultiblockBuilder<>(logic, name)
                .defaultBEs(BE_REGISTER)
                .defaultBlock(BLOCK_REGISTER, ITEM_REGISTER, IEBlocks.METAL_PROPERTIES_NO_OCCLUSION.get());
    }

    public static void init(IEventBus bus)
    {
        BLOCK_REGISTER.register(bus);
        ITEM_REGISTER.register(bus);
        BE_REGISTER.register(bus);
        IMMultiblockBuilder.handleModBusRegistrations(bus);
    }
}
