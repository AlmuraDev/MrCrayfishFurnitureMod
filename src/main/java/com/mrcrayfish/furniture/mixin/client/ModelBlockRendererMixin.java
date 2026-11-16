package com.mrcrayfish.furniture.mixin.client;

import com.mrcrayfish.furniture.block.HedgeBlock;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Hedges follow the leaves graphics option like leaves do: with "Cutout Leaves" off they are drawn opaque (solid layer).
 * With it on they keep the cutout layer their leaf textures give them. This replaces the 1.20 FancyModel wrapper.
 *
 * Author: MrCrayfish
 */
@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin
{
    @Inject(method = "forceOpaque", at = @At("HEAD"), cancellable = true)
    private static void cfm$hedgesFollowLeaves(boolean cutoutLeaves, BlockState state, CallbackInfoReturnable<Boolean> cir)
    {
        if(!cutoutLeaves && state.getBlock() instanceof HedgeBlock)
        {
            cir.setReturnValue(true);
        }
    }
}
