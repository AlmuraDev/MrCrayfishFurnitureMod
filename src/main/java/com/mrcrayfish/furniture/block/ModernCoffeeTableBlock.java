package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The modern coffee table (from 1.12.2). Coffee tables next to each other join; legs only stand at free corners.
 *
 * Author: MrCrayfish
 */
public class ModernCoffeeTableBlock extends FurnitureBlock
{
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    private static final Map<Integer, VoxelShape> SHAPES = new HashMap<>();

    static
    {
        double p = 0.0625;
        VoxelShape top = Shapes.box(0, 6.5 * p, 0, 1, 8 * p, 1);
        VoxelShape southWest = Shapes.box(0, 0, 14.4 * p, 1.6 * p, 6.5 * p, 1);
        VoxelShape northWest = Shapes.box(0, 0, 0, 1.6 * p, 6.5 * p, 1.6 * p);
        VoxelShape southEast = Shapes.box(14.4 * p, 0, 14.4 * p, 1, 6.5 * p, 1);
        VoxelShape northEast = Shapes.box(14.4 * p, 0, 0, 1, 6.5 * p, 1.6 * p);
        for(int i = 0; i < 16; i++)
        {
            boolean north = (i & 1) != 0, south = (i & 2) != 0, east = (i & 4) != 0, west = (i & 8) != 0;
            List<VoxelShape> shapes = new ArrayList<>(List.of(top));
            if(!north)
            {
                if(!west) shapes.add(northWest);
                if(!east) shapes.add(northEast);
            }
            if(!south)
            {
                if(!west) shapes.add(southWest);
                if(!east) shapes.add(southEast);
            }
            SHAPES.put(i, VoxelShapeHelper.combineAll(shapes));
        }
    }

    public ModernCoffeeTableBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        int key = (state.getValue(NORTH) ? 1 : 0) | (state.getValue(SOUTH) ? 2 : 0) | (state.getValue(EAST) ? 4 : 0) | (state.getValue(WEST) ? 8 : 0);
        return SHAPES.get(key);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.connect(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return direction.getAxis().isHorizontal() ? this.connect(state, level, pos) : state;
    }

    private BlockState connect(BlockState state, LevelReader level, BlockPos pos)
    {
        return state.setValue(NORTH, level.getBlockState(pos.north()).is(this))
                .setValue(EAST, level.getBlockState(pos.east()).is(this))
                .setValue(SOUTH, level.getBlockState(pos.south()).is(this))
                .setValue(WEST, level.getBlockState(pos.west()).is(this));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, EAST, SOUTH, WEST);
    }
}
