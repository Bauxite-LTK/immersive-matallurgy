package net.bauxite_ltk.immersive_metallurgy.gui.multiblock;

import blusunrize.immersiveengineering.api.energy.IMutableEnergyStorage;
import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.common.gui.IEContainerMenu;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.sync.GenericContainerData;
import blusunrize.immersiveengineering.common.gui.sync.GenericDataSerializers;
import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.BallMillLogic;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.EliteBlastFurnaceLogic;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class EliteBlastFurnaceMenu extends IEContainerMenu {
    public final IMutableEnergyStorage energy;
    public final EliteBlastFurnaceLogic.EliteBlastFurnaceTanks tanks;
    public final GetterAndSetter<Integer> temperature;
    private static final int ORE_INPUT_MIN_X = 71;
    private static final int ORE_INPUT_MIN_Y = 47;

    private static final int SLUG_OUTPUT_MIN_X = 16;
    private static final int SLUG_OUTPUT_MIN_Y = 81;

    private static final int INV_MIN_X = 8;
    private static final int INV_MIN_Y = 116;
    private static final int MAIN_INV_MIN_Y = 174;

    public static EliteBlastFurnaceMenu makeServer(
            MenuType<?> type, int id, Inventory invPlayer, MultiblockMenuContext<EliteBlastFurnaceLogic.State> ctx
    )
    {
        final EliteBlastFurnaceLogic.State state = ctx.mbContext().getState();
        return new EliteBlastFurnaceMenu(
                multiblockCtx(type, id, ctx), invPlayer,
                state.getInventory(),
                state.getEnergy(),
                state.tanks,
                GetterAndSetter.getterOnly(state::getTemperature)
        );
    }

    public static EliteBlastFurnaceMenu makeClient(MenuType<?> type, int id, Inventory invPlayer)
    {
        return new EliteBlastFurnaceMenu(
                clientCtx(type, id),
                invPlayer,
                new ItemStackHandler(BallMillLogic.NUM_SLOTS),
                new MutableEnergyStorage(BallMillLogic.ENERGY_CAPACITY),
                new EliteBlastFurnaceLogic.EliteBlastFurnaceTanks(),
                GetterAndSetter.standalone(0)
        );
    }


    protected EliteBlastFurnaceMenu(
            MenuContext ctx,
            Inventory inventoryPlayer, IItemHandler inv, IMutableEnergyStorage energy, EliteBlastFurnaceLogic.EliteBlastFurnaceTanks tanks, GetterAndSetter<Integer> temperature) {
        super(ctx);
        this.energy = energy;
        this.tanks = tanks;
        this.temperature = temperature;

        for(int i = 0; i < 4; i++)
            this.addSlot(new SlotItemHandler(inv, i, ORE_INPUT_MIN_X+(i%2)*18, ORE_INPUT_MIN_Y+(i/2)*18));

        this.addSlot(new IESlot.NewOutput(inv, 4, SLUG_OUTPUT_MIN_X, SLUG_OUTPUT_MIN_Y));

        ownSlotCount = 5;

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 9; j++)
                addSlot(new Slot(inventoryPlayer, j+i*9+9, INV_MIN_X+j*18, INV_MIN_Y+i*18));

        for(int i = 0; i < 9; i++)
            addSlot(new Slot(inventoryPlayer, i, INV_MIN_X + i*18, MAIN_INV_MIN_Y));

        addGenericData(GenericContainerData.energy(energy));
        addGenericData(GenericContainerData.fluid(tanks.outputMetal()));
        addGenericData(GenericContainerData.fluid(tanks.outputGas()));
        addGenericData(GenericContainerData.fluid(tanks.inputAirLeft()));
        addGenericData(GenericContainerData.fluid(tanks.inputAirRight()));
        addGenericData(new GenericContainerData<>(GenericDataSerializers.INT32, temperature));

    }
}
