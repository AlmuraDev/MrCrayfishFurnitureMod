package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.furniture.tileentity.DisplayItemBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Draws the item lying on a plate or chopping board, turned the way it was put down (1.12.2).
 */
public class DisplayItemBlockEntityRenderer<T extends DisplayItemBlockEntity> implements BlockEntityRenderer<T, DisplayItemBlockEntityRenderer.ItemRenderState>
{
    private final ItemModelResolver itemModelResolver;
    private final float height;

    private DisplayItemBlockEntityRenderer(BlockEntityRendererProvider.Context context, float height)
    {
        this.itemModelResolver = context.itemModelResolver();
        this.height = height;
    }

    public static <T extends DisplayItemBlockEntity> DisplayItemBlockEntityRenderer<T> plate(BlockEntityRendererProvider.Context context)
    {
        return new DisplayItemBlockEntityRenderer<>(context, 0.11F);
    }

    public static <T extends DisplayItemBlockEntity> DisplayItemBlockEntityRenderer<T> choppingBoard(BlockEntityRendererProvider.Context context)
    {
        return new DisplayItemBlockEntityRenderer<>(context, 0.08F);
    }

    @Override
    public ItemRenderState createRenderState()
    {
        return new ItemRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, ItemRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.item = ApplianceRenderUtil.resolve(this.itemModelResolver, blockEntity.getItem(), blockEntity.getLevel());
        state.rotation = blockEntity.getRotation();
    }

    @Override
    public void submit(ItemRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        if(state.item.isEmpty())
            return;

        poseStack.pushPose();
        poseStack.translate(0.5F, this.height, 0.5F);
        poseStack.rotateDegrees(Axis.YP, state.rotation * -90F);
        poseStack.rotateDegrees(Axis.XP, 90F);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    public static class ItemRenderState extends BlockEntityRenderState
    {
        public ItemStackRenderState item = new ItemStackRenderState();
        public int rotation;
    }
}
