package com.mrcrayfish.furniture.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mrcrayfish.furniture.block.BlindsBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Blinds never hide the faces of the blocks next to them. Treating their face as empty makes shouldRenderFace render the
 * neighbour's face, after it has still checked skipRendering and hidesNeighborFace, as 1.20 did.
 *
 * Author: MrCrayfish
 */
@Mixin(Block.class)
public class BlockMixin
{
    @WrapOperation(method = "shouldRenderFace(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFaceOcclusionShape(Lnet/minecraft/core/Direction;)Lnet/minecraft/world/phys/shapes/VoxelShape;", ordinal = 0))
    private static VoxelShape cfm$blindsDoNotOcclude(BlockState neighborState, Direction direction, Operation<VoxelShape> original)
    {
        if(neighborState.getBlock() instanceof BlindsBlock)
        {
            return Shapes.empty();
        }
        return original.call(neighborState, direction);
    }
}
