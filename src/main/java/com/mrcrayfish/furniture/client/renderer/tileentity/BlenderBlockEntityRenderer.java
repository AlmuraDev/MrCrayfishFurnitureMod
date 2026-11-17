package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.furniture.tileentity.BlenderBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Draws the blender's ingredients spinning in the jug, and the drink: it fades in while blending, then goes down as cups are filled
 * (1.12.2).
 */
public class BlenderBlockEntityRenderer implements BlockEntityRenderer<BlenderBlockEntity, BlenderBlockEntityRenderer.BlenderRenderState>
{
    private final ItemModelResolver itemModelResolver;

    public BlenderBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public BlenderRenderState createRenderState()
    {
        return new BlenderRenderState();
    }

    @Override
    public void extractRenderState(BlenderBlockEntity blender, BlenderRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(blender, state, partialTicks, cameraPosition, breakProgress);
        NonNullList<ItemStack> ingredients = blender.getIngredients();
        state.ingredients = new ItemStackRenderState[ingredients.size()];
        for(int i = 0; i < ingredients.size(); i++)
        {
            state.ingredients[i] = ApplianceRenderUtil.resolve(this.itemModelResolver, ingredients.get(i), blender.getLevel());
        }

        float progress = blender.isRunning() ? Math.min(blender.getProgress() + partialTicks, BlenderBlockEntity.BLEND_TIME) : 0F;
        state.spin = progress * 18F;
        state.sprite = null;
        ItemStack drink = blender.getDrink();
        if(!drink.isEmpty() && (blender.isRunning() || blender.hasDrinks()))
        {
            int rgb = DyedItemColor.getOrDefault(drink, -1);
            float alpha = blender.isRunning() ? progress / BlenderBlockEntity.BLEND_TIME : 1F;
            state.colour = ARGB.color(Math.round(alpha * 255), rgb);
            state.height = blender.isRunning() ? 0.85F : 0.325F + 0.525F * blender.getDrinkCount() / BlenderBlockEntity.DRINKS;
            state.sprite = ApplianceRenderUtil.liquidSprite();
        }
    }

    @Override
    public void submit(BlenderRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        for(int i = 0; i < state.ingredients.length; i++)
        {
            ItemStackRenderState item = state.ingredients[i];
            if(item.isEmpty())
                continue;

            poseStack.pushPose();
            poseStack.translate(0.5, 0.4, 0.5);
            poseStack.rotateDegrees(Axis.YP, i * -90F + state.spin);
            poseStack.translate(0.0, 0.0, 0.05);
            poseStack.scale(0.3F, 0.3F, 0.3F);
            item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if(state.sprite != null)
        {
            ApplianceRenderUtil.liquid(poseStack, collector, state.sprite, state.colour, state.lightCoords, 0.3F, 0.325F, 0.3F, 0.7F, state.height, 0.7F);
        }
    }

    public static class BlenderRenderState extends BlockEntityRenderState
    {
        public ItemStackRenderState[] ingredients = new ItemStackRenderState[0];
        public float spin;
        /** The drink's texture, or null when there is no drink to draw */
        @Nullable
        public TextureAtlasSprite sprite;
        public int colour = -1;
        public float height;
    }
}
