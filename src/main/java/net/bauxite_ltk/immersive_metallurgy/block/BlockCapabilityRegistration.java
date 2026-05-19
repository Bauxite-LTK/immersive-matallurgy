package net.bauxite_ltk.immersive_metallurgy.block;


import net.bauxite_ltk.immersive_metallurgy.ImmersiveMetallurgy;
import net.bauxite_ltk.immersive_metallurgy.block.metal.ElectricCableBlockEntity;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.function.Supplier;

@EventBusSubscriber(modid = IMUtils.MOD_ID)
public class BlockCapabilityRegistration {
    @SubscribeEvent
    public static void registerBlockCapabilities(RegisterCapabilitiesEvent event){
        ElectricCableBlockEntity.registerCapabilities(forType(event,IMBlockEntities.ELECTRIC_CABLE_LV));
        ElectricCableBlockEntity.registerCapabilities(forType(event,IMBlockEntities.ELECTRIC_CABLE_MV));
        ImmersiveMetallurgy.LOGGER.info("BlockCapabilityRegistration: Called registerBlockCapabilities");
    }

    private static <BE extends BlockEntity> blusunrize.immersiveengineering.common.blocks.BlockCapabilityRegistration.BECapabilityRegistrar<BE> forType(
            RegisterCapabilitiesEvent ev, Supplier<BlockEntityType<BE>> type
    )
    {
        return new blusunrize.immersiveengineering.common.blocks.BlockCapabilityRegistration.BECapabilityRegistrar<>()
        {
            @Override
            public <C, T> void register(BlockCapability<T, C> capability, ICapabilityProvider<? super BE, C, T> provider)
            {
                ev.registerBlockEntity(capability, type.get(), provider);
            }
        };
    }
}
