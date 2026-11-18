package com.mrcrayfish.furniture.client;

import com.mrcrayfish.furniture.client.renderer.SeatRenderer;
import com.mrcrayfish.furniture.client.renderer.tileentity.ApplianceItemsRenderer;
import com.mrcrayfish.furniture.client.renderer.tileentity.BlenderBlockEntityRenderer;
import com.mrcrayfish.furniture.client.renderer.tileentity.CupBlockEntityRenderer;
import com.mrcrayfish.furniture.client.renderer.tileentity.DigitalClockBlockEntityRenderer;
import com.mrcrayfish.furniture.client.renderer.tileentity.DoorMatBlockEntityRenderer;
import com.mrcrayfish.furniture.client.renderer.tileentity.ScreenBlockEntityRenderer;
import com.mrcrayfish.furniture.client.renderer.tileentity.GrillBlockEntityRenderer;
import com.mrcrayfish.furniture.client.renderer.tileentity.KitchenSinkBlockEntityRenderer;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModEntities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Registers the furniture's entity and block entity renderers (client only). Hedges follow the leaves graphics option through
 * {@link com.mrcrayfish.furniture.mixin.client.ModelBlockRendererMixin}.
 */
public class RenderSetup
{
    public static void register(IEventBus modBus)
    {
        modBus.addListener(RenderSetup::onRegisterRenderers);
    }

    private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(ModEntities.SEAT.get(), SeatRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.GRILL.get(), GrillBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DOOR_MAT.get(), DoorMatBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.KITCHEN_SINK.get(), KitchenSinkBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.OVEN.get(), ApplianceItemsRenderer.Oven::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MICROWAVE.get(), ApplianceItemsRenderer.Microwave::new);
        event.registerBlockEntityRenderer(ModBlockEntities.TOASTER.get(), ApplianceItemsRenderer.Toaster::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WASHING_MACHINE.get(), ApplianceItemsRenderer.WashingMachine::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BLENDER.get(), BlenderBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CUP.get(), CupBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.TV.get(), ScreenBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.PHOTO_FRAME.get(), ScreenBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DIGITAL_CLOCK.get(), DigitalClockBlockEntityRenderer::new);
    }
}
