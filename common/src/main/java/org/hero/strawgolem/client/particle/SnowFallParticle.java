package org.hero.strawgolem.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;

public class SnowFallParticle extends SingleQuadParticle {
    private final SpriteSet spriteSet;

    public SnowFallParticle(ClientLevel level, double x, double y, double z, double xv, double yv, double zv, SpriteSet spriteSet) {
        super(level, x, y, z, spriteSet.first());
        this.spriteSet = spriteSet;
        this.gravity = 2.0f;
        this.quadSize = 0.2f;
        this.lifetime = 6;
        float tmp = (float) (level.getRandom().nextFloat() * 2 * Math.PI);
        roll = tmp;
        oRoll = tmp;
        friction = 0;
        setParticleSpeed(xv / 20, yv / 20, zv / 20);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(spriteSet);
        this.oRoll = roll;
        super.tick();
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }
}