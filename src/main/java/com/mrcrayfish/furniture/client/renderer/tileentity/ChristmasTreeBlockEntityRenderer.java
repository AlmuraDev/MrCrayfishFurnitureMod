package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.furniture.block.ChristmasTreeBlock;
import com.mrcrayfish.furniture.tileentity.ChristmasTreeBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Draws the ornaments hung on a Christmas tree, one on each side, tilted out a little (1.12.2). The bottom half is wider, so its
 * ornaments hang further out.
 */
public class ChristmasTreeBlockEntityRenderer implements BlockEntityRenderer<ChristmasTreeBlockEntity, ChristmasTreeBlockEntityRenderer.TreeRenderState>
{
    private final ItemModelResolver itemModelResolver;

    public ChristmasTreeBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public TreeRenderState createRenderState()
    {
        return new TreeRenderState();
    }

    @Override
    public void extractRenderState(ChristmasTreeBlockEntity tree, TreeRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(tree, state, partialTicks, cameraPosition, breakProgress);
        state.bottom = tree.getBlockState().getValue(ChristmasTreeBlock.HALF) == DoubleBlockHalf.LOWER;
        NonNullList<ItemStack> ornaments = tree.getOrnaments();
        state.ornaments = new ItemStackRenderState[ornaments.size()];
        for(int i = 0; i < ornaments.size(); i++)
        {
            state.ornaments[i] = ApplianceRenderUtil.resolve(this.itemModelResolver, ornaments.get(i), tree.getLevel());
        }
    }

    @Override
    public void submit(TreeRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        float spread = state.bottom ? 0.45F : 0.3F;
        float height = state.bottom ? 0.65F : 0.15F;
        for(int i = 0; i < state.ornaments.length; i++)
        {
            ItemStackRenderState ornament = state.ornaments[i];
            if(ornament.isEmpty())
                continue;

            poseStack.pushPose();
            poseStack.translate(0.5F, height, 0.5F);
            // Side i faces the way a player looked when hanging it (south, west, north, east)
            poseStack.rotateDegrees(Axis.YP, 90F - 90F * i);
            poseStack.translate(spread, 0F, 0F);
            poseStack.rotateDegrees(Axis.YP, 90F);
            poseStack.rotateDegrees(Axis.XP, -15F);
            poseStack.scale(0.45F, 0.45F, 0.45F);
            ornament.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    public static class TreeRenderState extends BlockEntityRenderState
    {
        public boolean bottom;
        public ItemStackRenderState[] ornaments = new ItemStackRenderState[0];
    }
}
