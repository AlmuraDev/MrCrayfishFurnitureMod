package com.mrcrayfish.furniture.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * A modern window pane (from 1.12.2). It connects like glass panes, and its frame also joins the windows and sliding doors around it,
 * above and below too.
 *
 * Author: MrCrayfish
 */
public class ModernWindowBlock extends IronBarsBlock
{
    public static final BooleanProperty PANE_NORTH = BooleanProperty.create("pane_north");
    public static final BooleanProperty PANE_EAST = BooleanProperty.create("pane_east");
    public static final BooleanProperty PANE_SOUTH = BooleanProperty.create("pane_south");
    public static final BooleanProperty PANE_WEST = BooleanProperty.create("pane_west");
    public static final BooleanProperty PANE_UP = BooleanProperty.create("pane_up");
    public static final BooleanProperty PANE_DOWN = BooleanProperty.create("pane_down");

    public ModernWindowBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(PANE_NORTH, false).setValue(PANE_EAST, false).setValue(PANE_SOUTH, false)
                .setValue(PANE_WEST, false).setValue(PANE_UP, false).setValue(PANE_DOWN, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.joinFrames(super.getStateForPlacement(context), context.getLevel(), context.getClickedPos());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return this.joinFrames(super.updateShape(state, level, ticks, pos, direction, neighbourPos, neighbourState, random), level, pos);
    }

    private BlockState joinFrames(BlockState state, LevelReader level, BlockPos pos)
    {
        return state.setValue(PANE_NORTH, joins(level, pos.north()))
                .setValue(PANE_EAST, joins(level, pos.east()))
                .setValue(PANE_SOUTH, joins(level, pos.south()))
                .setValue(PANE_WEST, joins(level, pos.west()))
                .setValue(PANE_UP, joins(level, pos.above()))
                .setValue(PANE_DOWN, joins(level, pos.below()));
    }

    private static boolean joins(LevelReader level, BlockPos pos)
    {
        Block block = level.getBlockState(pos).getBlock();
        return block instanceof IronBarsBlock || block instanceof ModernSlidingDoorBlock;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(PANE_NORTH, PANE_EAST, PANE_SOUTH, PANE_WEST, PANE_UP, PANE_DOWN);
    }
}
