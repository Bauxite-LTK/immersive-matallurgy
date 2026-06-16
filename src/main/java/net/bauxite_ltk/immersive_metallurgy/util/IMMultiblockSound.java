package net.bauxite_ltk.immersive_metallurgy.util;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.sound.MultiblockSound;
import blusunrize.immersiveengineering.mixin.accessors.client.GuiSubtitleOverlayAccess;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class IMMultiblockSound extends MultiblockSound {
    Supplier<Float> volumeSup;

    public IMMultiblockSound(BooleanSupplier active, BooleanSupplier valid, Vec3 pos, SoundEvent sound, boolean loop, Supplier<Float> volumeSup) {
        super(active, valid, pos, sound, loop, volumeSup.get());
        this.volumeSup = volumeSup;
    }

    public static BooleanSupplier startSound(
            BooleanSupplier active, BooleanSupplier valid, Vec3 pos, Holder<SoundEvent> sound, Supplier<Float> maxVolumeSup
    )
    {
        return startSound(active, valid, pos, sound, true, maxVolumeSup);
    }

    public static BooleanSupplier startSound(
            BooleanSupplier active, BooleanSupplier valid, Vec3 pos, Holder<SoundEvent> sound, boolean loop, Supplier<Float> maxVolumeSup
    )
    {
        final MultiblockSound instance = new IMMultiblockSound(active, valid, pos, sound.value(), loop, maxVolumeSup);
        final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        soundManager.play(instance);
        return () -> soundManager.isActive(instance);
    }

    @Override
    public void tick()
    {
        super.tick();
        this.volume = volumeSup.get();
    }
}
