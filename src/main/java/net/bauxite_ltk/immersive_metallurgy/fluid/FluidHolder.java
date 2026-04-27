package net.bauxite_ltk.immersive_metallurgy.fluid;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;

public record FluidHolder<F extends FlowingFluid>(
        DeferredHolder<FluidType, FluidType> type,
        DeferredHolder<Fluid, F> flowing,
        DeferredHolder<Fluid, F> source
) {
    public F getFlowing()
    {
        return flowing.get();
    }

    public F getSource()
    {
        return source.get();
    }

    public FluidType getType()
    {
        return type.value();
    }

    public BlockState createSourceBlock()
    {
        return source.get().defaultFluidState().createLegacyBlock();
    }
}
