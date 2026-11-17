package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

/**
 * Shared drawing for the appliance renderers: their items, the turn into the direction the appliance faces, and drinks.
 */
public class ApplianceRenderUtil
{
    public static ItemStackRenderState resolve(ItemModelResolver resolver, ItemStack stack, @Nullable Level level)
    {
        ItemStackRenderState state = new ItemStackRenderState();
        if(!stack.isEmpty())
        {
            resolver.updateForTopItem(state, stack, ItemDisplayContext.FIXED, level, null, 0);
        }
        return state;
    }

    /**
     * Turns the pose so that what follows is drawn as if the appliance faced east, the way 1.12.2 built the models (the same turn as
     * the blockstates').
     */
    public static void faceModel(PoseStack poseStack, Direction direction)
    {
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.rotateDegrees(Axis.YP, -((direction.toYRot() + 90F) % 360F));
        poseStack.translate(-0.5, 0.0, -0.5);
    }

    /**
     * The still water texture, which drinks are drawn with in their colour.
     */
    public static TextureAtlasSprite liquidSprite()
    {
        return Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(Fluids.WATER.defaultFluidState()).stillMaterial().sprite();
    }

    /**
     * Draws a box of liquid, all six sides.
     */
    public static void liquid(PoseStack poseStack, SubmitNodeCollector collector, TextureAtlasSprite sprite, int colour, int light, float x1, float y1, float z1, float x2, float y2, float z2)
    {
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();
        collector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(), (pose, consumer) -> {
            // Top and bottom
            quad(consumer, pose, colour, light, u0, u1, v0, v1, 0, 1, 0, x1, y2, z1, x1, y2, z2, x2, y2, z2, x2, y2, z1);
            quad(consumer, pose, colour, light, u0, u1, v0, v1, 0, -1, 0, x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2);
            // North and south
            quad(consumer, pose, colour, light, u0, u1, v0, v1, 0, 0, -1, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1);
            quad(consumer, pose, colour, light, u0, u1, v0, v1, 0, 0, 1, x2, y1, z2, x2, y2, z2, x1, y2, z2, x1, y1, z2);
            // West and east
            quad(consumer, pose, colour, light, u0, u1, v0, v1, -1, 0, 0, x1, y1, z2, x1, y2, z2, x1, y2, z1, x1, y1, z1);
            quad(consumer, pose, colour, light, u0, u1, v0, v1, 1, 0, 0, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2);
        });
    }

    private static void quad(VertexConsumer consumer, PoseStack.Pose pose, int colour, int light, float u0, float u1, float v0, float v1, float nx, float ny, float nz,
                             float ax, float ay, float az, float bx, float by, float bz, float cx, float cy, float cz, float dx, float dy, float dz)
    {
        consumer.addVertex(pose, ax, ay, az).setColor(colour).setUv(u0, v1).setLight(light).setNormal(pose, nx, ny, nz);
        consumer.addVertex(pose, bx, by, bz).setColor(colour).setUv(u0, v0).setLight(light).setNormal(pose, nx, ny, nz);
        consumer.addVertex(pose, cx, cy, cz).setColor(colour).setUv(u1, v0).setLight(light).setNormal(pose, nx, ny, nz);
        consumer.addVertex(pose, dx, dy, dz).setColor(colour).setUv(u1, v1).setLight(light).setNormal(pose, nx, ny, nz);
    }
}
