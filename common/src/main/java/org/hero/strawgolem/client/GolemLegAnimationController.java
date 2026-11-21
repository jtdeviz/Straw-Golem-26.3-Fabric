package org.hero.strawgolem.client;

import org.hero.strawgolem.Constants;
import org.hero.strawgolem.golem.StrawGolem;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.RawAnimation;

public class GolemLegAnimationController extends AnimationController<StrawGolem> {

    private static final RawAnimation[] legs = {
            RawAnimation.begin().thenLoop("legs_idle"),
            RawAnimation.begin().thenLoop("legs_walk"),
            RawAnimation.begin().thenLoop("legs_run")
    };

    private static final AnimationStateHandler<StrawGolem> PREDICATE = event -> {
        StrawGolem golem = event.animatable();
        // if the golem is picking a block up
        if (golem.pickupStatus() != 0) return PlayState.STOP;
        AnimationController<StrawGolem> controller = event.controller();
        if (controller.hasAnimationFinished()) {
            controller.reset();
        }
        controller.setAnimation(legs[golem.movementStatus()]);
        return PlayState.CONTINUE;
    };

    public GolemLegAnimationController() {
        super("legs_handler", Constants.Animation.TRANSITION_TIME, PREDICATE);
    }
}