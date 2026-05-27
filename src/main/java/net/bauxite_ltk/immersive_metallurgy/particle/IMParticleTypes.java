package net.bauxite_ltk.immersive_metallurgy.particle;

import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class IMParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, IMUtils.MOD_ID);

    public static final Supplier<SimpleParticleType> DRIPPING_SAP =
            PARTICLE_TYPES.register("dripping_sap",
                    () -> new SimpleParticleType(true)
            );

    public static void init(IEventBus modEventbus) {
        PARTICLE_TYPES.register(modEventbus);
    }
}
