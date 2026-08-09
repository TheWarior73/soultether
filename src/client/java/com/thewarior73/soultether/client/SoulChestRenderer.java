package com.thewarior73.soultether.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thewarior73.soultether.Blocks.SoulChestBlock;
import com.thewarior73.soultether.Blocks.SoulChestBlockEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class SoulChestRenderer implements BlockEntityRenderer<SoulChestBlockEntity, SoulChestRenderState> {
    private final ChestModel model;
    private final SpriteGetter sprites;
    private static final Identifier BLOCK_ATLAS = Identifier.withDefaultNamespace("textures/atlas/blocks.png");
    private static final SpriteId SOUL_CHEST_SPRITE_ID = new SpriteId(BLOCK_ATLAS, Identifier.fromNamespaceAndPath("soultether", "block/soul_chest"));
    private static final SpriteId SECURE_SOUL_CHEST_SPRITE_ID = new SpriteId(BLOCK_ATLAS, Identifier.fromNamespaceAndPath("soultether", "block/secure_soul_chest"));

    public SoulChestRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
        this.sprites = context.sprites();
    }

    @Override
    public SoulChestRenderState createRenderState() {
        return new SoulChestRenderState();
    }

    @Override
    public void extractRenderState(SoulChestBlockEntity entity, SoulChestRenderState state, float tickDelta, @NonNull Vec3 pos, CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(entity, state, breakProgress);
        state.facing = entity.getBlockState().getValue(SoulChestBlock.FACING);
        state.open = entity.getOpenNess(tickDelta);
        state.isSecure = (entity instanceof com.thewarior73.soultether.Blocks.SecureSoulChestBlockEntity);
    }

    @Override
    public void submit(SoulChestRenderState state, PoseStack poseStack, SubmitNodeCollector collector, @NonNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(ChestRenderer.modelTransformation(state.facing));

        float openness = state.open;
        openness = 1.0F - openness;
        openness = 1.0F - openness * openness * openness;

        SpriteId spriteId = state.isSecure ? SECURE_SOUL_CHEST_SPRITE_ID : SOUL_CHEST_SPRITE_ID;

        collector.submitModel(
            this.model,
            openness,
            poseStack,
            state.lightCoords,
            OverlayTexture.NO_OVERLAY,
            -1,
            spriteId,
            this.sprites,
            0,
            state.breakProgress
        );
        poseStack.popPose();
    }
}
