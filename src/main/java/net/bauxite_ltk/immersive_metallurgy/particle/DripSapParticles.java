package net.bauxite_ltk.immersive_metallurgy.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class DripSapParticles extends TextureSheetParticle {

    private final SpriteSet sprites;
    private int phase;            // 0=hang, 1=fall, 2=land
    private int hangTicksLeft;

    // === 构造器 ===
    private DripSapParticles(ClientLevel level, double x, double y, double z,
                               double xSpeed, double ySpeed, double zSpeed,
                               SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;

        // ---- START ----
        this.phase = 0;
        this.hangTicksLeft = 10 + level.random.nextInt(20); // hang phase 10~30 tick
        this.gravity = 0;
        this.friction = 1.0F;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        this.lifetime = 120;                // 最长存活
        this.quadSize = 0.1F;              // 水滴大小
        this.alpha = 0.92F;

        // 颜色（可改，比如琥珀色树脂滴）
        this.rCol = 0.45F;
        this.gCol = 0.30F;
        this.bCol = 0.12F;

        // 先用悬挂帧
        this.setSprite(sprites.get(0, 2)); // 取第0帧 (hang)
    }

    // === 渲染类型：透明粒子 → 需要 PARTICLE_SHEET_TRANSLUCENT ===
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    // === 核心：每 tick 行为 ===
    @Override
    public void tick() {
        this.oRoll = this.roll;
        // 微幅颤动（水平漂移）
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.phase == 0) {
            // ── HANG ──
            this.hangTicksLeft--;
            if (this.hangTicksLeft <= 0) {
                startFalling();
            }

        } else if (this.phase == 1) {
            // ── FALL──
            this.yd -= 0.06F;
            this.move(this.xd, this.yd, this.zd);

            if (this.onGround) {
                // FALL -> LAND
                startLanding();
            }

        } else {
            // ── LAND ──
            this.yd = 0;
            this.alpha *= 0.7F;
            if (this.alpha < 0.01F) {
                this.remove();
            }
        }

        this.age++;
        if (this.age >= this.lifetime) {
            this.remove();
        }
    }

    private void startFalling() {
        this.phase = 1;
        this.gravity = 0.06F;
        this.friction = 0.98F;
        this.yd = -0.03F; // 初始下落速度
        this.quadSize = 0.09F;

        this.setSprite(this.sprites.get(1, 2));

    }

    private void startLanding() {
        this.phase = 2;
        this.onGround = false;
        this.yd = 0;
        this.xd = 0;
        this.zd = 0;
        this.gravity = 0;
        this.quadSize = 0.18F;
        this.alpha = 0.85F;
        this.lifetime = this.age + 8;    // 8 tick 后消掉

        float f = Mth.randomBetween(this.random, 0.6F, 1.0F);
        this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER, SoundSource.BLOCKS, f, 1.0F, false);

        this.setSprite(this.sprites.get(2, 2));

    }


    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new DripSapParticles(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
