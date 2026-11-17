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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The modern tables (indoor and outdoor, from 1.12.2). Tables next to each other join into one long table; a table only has legs at its
 * free corners.
 *
 * Author: MrCrayfish
 */
public class ModernTableBlock extends FurnitureBlock
{
    // 1.12.2's names: forward is north, back south, left west, right east
    public static final BooleanProperty FORWARD = BooleanProperty.create("forward");
    public static final BooleanProperty BACK = BooleanProperty.create("back");
    public static final BooleanProperty LEFT = BooleanProperty.create("left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");

    private static final Map<Integer, VoxelShape> SHAPES = new HashMap<>();

    static
    {
        double p = 0.0625;
        VoxelShape top = Shapes.box(0, 14 * p, 0, 1, 1, 1);
        VoxelShape southEast = Shapes.box(0.25 * p, 0, 13.75 * p, 2.25 * p, 14 * p, 15.75 * p);
        VoxelShape southWest = Shapes.box(13.75 * p, 0, 13.75 * p, 15.75 * p, 14 * p, 15.75 * p);
        VoxelShape northEast = Shapes.box(0.25 * p, 0, 0.25 * p, 2.25 * p, 14 * p, 2.25 * p);
        VoxelShape northWest = Shapes.box(13.75 * p, 0, 0.25 * p, 15.75 * p, 14 * p, 2.25 * p);
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

    public ModernTableBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FORWARD, false).setValue(BACK, false).setValue(LEFT, false).setValue(RIGHT, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        int key = (state.getValue(FORWARD) ? 1 : 0) | (state.getValue(BACK) ? 2 : 0) | (state.getValue(RIGHT) ? 4 : 0) | (state.getValue(LEFT) ? 8 : 0);
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
        return state.setValue(FORWARD, level.getBlockState(pos.north()).is(this))
                .setValue(BACK, level.getBlockState(pos.south()).is(this))
                .setValue(LEFT, level.getBlockState(pos.west()).is(this))
                .setValue(RIGHT, level.getBlockState(pos.east()).is(this));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FORWARD, BACK, LEFT, RIGHT);
    }
}
