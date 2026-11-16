package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.furniture.block.DoorMatBlock;
import com.mrcrayfish.furniture.tileentity.DoorMatBlockEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Author: MrCrayfish
 */
public class DoorMatBlockEntityRenderer implements BlockEntityRenderer<DoorMatBlockEntity, DoorMatBlockEntityRenderer.DoorMatRenderState>
{
    /**
     * The near-black the text has always been drawn in (1.20 passed the overlay coords, 0x0A0000, as the colour and the font made it opaque)
     */
    private static final int TEXT_COLOUR = 0xFF0A0000;

    private final Font font;

    public DoorMatBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.font = context.font();
    }

    @Override
    public DoorMatRenderState createRenderState()
    {
        return new DoorMatRenderState();
    }

    @Override
    public void extractRenderState(DoorMatBlockEntity tileEntity, DoorMatRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(tileEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = tileEntity.getBlockState();
        if(tileEntity.getMessage() != null && blockState.getBlock() instanceof DoorMatBlock)
        {
            state.message = tileEntity.getMessage();
            state.rotation = blockState.getValue(DoorMatBlock.DIRECTION).get2DDataValue();
        }
        else
        {
            state.message = null;
        }
    }

    @Override
    public void submit(DoorMatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if(state.message == null)
            return;

        poseStack.pushPose(); //Push

        poseStack.translate(0.5, 0.0626, 0.5);

        poseStack.rotateDegrees(Axis.YP, -90F * state.rotation + 180F);
        poseStack.rotateDegrees(Axis.XP, -90F);

        poseStack.scale(1.0F, -1.0F, -1.0F);
        poseStack.scale(0.0125F, 0.0125F, 0.0125F);

        List<FormattedCharSequence> lines = this.font.split(FormattedText.of(state.message), 60);
        poseStack.translate(0.0, -(lines.size() * this.font.lineHeight - 1.0) / 2.0, 0);

        for(int j = 0; j < lines.size(); j++)
        {
            FormattedCharSequence line = lines.get(j);
            submitNodeCollector.submitText(poseStack, -this.font.width(line) / 2.0F, j * this.font.lineHeight, line, false, Font.DisplayMode.NORMAL, state.lightCoords, TEXT_COLOUR, 0, 0);
        }
        poseStack.popPose(); //Pop
    }

    public static class DoorMatRenderState extends BlockEntityRenderState
    {
        @Nullable
        public String message;
        public int rotation;
    }
}
