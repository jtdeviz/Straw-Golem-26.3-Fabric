package org.hero.strawgolem.client;

import org.hero.strawgolem.Constants;
import org.hero.strawgolem.golem.StrawGolem;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.RawAnimation;

public class GolemArmAnimationController extends AnimationController<StrawGolem> {

    private static final RawAnimation[] arms = {
            RawAnimation.begin().thenPlay("arms_idle"),
            RawAnimation.begin().thenPlay("arms_walk"),
            RawAnimation.begin().thenPlay("arms_run"),
            RawAnimation.begin().thenPlay("arms_hold_item"),
            RawAnimation.begin().thenPlay("arms_hold_block"),
            RawAnimation.begin().thenPlay("arms_scared")
    };

    private static final AnimationStateHandler<StrawGolem> PREDICATE = event -> {
        StrawGolem golem = event.animatable();
        // if the golem is picking a block up
        if (golem.pickupStatus() != 0 || golem.shouldForceAnimationReset()) return PlayState.STOP;
        AnimationController<StrawGolem> controller = event.controller();
        if (controller.hasAnimationFinished()) {
            controller.reset();
        }
        if (golem.getPanic()) {
            controller.setAnimation(arms[5]);
        } else if (golem.holdItemAbove()) {
            controller.setAnimation(arms[4]);
        } else if (golem.carryStatus() != 0) {
            controller.setAnimation(arms[3]);
        } else {
            controller.setAnimation(arms[golem.movementStatus()]);
        }
        return PlayState.CONTINUE;
    };

    public GolemArmAnimationController() {
        super("arms_handler", Constants.Animation.TRANSITION_TIME, PREDICATE);
    }
}
