package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.furniture.tileentity.GrillBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Author: MrCrayfish
 */
public class GrillBlockEntityRenderer implements BlockEntityRenderer<GrillBlockEntity, GrillBlockEntityRenderer.GrillRenderState>
{
    private final ItemModelResolver itemModelResolver;

    public GrillBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public GrillRenderState createRenderState()
    {
        return new GrillRenderState();
    }

    @Override
    public void extractRenderState(GrillBlockEntity tileEntity, GrillRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(tileEntity, state, partialTicks, cameraPosition, breakProgress);

        NonNullList<ItemStack> grill = tileEntity.getGrill();
        byte[] rotations = tileEntity.getRotations();
        state.grill = new ItemStackRenderState[grill.size()];
        state.rotations = new int[grill.size()];
        state.flipProgress = new float[grill.size()];
        state.flipped = new boolean[grill.size()];
        for(int j = 0; j < grill.size(); j++)
        {
            state.grill[j] = this.resolve(tileEntity, grill.get(j));
            state.rotations[j] = rotations[j];
            state.flipProgress[j] = tileEntity.isFlipping(j) ? Mth.clamp(tileEntity.getFlippingCount(j) + partialTicks, 0F, GrillBlockEntity.MAX_FLIPPING_COUNTER) / GrillBlockEntity.MAX_FLIPPING_COUNTER : -1F;
            state.flipped[j] = tileEntity.isFlipped(j);
        }

        NonNullList<ItemStack> fuel = tileEntity.getFuel();
        state.fuel = new ItemStackRenderState[fuel.size()];
        for(int j = 0; j < fuel.size(); j++)
        {
            state.fuel[j] = this.resolve(tileEntity, fuel.get(j));
        }
    }

    private ItemStackRenderState resolve(GrillBlockEntity tileEntity, ItemStack stack)
    {
        ItemStackRenderState itemState = new ItemStackRenderState();
        if(!stack.isEmpty())
        {
            this.itemModelResolver.updateForTopItem(itemState, stack, ItemDisplayContext.FIXED, tileEntity.getLevel(), null, 0);
        }
        return itemState;
    }

    @Override
    public void submit(GrillRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        for(int j = 0; j < state.grill.length; j++)
        {
            ItemStackRenderState itemState = state.grill[j];
            if(!itemState.isEmpty())
            {
                poseStack.pushPose();

                float progress = state.flipProgress[j];
                boolean flipping = progress >= 0F;
                if(flipping)
                {
                    poseStack.translate(0.0, Math.sin(Math.toRadians(180 * progress)), 0.0);
                }

                poseStack.translate(0.5, 1.0, 0.5);
                poseStack.rotateDegrees(Axis.XP, 90F);
                poseStack.translate(-0.2 + 0.4 * (j % 2), -0.2 + 0.4 * (j / 2), 0.0);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                poseStack.rotateDegrees(Axis.ZP, 90F * state.rotations[j]);

                if(flipping)
                {
                    poseStack.rotateDegrees(Axis.XP, -540F * progress);
                }
                else if(state.flipped[j])
                {
                    poseStack.rotateDegrees(Axis.XP, 180F);
                }

                itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

                poseStack.popPose();
            }
        }

        for(int j = 0; j < state.fuel.length; ++j)
        {
            ItemStackRenderState itemState = state.fuel[j];
            if(!itemState.isEmpty())
            {
                poseStack.pushPose();
                poseStack.translate(0.5, 0.85, 0.5);
                poseStack.rotateDegrees(Axis.XP, 90F);
                poseStack.translate(-0.2 + 0.2 * (j % 3), -0.2 + 0.2 * (j / 3), 0.0);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                poseStack.rotateDegrees(Axis.YP, 10F);
                poseStack.rotateDegrees(Axis.ZP, 10F);
                poseStack.rotateDegrees(Axis.XP, 5F);
                itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
        }
    }

    /**
     * Flipped items jump up to a block above the grill, so keep rendering them while only that space is on screen
     */
    @Override
    public AABB getRenderBoundingBox(GrillBlockEntity tileEntity)
    {
        BlockPos pos = tileEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 2.5, pos.getZ() + 1.0);
    }

    public static class GrillRenderState extends BlockEntityRenderState
    {
        public ItemStackRenderState[] grill = new ItemStackRenderState[0];
        public int[] rotations = new int[0];
        /** How far through its flip each grill item is (0 to 1), or -1 when it isn't flipping */
        public float[] flipProgress = new float[0];
        public boolean[] flipped = new boolean[0];
        public ItemStackRenderState[] fuel = new ItemStackRenderState[0];
    }
}
