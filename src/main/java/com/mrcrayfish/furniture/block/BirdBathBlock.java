package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * A stone bird bath for the garden (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class BirdBathBlock extends FurnitureBlock
{
    private static final VoxelShape SHAPE = VoxelShapeHelper.combineAll(List.of(
            Block.box(5, 1.75, 5, 11, 12, 11),
            Block.box(4, 0, 4, 12, 1.5, 12),
            Block.box(1, 11.25, 1, 15, 12.75, 15),
            Block.box(0, 12.75, 0, 16, 14.5, 16)));

    public BirdBathBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }
}
