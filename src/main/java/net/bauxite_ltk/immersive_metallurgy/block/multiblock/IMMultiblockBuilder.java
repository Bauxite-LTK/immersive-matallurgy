package net.bauxite_ltk.immersive_metallurgy.block.multiblock;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistrationBuilder;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.ComparatorManager;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IMultiblockComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.RedstoneControl;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import com.google.common.base.Preconditions;
import net.bauxite_ltk.immersive_metallurgy.gui.IMMenuTypes;
import net.bauxite_ltk.immersive_metallurgy.gui.IMMultiblockGui;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IMMultiblockBuilder<S extends IMultiblockState>
        extends MultiblockRegistrationBuilder<S, IMMultiblockBuilder<S>>
{
    private static final List<Consumer<IEventBus>> LAZY_MOD_BUS_REGISTRATION = new ArrayList<>();

    public IMMultiblockBuilder(IMultiblockLogic<S> logic, String name)
    {
        super(logic, IMUtils.modRL(name));
    }

    public IMMultiblockBuilder<S> gui(IMMenuTypes.MultiblockContainer<S, ?> menu)
    {
        return component(new IMMultiblockGui<>(menu));
    }

    public IMMultiblockBuilder<S> redstoneNoComputer(IMultiblockComponent.StateWrapper<S, RedstoneControl.RSState> getState, BlockPos... positions)
    {
        redstoneAware();
        return selfWrappingComponent(new RedstoneControl<>(getState, false, positions));
    }

    public IMMultiblockBuilder<S> redstone(IMultiblockComponent.StateWrapper<S, RedstoneControl.RSState> getState, BlockPos... positions)
    {
        redstoneAware();
        return selfWrappingComponent(new RedstoneControl<>(getState, positions));
    }

    public IMMultiblockBuilder<S> comparator(ComparatorManager<S> comparator)
    {
        withComparator();
        return super.selfWrappingComponent(comparator);
    }

    public MultiblockRegistration<S> build()
    {
        return super.build(LAZY_MOD_BUS_REGISTRATION::add);
    }

    @Override
    public <CS, C extends IMultiblockComponent<CS> & IMultiblockComponent.StateWrapper<S, CS>>
    IMMultiblockBuilder<S> selfWrappingComponent(C extraComponent)
    {
        Preconditions.checkArgument(!(extraComponent instanceof ComparatorManager<?>));
        return super.selfWrappingComponent(extraComponent);
    }

    @Override
    protected IMMultiblockBuilder<S> self()
    {
        return this;
    }

    public static void handleModBusRegistrations(IEventBus modBus)
    {
        LAZY_MOD_BUS_REGISTRATION.forEach(registration -> registration.accept(modBus));
    }
}
