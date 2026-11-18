package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.furniture.block.CookieJarBlock;
import com.mrcrayfish.furniture.tileentity.CookieJarBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Draws the cookies stacked in a cookie jar (1.12.2).
 */
public class CookieJarBlockEntityRenderer implements BlockEntityRenderer<CookieJarBlockEntity, CookieJarBlockEntityRenderer.JarRenderState>
{
    private final ItemModelResolver itemModelResolver;

    public CookieJarBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public JarRenderState createRenderState()
    {
        return new JarRenderState();
    }

    @Override
    public void extractRenderState(CookieJarBlockEntity jar, JarRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(jar, state, partialTicks, cameraPosition, breakProgress);
        state.cookies = jar.getBlockState().hasProperty(CookieJarBlock.COOKIES) ? jar.getBlockState().getValue(CookieJarBlock.COOKIES) : 0;
        if(state.cookies > 0)
        {
            state.cookie = ApplianceRenderUtil.resolve(this.itemModelResolver, new ItemStack(Items.COOKIE), jar.getLevel());
        }
    }

    @Override
    public void submit(JarRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        for(int i = 0; i < state.cookies; i++)
        {
            poseStack.pushPose();
            poseStack.translate(0.5F, 0.06F + 0.07F * i, 0.5F);
            poseStack.rotateDegrees(Axis.YP, i * 37F);
            poseStack.rotateDegrees(Axis.XP, 90F);
            poseStack.scale(0.4F, 0.4F, 0.4F);
            state.cookie.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    public static class JarRenderState extends BlockEntityRenderState
    {
        public int cookies;
        public ItemStackRenderState cookie = new ItemStackRenderState();
    }
}
