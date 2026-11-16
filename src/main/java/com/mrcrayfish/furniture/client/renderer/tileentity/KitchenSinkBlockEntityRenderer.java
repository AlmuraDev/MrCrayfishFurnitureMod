package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mrcrayfish.furniture.block.FurnitureHorizontalBlock;
import com.mrcrayfish.furniture.tileentity.KitchenSinkBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.Nullable;

/**
 * Author: MrCrayfish
 */
public class KitchenSinkBlockEntityRenderer implements BlockEntityRenderer<KitchenSinkBlockEntity, KitchenSinkBlockEntityRenderer.KitchenSinkRenderState>
{
    public KitchenSinkBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public KitchenSinkRenderState createRenderState()
    {
        return new KitchenSinkRenderState();
    }

    @Override
    public void extractRenderState(KitchenSinkBlockEntity tileEntity, KitchenSinkRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(tileEntity, state, partialTicks, cameraPosition, breakProgress);
        state.direction = tileEntity.getBlockState().getValue(FurnitureHorizontalBlock.DIRECTION);
        state.sprite = null;

        FluidStack fluidStack = tileEntity.getFluid();
        int capacity = tileEntity.getCapacity();
        if(fluidStack.isEmpty() || capacity <= 0)
            return;

        FluidState fluidState = fluidStack.getFluid().defaultFluidState();
        FluidModel model = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
        state.sprite = model.stillMaterial().sprite();

        int colour = -1;
        FluidTintSource tint = model.fluidTintSource();
        if(tint != null)
        {
            colour = tileEntity.getLevel() instanceof BlockAndTintGetter level ? tint.colorInWorld(fluidState, tileEntity.getBlockState(), level, tileEntity.getBlockPos()) : tint.colorAsStack(fluidStack);
        }
        state.colour = ARGB.opaque(colour);
        state.fill = Math.min(1F, (float) ((double) fluidStack.getAmount() / (double) capacity));
    }

    @Override
    public void submit(KitchenSinkRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if(state.sprite == null)
            return;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.rotateDegrees(Axis.YP, state.direction.get2DDataValue() * -90F - 90F);
        poseStack.translate(-0.5, -0.5, -0.5);
        this.drawFluid(state, poseStack, submitNodeCollector, 2F * 0.0625F, 10F * 0.0625F, 2F * 0.0625F, 10F * 0.0625F, 5F * 0.0625F, 12F * 0.0625F);
        poseStack.popPose();
    }

    private void drawFluid(KitchenSinkRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, float x, float y, float z, float width, float height, float depth)
    {
        TextureAtlasSprite sprite = state.sprite;
        float minU = sprite.getU0();
        float maxU = Math.min(minU + (sprite.getU1() - minU) * depth, sprite.getU1());
        float minV = sprite.getV0();
        float maxV = Math.min(minV + (sprite.getV1() - minV) * width, sprite.getV1());
        int colour = state.colour;
        int light = state.lightCoords;
        float top = y + height * state.fill;

        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(), (pose, consumer) -> {
            vertex(consumer, pose, x, top, z, colour, maxU, minV, light);
            vertex(consumer, pose, x, top, z + depth, colour, minU, minV, light);
            vertex(consumer, pose, x + width, top, z + depth, colour, minU, maxV, light);
            vertex(consumer, pose, x + width, top, z, colour, maxU, maxV, light);
        });
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, int colour, float u, float v, int light)
    {
        consumer.addVertex(pose, x, y, z).setColor(colour).setUv(u, v).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    public static class KitchenSinkRenderState extends BlockEntityRenderState
    {
        public Direction direction = Direction.NORTH;
        /** The fluid's still texture, or null when the sink is empty */
        @Nullable
        public TextureAtlasSprite sprite;
        public int colour = -1;
        /** How full the sink is, from 0 to 1 */
        public float fill;
    }
}
