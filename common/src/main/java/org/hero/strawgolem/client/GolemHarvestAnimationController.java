package org.hero.strawgolem.client;

import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.hero.strawgolem.Constants;
import org.hero.strawgolem.client.particle.SimplerParticleType;
import org.hero.strawgolem.golem.StrawGolem;

import static org.hero.strawgolem.registry.DataTicketsRegistry.*;
import static org.hero.strawgolem.registry.ParticleRegistry.snow;
import static org.hero.strawgolem.registry.ParticleRegistry.snowfall;

public class GolemHarvestAnimationController extends AnimationController<StrawGolem> {

    private static final RawAnimation[] harvest = {
            RawAnimation.begin().thenPlay("harvest_item"),
            RawAnimation.begin().thenPlay("harvest_block")
    };

    private static final AnimationStateHandler<StrawGolem> PREDICATE = event -> {
        StrawGolem golem = event.animatable();
        AnimationController<StrawGolem> controller = event.controller();
        int status = golem.pickupStatus();
        if (status != 0) {
            status--;
            if (controller.hasAnimationFinished()) {
                controller.reset();
            }
            if (golem.hasBarrel() && status < 1) status++;
            status = Math.max(0, Math.min(harvest.length - 1, status));

            return event.setAndContinue(harvest[status]);
        }
        event.controller().reset();
        return PlayState.STOP;
    };

    public GolemHarvestAnimationController() {
        super("harvest_handler", Constants.Animation.TRANSITION_TIME, PREDICATE);
        setCustomInstructionKeyframeHandler(event -> {
            if (event.keyframeData().getInstructions().equals("completeHarvest")) {}
        });
        setParticleKeyframeHandler(event -> {
            if (event.renderState().getGeckolibData(FESTIVE) && event.keyframeData().getEffect().equals("strawgolem:snow")) {
                spawnParticleAtLocator(event.renderState(), (SimplerParticleType) snow.get(), (SimplerParticleType) snowfall.get());
            }
        });
    }

    public void spawnParticleAtLocator(GeoRenderState renderState, SimplerParticleType snow, SimplerParticleType snowfall) {
        Vec3 basePos = renderState.getGeckolibData(POSITION);
        if (basePos == null) return;
        Vec3 pos = basePos.add(0, 0.75, 0);
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;
        double speed = 1.0;
        Vec3 lookAngle = renderState.getGeckolibData(ANGLE);
        double velX = lookAngle.x * speed;
        double velZ = lookAngle.z * speed;
        var random = RandomSource.create();
        for (int i = 0; i < 3; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 0.4;
            double offsetZ = (random.nextDouble() - 0.5) * 0.4;
            Minecraft.getInstance().particleEngine.createParticle(snowfall, x + offsetX, y, z + offsetZ, velX * 2, 0, velZ * 2);
        }
        for (int i = 0; i < 100; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 0.4;
            double offsetZ = (random.nextDouble() - 0.5) * 0.4;
            Minecraft.getInstance().particleEngine.createParticle(snow, x + offsetX, y, z + offsetZ, velX, 0, velZ);
        }
    }
}