package net.bauxite_ltk.immersive_metallurgy.gui.multiblock;

import blusunrize.immersiveengineering.api.energy.IMutableEnergyStorage;
import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.common.gui.IEContainerMenu;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.sync.GenericContainerData;
import blusunrize.immersiveengineering.common.gui.sync.GenericDataSerializers;
import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.ContinuousCastingMachineLogic;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.ThickenerLogic;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ContinuousCastingMachineMenu extends IEContainerMenu {
    public final ContinuousCastingMachineLogic.ContinuousCastingMachineTanks tanks;
    public final GetterAndSetter<Float> guiProgress;
    public final GetterAndSetter<Integer> fuelTicks;
    public final IEnergyStorage energy;

    public static ContinuousCastingMachineMenu makeServer(
            MenuType<?> type, int id, Inventory invPlayer, MultiblockMenuContext<ContinuousCastingMachineLogic.State> ctx
    )
    {
        final ContinuousCastingMachineLogic.State state = ctx.mbContext().getState();
        return new ContinuousCastingMachineMenu(
                multiblockCtx(type, id, ctx),
                invPlayer,
                state.getInventory(),
                state.getEnergy(),
                state.tanks,
                GetterAndSetter.getterOnly(state::getRecipeProgress),
                GetterAndSetter.getterOnly(state::getFuelTicks)
        );
    }

    public static ContinuousCastingMachineMenu makeClient(MenuType<?> type, int id, Inventory invPlayer)
    {
        return new ContinuousCastingMachineMenu(
                clientCtx(type, id),
                invPlayer,
                new ItemStackHandler(ContinuousCastingMachineLogic.NUM_SLOTS),
                new MutableEnergyStorage(ContinuousCastingMachineLogic.ENERGY_CAPACITY),
                new ContinuousCastingMachineLogic.ContinuousCastingMachineTanks(),
                GetterAndSetter.standalone(0f),
                GetterAndSetter.standalone(0)
        );
    }


    protected ContinuousCastingMachineMenu(
            MenuContext ctx,
            Inventory inventoryPlayer,
            IItemHandler inv,
            IMutableEnergyStorage energy,
            ContinuousCastingMachineLogic.ContinuousCastingMachineTanks tanks,
            GetterAndSetter<Float> guiProgress,
            GetterAndSetter<Integer> fuelTicks
    ) {
        super(ctx);
        this.tanks = tanks;
        this.guiProgress = guiProgress;
        this.fuelTicks = fuelTicks;
        this.energy = energy;

        this.addSlot(new IESlot.NewOutput(inv, 0, 134, 29));

        ownSlotCount = 1;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 9; j++)
                addSlot(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 85+i*18));
        for(int i = 0; i < 9; i++)
            addSlot(new Slot(inventoryPlayer, i, 8+i*18, 143));
        addGenericData(GenericContainerData.fluid(tanks.inputGas()));
        addGenericData(GenericContainerData.fluid(tanks.inputMetal()));
        addGenericData(GenericContainerData.fluid(tanks.inputWater()));
        addGenericData(GenericContainerData.energy(energy));
        addGenericData(new GenericContainerData<>(GenericDataSerializers.FLOAT, guiProgress));
        addGenericData(new GenericContainerData<>(GenericDataSerializers.INT32, fuelTicks));


    }
}
