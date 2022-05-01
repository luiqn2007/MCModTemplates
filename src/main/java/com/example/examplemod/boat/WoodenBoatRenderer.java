package com.example.examplemod.boat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class WoodenBoatRenderer extends EntityRenderer<WoodenBoatEntity> {

    private final EntityRendererProvider.Context context;
    @Nullable
    private ResourceLocation modelLocation;
    @Nullable
    private BoatModel model;

    public WoodenBoatRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.context = context;
        this.shadowRadius = 0.8F;
    }

    @Override
    public void render(WoodenBoatEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.0D, 0.375D, 0.0D);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - pEntityYaw));
        float f = pEntity.getHurtTime() - pPartialTicks;
        float f1 = Math.max(pEntity.getDamage() - pPartialTicks, 0);
        if (f > 0.0F) {
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10 * pEntity.getHurtDir()));
        }

        float f2 = pEntity.getBubbleAngle(pPartialTicks);
        if (!Mth.equal(f2, 0.0F)) {
            pMatrixStack.mulPose(new Quaternion(new Vector3f(1, 0, 1), pEntity.getBubbleAngle(pPartialTicks), true));
        }

        BoatModel model = getModel(pEntity);
        pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        model.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(model.renderType(getTextureLocation(pEntity)));
        model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!pEntity.isUnderWater()) {
            VertexConsumer buffer = pBuffer.getBuffer(RenderType.waterMask());
            model.waterPatch().render(pMatrixStack, buffer, pPackedLight, OverlayTexture.NO_OVERLAY);
        }

        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(WoodenBoatEntity boat) {
        if (modelLocation == null) {
            modelLocation = boat.boat()
                    .map(wb -> wb.name)
                    .map(name -> new ResourceLocation(name.getNamespace(), "textures/entity/boat/" + name.getPath() + ".png"))
                    .orElseGet(() -> new ResourceLocation("textures/entity/boat/" + Boat.Type.OAK.getName() + ".png"));
        }
        return modelLocation;
    }

    public BoatModel getModel(WoodenBoatEntity boat) {
        if (model == null) {
            model = boat.boat().map(b -> new BoatModel(context.bakeLayer(b.boatLayer())))
                    .orElseGet(() -> new BoatModel(context.bakeLayer(ModelLayers.createBoatModelName(Boat.Type.OAK))));
        }
        return model;
    }
}
