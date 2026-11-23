package dev.skyrotor.client.model;

import dev.skyrotor.SkyRotor;
import dev.skyrotor.entity.HelicopterEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class HelicopterModel extends EntityModel<HelicopterEntity> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(SkyRotor.MOD_ID, "helicopter"), "main");

    private final ModelPart root;
    private final ModelPart mainRotor;
    private final ModelPart tailRotor;

    public HelicopterModel(ModelPart root) {
        this.root = root;
        this.mainRotor = root.getChild("main_rotor");
        this.tailRotor = root.getChild("tail_rotor");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition fuselage = root.addOrReplaceChild("fuselage",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -4.0F, -7.0F, 8.0F, 7.0F, 14.0F)
                        .texOffs(0, 24).addBox(-3.5F, -3.5F, -9.0F, 7.0F, 5.0F, 3.0F),
                PartPose.offset(0.0F, -11.0F, 0.0F));

        fuselage.addOrReplaceChild("cockpit_glass",
                CubeListBuilder.create().texOffs(64, 0).addBox(-3.0F, -3.0F, -9.2F, 6.0F, 3.7F, 2.0F),
                PartPose.ZERO);

        fuselage.addOrReplaceChild("side_window_left",
                CubeListBuilder.create().texOffs(64, 16).addBox(-4.1F, -2.6F, -4.0F, 0.2F, 2.8F, 5.0F),
                PartPose.ZERO);
        fuselage.addOrReplaceChild("side_window_right",
                CubeListBuilder.create().texOffs(64, 16).addBox(3.9F, -2.6F, -4.0F, 0.2F, 2.8F, 5.0F),
                PartPose.ZERO);

        root.addOrReplaceChild("tail_boom",
                CubeListBuilder.create().texOffs(0, 42).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 13.0F),
                PartPose.offset(0.0F, -10.0F, 5.0F));

        root.addOrReplaceChild("tail_fin",
                CubeListBuilder.create().texOffs(32, 42).addBox(-0.8F, -5.0F, -1.0F, 1.6F, 6.0F, 2.0F),
                PartPose.offset(0.0F, -8.0F, 17.0F));

        root.addOrReplaceChild("tail_wing",
                CubeListBuilder.create().texOffs(40, 42).addBox(-5.0F, -0.5F, -1.0F, 10.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, -10.0F, 16.0F));

        root.addOrReplaceChild("skid_left",
                CubeListBuilder.create()
                        .texOffs(0, 58).addBox(-0.5F, -0.5F, -8.0F, 1.0F, 1.0F, 18.0F)
                        .texOffs(40, 58).addBox(-0.6F, -3.0F, -5.0F, 1.2F, 3.0F, 1.2F)
                        .texOffs(40, 58).addBox(-0.6F, -3.0F, 4.0F, 1.2F, 3.0F, 1.2F),
                PartPose.offset(-5.0F, -3.0F, 0.0F));

        root.addOrReplaceChild("skid_right",
                CubeListBuilder.create()
                        .texOffs(0, 58).addBox(-0.5F, -0.5F, -8.0F, 1.0F, 1.0F, 18.0F)
                        .texOffs(40, 58).addBox(-0.6F, -3.0F, -5.0F, 1.2F, 3.0F, 1.2F)
                        .texOffs(40, 58).addBox(-0.6F, -3.0F, 4.0F, 1.2F, 3.0F, 1.2F),
                PartPose.offset(5.0F, -3.0F, 0.0F));

        root.addOrReplaceChild("main_rotor",
                CubeListBuilder.create()
                        .texOffs(88, 0).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 8.0F, 2.0F)
                        .texOffs(0, 82).addBox(-19.0F, -8.0F, -1.0F, 38.0F, 1.0F, 2.0F)
                        .texOffs(0, 86).addBox(-1.0F, -8.0F, -19.0F, 2.0F, 1.0F, 38.0F),
                PartPose.offset(0.0F, -14.0F, 0.0F));

        root.addOrReplaceChild("tail_rotor",
                CubeListBuilder.create()
                        .texOffs(88, 18).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F)
                        .texOffs(92, 18).addBox(-4.0F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, -8.0F, 18.5F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(HelicopterEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        mainRotor.yRot = ageInTicks * (entity.isEngineOn() ? 1.25F : 0.08F);
        tailRotor.zRot = ageInTicks * (entity.isEngineOn() ? 1.8F : 0.12F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
