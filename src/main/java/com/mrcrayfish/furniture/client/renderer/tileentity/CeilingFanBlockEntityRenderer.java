package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.tileentity.CeilingFanBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Draws a ceiling fan's turning blades (1.12.2). The blades are their own model (items/ceiling_fan_blades), drawn through a stack that
 * only borrows that model, so there is no blades item in the game.
 */
public class CeilingFanBlockEntityRenderer implements BlockEntityRenderer<CeilingFanBlockEntity, CeilingFanBlockEntityRenderer.FanRenderState>
{
    private static final ItemStack BLADES = new ItemStack(Items.STICK);

    static
    {
        BLADES.set(DataComponents.ITEM_MODEL, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "ceiling_fan_blades"));
    }

    private final ItemModelResolver itemModelResolver;

    public CeilingFanBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public FanRenderState createRenderState()
    {
        return new FanRenderState();
    }

    @Override
    public void extractRenderState(CeilingFanBlockEntity fan, FanRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(fan, state, partialTicks, cameraPosition, breakProgress);
        state.rotation = fan.getRotation(partialTicks);
        this.itemModelResolver.updateForTopItem(state.blades, BLADES, ItemDisplayContext.NONE, fan.getLevel(), null, 0);
    }

    @Override
    public void submit(FanRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.scale(1.35F, 1.0F, 1.35F);
        poseStack.rotateDegrees(Axis.YP, -state.rotation);
        state.blades.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    // The blades reach well past the block
    @Override
    public AABB getRenderBoundingBox(CeilingFanBlockEntity fan)
    {
        return new AABB(fan.getBlockPos()).inflate(1.0);
    }

    public static class FanRenderState extends BlockEntityRenderState
    {
        public float rotation;
        public ItemStackRenderState blades = new ItemStackRenderState();
    }
}
