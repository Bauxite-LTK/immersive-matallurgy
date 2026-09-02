package net.bauxite_ltk.immersive_metallurgy.gui.multiblock;

import blusunrize.immersiveengineering.api.energy.IMutableEnergyStorage;
import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.common.gui.IEContainerMenu;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.sync.GenericContainerData;
import blusunrize.immersiveengineering.common.gui.sync.GenericDataSerializers;
import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.HotAirFurnaceLogic;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class HotAirFurnaceMenu extends IEContainerMenu {
    public final HotAirFurnaceLogic.HotAirFurnaceTanks tanks;
    public final GetterAndSetter<Float> guiProgress;

    public static HotAirFurnaceMenu makeServer(
            MenuType<?> type, int id, Inventory invPlayer, MultiblockMenuContext<HotAirFurnaceLogic.State> ctx
    )
    {
        final HotAirFurnaceLogic.State state = ctx.mbContext().getState();
        return new HotAirFurnaceMenu(
                multiblockCtx(type, id, ctx),
                invPlayer,
                state.tanks,
                GetterAndSetter.getterOnly(state::getRecipeProgress)
        );
    }

    public static HotAirFurnaceMenu makeClient(MenuType<?> type, int id, Inventory invPlayer)
    {
        return new HotAirFurnaceMenu(
                clientCtx(type, id),
                invPlayer,
                new HotAirFurnaceLogic.HotAirFurnaceTanks(),
                GetterAndSetter.standalone(0f)
        );
    }


    protected HotAirFurnaceMenu(
            MenuContext ctx,
            Inventory inventoryPlayer,
            HotAirFurnaceLogic.HotAirFurnaceTanks tanks,
            GetterAndSetter<Float> guiProgress) {
        super(ctx);
        this.tanks = tanks;
        this.guiProgress = guiProgress;


        ownSlotCount = 1;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 9; j++)
                addSlot(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 85+i*18));
        for(int i = 0; i < 9; i++)
            addSlot(new Slot(inventoryPlayer, i, 8+i*18, 143));
        addGenericData(GenericContainerData.fluid(tanks.inputGas()));
        addGenericData(GenericContainerData.fluid(tanks.outputAir()));
        addGenericData(new GenericContainerData<>(GenericDataSerializers.FLOAT, guiProgress));

    }
}
