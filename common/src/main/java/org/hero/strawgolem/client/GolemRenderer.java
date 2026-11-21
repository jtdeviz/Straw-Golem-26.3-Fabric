package org.hero.strawgolem.client;


import com.geckolib.cache.model.GeoBone;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.builtin.BlockAndItemGeoLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.hero.strawgolem.Constants;
import org.hero.strawgolem.golem.StrawGolem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.hero.strawgolem.registry.DataTicketsRegistry.*;

public class GolemRenderer<R extends LivingEntityRenderState & GeoRenderState>
        extends GeoEntityRenderer<StrawGolem, R> {

    public GolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GolemModel());

        withRenderLayer(new BlockAndItemGeoLayer<>(renderManager, this) {
            private final String itemBone = "item";

            @Override
            protected List<BlockAndItemGeoLayer.RenderData> getRelevantBones(StrawGolem animatable, @Nullable Void relatedObject, R renderState, float partialTick) {
                ItemStack stack = animatable.getMainHandItem();
                if (stack.isEmpty()) {
                    return List.of();
                }
                ItemStackRenderState itemRenderState = new ItemStackRenderState();
                this.itemModelResolver.updateForLiving(itemRenderState, stack, ItemDisplayContext.NONE, animatable);
                return List.of(BlockAndItemGeoLayer.RenderData.item(this.itemBone, ItemDisplayContext.NONE, itemRenderState));
            }

            @Override
            public void addRenderData(StrawGolem animatable, @Nullable Void relatedObject, R renderState, float partialTick) {
                renderState.addGeckolibData(ITEM, animatable.getMainHandItem());
            }

            @Override
            protected void submitItemStackRender(PoseStack poseStack, GeoBone bone, ItemStackRenderState stackState, ItemDisplayContext displayContext, R renderState, SubmitNodeCollector renderTasks, int packedLight) {
                poseStack.scale(0.5f, 0.5f, 0.5f);
                super.submitItemStackRender(poseStack, bone, stackState, displayContext, renderState, renderTasks, packedLight);
            }
        });
    }

    @Override
    public void addRenderData(StrawGolem animatable, @Nullable Void relatedObject, R renderState, float partialTick) {
        renderState.addGeckolibData(FESTIVE, animatable.isFestive());
        renderState.addGeckolibData(SHIVER, animatable.shouldShiver());
        renderState.addGeckolibData(HAT, animatable.hasHat());
        renderState.addGeckolibData(BARREL, animatable.hasBarrel());

        renderState.addGeckolibData(HEALTH, animatable.healthStatus());
        renderState.addGeckolibData(YROT, animatable.getYRot());

        renderState.addGeckolibData(POSITION, animatable.position());
        renderState.addGeckolibData(ANGLE, animatable.getLookAngle());
        renderState.addGeckolibData(ITEM, animatable.getMainHandItem());
    }

    @Override
    public void adjustModelBonesForRender(RenderPassInfo<R> renderPassInfo, BoneSnapshots snapshots) {
        R renderState = renderPassInfo.renderState();
        final boolean hat = Boolean.TRUE.equals(renderState.getGeckolibData(HAT));
        final boolean festive = Boolean.TRUE.equals(renderState.getGeckolibData(FESTIVE));
        final boolean barrel = Boolean.TRUE.equals(renderState.getGeckolibData(BARREL));

        snapshots.get("hat").ifPresent(bone -> bone.skipRender(!hat));
        snapshots.get("dynamicsnow").ifPresent(bone -> bone.skipRender(!festive));
        snapshots.get("snow").ifPresent(bone -> bone.skipRender(!festive || !hat));
        snapshots.get("barrel").ifPresent(bone -> bone.skipRender(!barrel));

        super.adjustModelBonesForRender(renderPassInfo, snapshots);
    }

    @Override
    public void adjustRenderPose(RenderPassInfo<R> renderPassInfo) {
        if (Constants.Golem.shiver && Boolean.TRUE.equals(renderPassInfo.renderState().getGeckolibData(SHIVER))) {
            var random = RandomSource.create();
            double deltaX = random.nextDouble() * 0.02;
            double deltaZ = random.nextDouble() * 0.02;
            renderPassInfo.poseStack().translate(deltaX, 0, deltaZ);
        }
        super.adjustRenderPose(renderPassInfo);
    }
}