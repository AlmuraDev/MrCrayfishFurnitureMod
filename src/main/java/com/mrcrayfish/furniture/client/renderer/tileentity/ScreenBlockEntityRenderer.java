package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.block.FurnitureHorizontalBlock;
import com.mrcrayfish.furniture.block.TVBlock;
import com.mrcrayfish.furniture.client.screen.OnlineImageCache;
import com.mrcrayfish.furniture.tileentity.ScreenBlockEntity;
import com.mrcrayfish.furniture.tileentity.TVBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Draws the picture on a TV or in a photo frame, the way 1.12.2 did: static while it downloads, then the picture (fitted to the screen
 * unless stretched) on black, or why it couldn't be shown. TVs glow; photo frames take the light around them and are a little dimmer.
 */
public class ScreenBlockEntityRenderer implements BlockEntityRenderer<ScreenBlockEntity, ScreenBlockEntityRenderer.ScreenRenderState>
{
    private static final Identifier NOISE = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/misc/noise.png");
    private static final RandomSource RANDOM = RandomSource.create();
    private static final float PIXEL = 1.0F / 16.0F;
    private static final int PHOTO_TINT = 0xFFA6A6A6;

    public ScreenBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public ScreenRenderState createRenderState()
    {
        return new ScreenRenderState();
    }

    @Override
    public void extractRenderState(ScreenBlockEntity screen, ScreenRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(screen, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = screen.getBlockState();
        state.direction = blockState.hasProperty(FurnitureHorizontalBlock.DIRECTION) ? blockState.getValue(FurnitureHorizontalBlock.DIRECTION).get2DDataValue() : 0;
        boolean tv = screen instanceof TVBlockEntity;
        if(blockState.getBlock() instanceof TVBlock block)
        {
            state.width = block.getScreenWidth();
            state.height = block.getScreenHeight();
            state.y = (float) block.getScreenY(blockState);
            state.z = (float) block.getScreenZ(blockState);
        }
        else
        {
            state.width = 14;
            state.height = 14;
            state.y = 1;
            state.z = 7.49F;
        }
        state.glow = tv;
        state.stretch = screen.isStretched();
        state.picture = null;
        String url = screen.getCurrentUrl();
        if(url != null)
        {
            state.picture = OnlineImageCache.get(url, tv);
            state.frame = state.picture.status() == OnlineImageCache.Status.READY ? state.picture.frame() : null;
        }
    }

    @Override
    public void submit(ScreenRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        OnlineImageCache.Picture picture = state.picture;
        if(picture == null)
            return;

        int light = state.glow ? LightCoordsUtil.FULL_BRIGHT : state.lightCoords;
        int tint = state.glow ? -1 : PHOTO_TINT;
        float width = state.width * PIXEL;
        float height = state.height * PIXEL;

        poseStack.pushPose();
        poseStack.translate(0.5F, state.y * PIXEL, 0.5F);
        poseStack.rotateDegrees(Axis.YP, state.direction * -90F);
        poseStack.translate(-width / 2F, 0F, state.z * PIXEL);

        switch(picture.status())
        {
            case LOADING ->
            {
                // A random patch of static each frame
                float u = (int) ((256 - state.width * 4) * RANDOM.nextFloat()) / 256F;
                float v = (int) ((256 - state.height * 4) * RANDOM.nextFloat()) / 256F;
                quad(poseStack, collector, NOISE, tint, light, 0F, 0F, width, height, -0.01F * PIXEL, u, v, u + state.width * 4 / 256F, v + state.height * 4 / 256F);
            }
            case READY ->
            {
                if(state.frame != null)
                {
                    float imageWidth = width;
                    float imageHeight = height;
                    if(!state.stretch)
                    {
                        float scale = Math.min(width / picture.width(), height / picture.height());
                        imageWidth = picture.width() * scale;
                        imageHeight = picture.height() * scale;
                    }
                    float x = (width - imageWidth) / 2F;
                    float y = (height - imageHeight) / 2F;
                    quad(poseStack, collector, NOISE, 0xFF000000, light, 0F, 0F, width, height, 0F, 0F, 0F, 1F, 1F);
                    quad(poseStack, collector, state.frame, tint, light, x, y, imageWidth, imageHeight, -0.01F * PIXEL, 0F, 0F, 1F, 1F);
                }
            }
            case FAILED ->
            {
                Font font = Minecraft.getInstance().font;
                List<FormattedCharSequence> lines = font.split(Component.translatable(picture.error()), (int) ((state.width - 2) * 6.3F));
                poseStack.translate(width - PIXEL, height - PIXEL, -0.02F * PIXEL);
                poseStack.scale(-0.01F, -0.01F, 0.01F);
                for(int i = 0; i < lines.size(); i++)
                {
                    collector.submitText(poseStack, 0F, font.lineHeight * i, lines.get(i), false, Font.DisplayMode.POLYGON_OFFSET, light, -1, 0, 0);
                }
            }
        }
        poseStack.popPose();
    }

    /**
     * A textured rectangle facing the front of the screen (-z here), seen the right way round from there.
     */
    private static void quad(PoseStack poseStack, SubmitNodeCollector collector, Identifier texture, int colour, int light, float x, float y, float width, float height, float z, float u0, float v0, float u1, float v1)
    {
        collector.submitCustomGeometry(poseStack, RenderTypes.text(texture), (pose, consumer) -> {
            vertex(consumer, pose, x + width, y, z, colour, u0, v1, light);
            vertex(consumer, pose, x, y, z, colour, u1, v1, light);
            vertex(consumer, pose, x, y + height, z, colour, u1, v0, light);
            vertex(consumer, pose, x + width, y + height, z, colour, u0, v0, light);
        });
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, int colour, float u, float v, int light)
    {
        consumer.addVertex(pose, x, y, z).setColor(colour).setUv(u, v).setLight(light);
    }

    public static class ScreenRenderState extends BlockEntityRenderState
    {
        public int direction;
        public int width;
        public int height;
        public float y;
        public float z;
        public boolean glow;
        public boolean stretch;
        public OnlineImageCache.@Nullable Picture picture;
        @Nullable
        public Identifier frame;
    }
}
