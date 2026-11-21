package org.hero.strawgolem.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;

public class SnowParticle extends SingleQuadParticle {
    private final SpriteSet spriteSet;
    private float rotSpeed = 0.0f;
    private final float rotAccel = 10.0f;
    private final float rotDrag = 0.5f;

    public SnowParticle(ClientLevel level, double x, double y, double z, double xv, double yv, double zv, SpriteSet spriteSet) {
        super(level, x, y, z, spriteSet.first());
        this.spriteSet = spriteSet;
        this.gravity = 0.5f;
        this.quadSize = 0.02f;
        this.lifetime = 50;
        float tmp = (float) (level.getRandom().nextFloat() * 2 * Math.PI);
        roll = tmp;
        oRoll = tmp;
        this.friction = 0.9f;
        setParticleSpeed(xv / 20, yv / 20, zv / 20);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(spriteSet);
        this.oRoll = roll;
        if (!this.onGround) {
            this.rotSpeed += (this.rotAccel / 20.0f);
            this.rotSpeed *= (1.0f - (this.rotDrag / 20.0f));
            this.roll += rotSpeed;
        }
        super.tick();
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }
}