package com.mrcrayfish.furniture.client;

import com.mrcrayfish.furniture.client.renderer.SeatRenderer;
import com.mrcrayfish.furniture.client.renderer.tileentity.DoorMatBlockEntityRenderer;
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
    }
}
