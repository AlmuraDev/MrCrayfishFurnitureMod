package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.furniture.block.FurnitureHorizontalBlock;
import com.mrcrayfish.furniture.tileentity.DigitalClockBlockEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

/**
 * Draws the time of day on a digital clock (1.12.2), in the colour its numbers were dyed.
 */
public class DigitalClockBlockEntityRenderer implements BlockEntityRenderer<DigitalClockBlockEntity, DigitalClockBlockEntityRenderer.ClockRenderState>
{
    private final Font font;

    public DigitalClockBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.font = context.font();
    }

    @Override
    public ClockRenderState createRenderState()
    {
        return new ClockRenderState();
    }

    @Override
    public void extractRenderState(DigitalClockBlockEntity clock, ClockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(clock, state, partialTicks, cameraPosition, breakProgress);
        state.direction = clock.getBlockState().getValue(FurnitureHorizontalBlock.DIRECTION).get2DDataValue();
        state.colour = ARGB.opaque(clock.getTextColour().getTextColor());
        long time = clock.getLevel() != null ? clock.getLevel().getDefaultClockTime() : 0;
        state.time = formatTime(time);
    }

    // Day time 0 is six in the morning
    private static String formatTime(long time)
    {
        long ticks = Math.floorMod(time, 24000L);
        long hours = (ticks / 1000 + 6) % 24;
        long minutes = (ticks % 1000) * 60 / 1000;
        return String.format(Locale.ROOT, "%02d:%02d", hours, minutes);
    }

    @Override
    public void submit(ClockRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.rotateDegrees(Axis.YP, state.direction * -90F + 180F);
        poseStack.translate(0.0675 - 4.2 * 0.0625, 0.005 - 5.0 * 0.0625, -0.032 + 1.55 * 0.0625);
        float scale = 0.010416667F * 1.5F;
        poseStack.scale(scale, -scale, scale);
        collector.submitText(poseStack, 0F, 0F, Component.literal(state.time).getVisualOrderText(), false, Font.DisplayMode.POLYGON_OFFSET, LightCoordsUtil.FULL_BRIGHT, state.colour, 0, 0);
        poseStack.popPose();
    }

    public static class ClockRenderState extends BlockEntityRenderState
    {
        public int direction;
        public int colour = -1;
        public String time = "";
    }
}
