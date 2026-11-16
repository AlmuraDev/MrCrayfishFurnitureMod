package com.mrcrayfish.furniture.block;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.furniture.common.ModTags;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class HedgeBlock extends FurnitureWaterloggedBlock
{
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    public final ImmutableMap<BlockState, VoxelShape> COLLISION_SHAPES;

    public HedgeBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(WATERLOGGED, false));
        SHAPES = this.generateShapes(this.getStateDefinition().getPossibleStates(), false);
        COLLISION_SHAPES = this.generateShapes(this.getStateDefinition().getPossibleStates(), true);
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states, boolean collision)
    {
        final VoxelShape POST = Block.box(4, 0, 4, 12, 16, 12);
        final VoxelShape[] SIDE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(4, 0, 0, 12, 16, 4), Direction.SOUTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            boolean north = state.getValue(NORTH);
            boolean east = state.getValue(EAST);
            boolean south = state.getValue(SOUTH);
            boolean west = state.getValue(WEST);

            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(this.applyCollision(POST, collision));
            if(north)
            {
                shapes.add(this.applyCollision(SIDE[Direction.NORTH.get2DDataValue()], collision));
            }
            if(east)
            {
                shapes.add(this.applyCollision(SIDE[Direction.EAST.get2DDataValue()], collision));
            }
            if(south)
            {
                shapes.add(this.applyCollision(SIDE[Direction.SOUTH.get2DDataValue()], collision));
            }
            if(west)
            {
                shapes.add(this.applyCollision(SIDE[Direction.WEST.get2DDataValue()], collision));
            }
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }

    private VoxelShape applyCollision(VoxelShape shape, boolean collision)
    {
        if(collision)
        {
            shape = VoxelShapeHelper.setMaxHeight(shape, 1.5);
            shape = VoxelShapeHelper.limitHorizontal(shape);
        }
        return shape;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state);
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state)
    {
        return SHAPES.get(state);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
    {
        return COLLISION_SHAPES.get(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos newPos, BlockState newState, RandomSource random)
    {
        state = super.updateShape(state, level, ticks, pos, direction, newPos, newState, random);
        return this.getHedgeState(state, level, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.getHedgeState(super.getStateForPlacement(context), context.getLevel(), context.getClickedPos());
    }

    private BlockState getHedgeState(BlockState state, LevelReader level, BlockPos pos)
    {
        boolean north = canConnectToBlock(level, pos, Direction.NORTH);
        boolean east = canConnectToBlock(level, pos, Direction.EAST);
        boolean south = canConnectToBlock(level, pos, Direction.SOUTH);
        boolean west = canConnectToBlock(level, pos, Direction.WEST);
        return state.setValue(NORTH, north).setValue(EAST, east).setValue(SOUTH, south).setValue(WEST, west);
    }

    private boolean canConnectToBlock(LevelReader level, BlockPos pos, Direction direction)
    {
        BlockPos offsetPos = pos.relative(direction);
        BlockState offsetState = level.getBlockState(offsetPos);
        return !isExceptionForConnection(offsetState) && offsetState.isFaceSturdy(level, offsetPos, direction.getOpposite()) || offsetState.is(ModTags.Blocks.HEDGES);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH);
        builder.add(EAST);
        builder.add(SOUTH);
        builder.add(WEST);
    }

    @Override
    protected int getLightDampening(BlockState state)
    {
        return 1;
    }
}

