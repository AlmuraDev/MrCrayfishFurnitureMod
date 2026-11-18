package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

/**
 * Furniture that hangs on the side of a block, facing into it (the photo frame and door bell). It drops when that block goes.
 *
 * Author: MrCrayfish
 */
public abstract class WallFurnitureBlock extends FurnitureHorizontalBlock
{
    private final Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);

    /**
     * @param shape the shape against a wall to the east
     */
    protected WallFurnitureBlock(Properties properties, VoxelShape shape)
    {
        super(properties);
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            this.shapes.put(direction, VoxelShapeHelper.rotate(shape, direction));
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return this.shapes.get(state.getValue(DIRECTION));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Direction face = context.getClickedFace();
        if(face.getAxis().isVertical())
            return null;

        BlockState state = this.defaultBlockState().setValue(DIRECTION, face.getOpposite());
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        Direction direction = state.getValue(DIRECTION);
        BlockPos behind = pos.relative(direction);
        return level.getBlockState(behind).isFaceSturdy(level, behind, direction.getOpposite());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return direction == state.getValue(DIRECTION) && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
    }
}
