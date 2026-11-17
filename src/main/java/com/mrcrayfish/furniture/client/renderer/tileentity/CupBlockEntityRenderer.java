package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.furniture.tileentity.CupBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Draws the drink in a cup that has been set down, in the drink's colour (1.12.2).
 */
public class CupBlockEntityRenderer implements BlockEntityRenderer<CupBlockEntity, CupBlockEntityRenderer.CupRenderState>
{
    public CupBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public CupRenderState createRenderState()
    {
        return new CupRenderState();
    }

    @Override
    public void extractRenderState(CupBlockEntity cup, CupRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(cup, state, partialTicks, cameraPosition, breakProgress);
        ItemStack drink = cup.getDrink();
        state.sprite = drink.isEmpty() ? null : ApplianceRenderUtil.liquidSprite();
        state.colour = ARGB.opaque(DyedItemColor.getOrDefault(drink, -1));
    }

    @Override
    public void submit(CupRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        if(state.sprite != null)
        {
            ApplianceRenderUtil.liquid(poseStack, collector, state.sprite, state.colour, state.lightCoords, 0.376F, 0.03125F, 0.376F, 0.624F, 0.4F, 0.624F);
        }
    }

    public static class CupRenderState extends BlockEntityRenderState
    {
        @Nullable
        public TextureAtlasSprite sprite;
        public int colour = -1;
    }
}
