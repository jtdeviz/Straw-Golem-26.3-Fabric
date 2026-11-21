package org.hero.strawgolem.client;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.resources.Identifier;
import org.hero.strawgolem.Constants;
import org.hero.strawgolem.golem.StrawGolem;

import static org.hero.strawgolem.registry.DataTicketsRegistry.*;

public class GolemModel extends GeoModel<StrawGolem> {
    private static final Identifier model = Identifier.tryBuild(Constants.MODID, "strawgolem");
    private static final Identifier animation = Identifier.tryBuild(Constants.MODID, "strawgolem");
    private static final Identifier[] textures = {
            Identifier.tryBuild(Constants.MODID, "textures/straw_golem.png"),
            Identifier.tryBuild(Constants.MODID, "textures/straw_golem_old.png"),
            Identifier.tryBuild(Constants.MODID, "textures/straw_golem_dying.png"),
            Identifier.tryBuild(Constants.MODID, "textures/snow.png"),
            Identifier.tryBuild(Constants.MODID, "textures/snow_old.png"),
            Identifier.tryBuild(Constants.MODID, "textures/snow_dying.png")
    };

    @Override
    public Identifier getModelResource(GeoRenderState state) {
        return model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState state) {
        var healthStatus = state.getGeckolibData(HEALTH);
        var festive = state.getGeckolibData(FESTIVE);
        if (healthStatus == null || festive == null) {
            return textures[0];
        }
        return textures[healthStatus + (festive ? 3 : 0)];
    }

    @Override
    public Identifier getAnimationResource(StrawGolem strawGolem) {
        return animation;
    }
}
