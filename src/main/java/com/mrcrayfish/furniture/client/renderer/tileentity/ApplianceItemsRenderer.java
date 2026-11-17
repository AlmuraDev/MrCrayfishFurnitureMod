package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.furniture.block.FurnitureHorizontalBlock;
import com.mrcrayfish.furniture.tileentity.MicrowaveBlockEntity;
import com.mrcrayfish.furniture.tileentity.OvenBlockEntity;
import com.mrcrayfish.furniture.tileentity.ToasterBlockEntity;
import com.mrcrayfish.furniture.tileentity.WasherBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Draws the items inside an appliance, as 1.12.2 did: the food in the oven, the item in the microwave, the slices in the toaster and
 * the armour turning in the washing machine.
 */
public abstract class ApplianceItemsRenderer<T extends BlockEntity> implements BlockEntityRenderer<T, ApplianceItemsRenderer.ItemsRenderState>
{
    private final ItemModelResolver itemModelResolver;

    protected ApplianceItemsRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    protected abstract List<ItemStack> items(T blockEntity);

    protected boolean running(T blockEntity)
    {
        return false;
    }

    protected float progress(T blockEntity, float partialTicks)
    {
        return 0F;
    }

    /**
     * Places and draws one item; the pose already faces the model's east.
     */
    protected abstract void place(PoseStack poseStack, int index, ItemsRenderState state);

    @Override
    public ItemsRenderState createRenderState()
    {
        return new ItemsRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, ItemsRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.direction = blockEntity.getBlockState().getValue(FurnitureHorizontalBlock.DIRECTION);
        state.running = this.running(blockEntity);
        state.progress = this.progress(blockEntity, partialTicks);
        List<ItemStack> items = this.items(blockEntity);
        state.items = new ItemStackRenderState[items.size()];
        for(int i = 0; i < items.size(); i++)
        {
            state.items[i] = ApplianceRenderUtil.resolve(this.itemModelResolver, items.get(i), blockEntity.getLevel());
        }
    }

    @Override
    public void submit(ItemsRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        for(int i = 0; i < state.items.length; i++)
        {
            ItemStackRenderState item = state.items[i];
            if(item.isEmpty())
                continue;

            poseStack.pushPose();
            ApplianceRenderUtil.faceModel(poseStack, state.direction);
            this.place(poseStack, i, state);
            item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    public static class ItemsRenderState extends BlockEntityRenderState
    {
        public Direction direction = Direction.NORTH;
        public boolean running;
        public float progress;
        public ItemStackRenderState[] items = new ItemStackRenderState[0];
    }

    /**
     * The food on the oven's racks: what is baking on the top rack, what is baked on the bottom one.
     */
    public static class Oven extends ApplianceItemsRenderer<OvenBlockEntity>
    {
        public Oven(BlockEntityRendererProvider.Context context)
        {
            super(context);
        }

        @Override
        protected List<ItemStack> items(OvenBlockEntity oven)
        {
            return oven.getContents();
        }

        @Override
        protected void place(PoseStack poseStack, int index, ItemsRenderState state)
        {
            int slot = index % OvenBlockEntity.ROW;
            double y = index < OvenBlockEntity.ROW ? 0.56 : 0.3;
            poseStack.translate(0.35 + 0.3 * (slot % 2), y, 0.35 + 0.3 * (slot / 2));
            poseStack.rotateDegrees(Axis.XP, 90F);
            poseStack.scale(0.3F, 0.3F, 0.3F);
        }
    }

    /**
     * The item standing in the middle of the microwave.
     */
    public static class Microwave extends ApplianceItemsRenderer<MicrowaveBlockEntity>
    {
        public Microwave(BlockEntityRendererProvider.Context context)
        {
            super(context);
        }

        @Override
        protected List<ItemStack> items(MicrowaveBlockEntity microwave)
        {
            return microwave.getContents();
        }

        @Override
        protected void place(PoseStack poseStack, int index, ItemsRenderState state)
        {
            poseStack.translate(0.45, 0.2, 0.5);
            poseStack.rotateDegrees(Axis.YP, 90F);
            poseStack.scale(0.3F, 0.3F, 0.3F);
        }
    }

    /**
     * The two slices standing in the toaster's slots, sunk in while they toast.
     */
    public static class Toaster extends ApplianceItemsRenderer<ToasterBlockEntity>
    {
        public Toaster(BlockEntityRendererProvider.Context context)
        {
            super(context);
        }

        @Override
        protected List<ItemStack> items(ToasterBlockEntity toaster)
        {
            return toaster.getSlices();
        }

        @Override
        protected boolean running(ToasterBlockEntity toaster)
        {
            return toaster.isRunning();
        }

        @Override
        protected void place(PoseStack poseStack, int index, ItemsRenderState state)
        {
            poseStack.translate(0.44 + 0.13 * index, state.running ? 0.3 : 0.42, 0.48);
            poseStack.rotateDegrees(Axis.YP, 90F);
            poseStack.scale(0.45F, 0.45F, 0.45F);
        }
    }

    /**
     * The armour tumbling in the washing machine drum while it runs.
     */
    public static class WashingMachine extends ApplianceItemsRenderer<WasherBlockEntity>
    {
        public WashingMachine(BlockEntityRendererProvider.Context context)
        {
            super(context);
        }

        @Override
        protected List<ItemStack> items(WasherBlockEntity washer)
        {
            // Not the soapy water
            return washer.getContents().subList(0, washer.getContainerSize() - 1);
        }

        @Override
        protected boolean running(WasherBlockEntity washer)
        {
            return washer.isRunning();
        }

        @Override
        protected float progress(WasherBlockEntity washer, float partialTicks)
        {
            return washer.isRunning() ? washer.getProgress() + partialTicks : 0F;
        }

        @Override
        protected void place(PoseStack poseStack, int index, ItemsRenderState state)
        {
            poseStack.translate(0.3 + 0.1 * index, 0.5, 0.5);
            poseStack.rotateDegrees(Axis.XP, state.progress * 5F + index * 90F);
            poseStack.translate(0.0, -0.15, 0.0);
            poseStack.rotateDegrees(Axis.YP, 90F);
            poseStack.scale(0.4F, 0.4F, 0.4F);
        }
    }
}
