package dev.skyrotor.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.skyrotor.SkyRotor;
import dev.skyrotor.client.model.HelicopterModel;
import dev.skyrotor.entity.HelicopterEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class HelicopterRenderer extends EntityRenderer<HelicopterEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SkyRotor.MOD_ID,
            "textures/entity/helicopter.png");
    private final HelicopterModel model;

    public HelicopterRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new HelicopterModel(context.bakeLayer(HelicopterModel.LAYER_LOCATION));
        this.shadowRadius = 1.2F;
    }

    @Override
    public void render(HelicopterEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTick, 0.0F, 0.0F);
        model.renderToBuffer(poseStack,
                buffer.getBuffer(net.minecraft.client.renderer.RenderType.entityCutout(TEXTURE)),
                packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(HelicopterEntity entity) {
        return TEXTURE;
    }
}
