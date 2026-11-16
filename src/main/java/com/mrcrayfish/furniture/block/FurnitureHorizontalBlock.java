package com.mrcrayfish.furniture.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * Author: MrCrayfish
 */
public abstract class FurnitureHorizontalBlock extends FurnitureBlock
{
    public static final EnumProperty<Direction> DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

    public FurnitureHorizontalBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return super.getStateForPlacement(context).setValue(DIRECTION, context.getHorizontalDirection());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(DIRECTION, rotation.rotate(state.getValue(DIRECTION)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(DIRECTION)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(DIRECTION);
    }
}
