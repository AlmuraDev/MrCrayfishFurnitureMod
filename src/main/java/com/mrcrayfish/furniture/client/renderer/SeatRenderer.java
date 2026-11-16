package com.mrcrayfish.furniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.furniture.entity.SeatEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

/**
 * The seat is invisible: it renders nothing, not even a name tag.
 *
 * Author: MrCrayfish
 */
public class SeatRenderer extends EntityRenderer<SeatEntity, EntityRenderState>
{
    public SeatRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState()
    {
        return new EntityRenderState();
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {}
}
