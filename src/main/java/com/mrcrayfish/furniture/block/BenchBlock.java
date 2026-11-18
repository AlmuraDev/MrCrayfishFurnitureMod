package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A garden bench (1.12.2) to sit on. Benches side by side along the same line join into one long bench.
 *
 * Author: MrCrayfish
 */
public class BenchBlock extends FurnitureHorizontalBlock
{
    public static final BooleanProperty LEFT = BooleanProperty.create("left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");
    // The bench runs across the way it faces
    private static final VoxelShape ALONG_X = Block.box(0, 0, 1, 16, 10, 15);
    private static final VoxelShape ALONG_Z = Block.box(1, 0, 0, 15, 10, 16);

    public BenchBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(LEFT, false).setValue(RIGHT, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return state.getValue(DIRECTION).getAxis() == Direction.Axis.Z ? ALONG_X : ALONG_Z;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.connect(context.getLevel(), context.getClickedPos(), super.getStateForPlacement(context));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return this.connect(level, pos, state);
    }

    // As 1.12.2 had it: a bench facing north or south joins benches to its east (right) and west (left), one facing east or west joins
    // benches to its north (right) and south (left)
    private BlockState connect(LevelReader level, BlockPos pos, BlockState state)
    {
        Direction.Axis axis = state.getValue(DIRECTION).getAxis();
        BlockPos right = axis == Direction.Axis.Z ? pos.east() : pos.north();
        BlockPos left = axis == Direction.Axis.Z ? pos.west() : pos.south();
        return state.setValue(RIGHT, this.isBench(level, right, axis)).setValue(LEFT, this.isBench(level, left, axis));
    }

    private boolean isBench(LevelReader level, BlockPos pos, Direction.Axis axis)
    {
        BlockState state = level.getBlockState(pos);
        return state.is(this) && state.getValue(DIRECTION).getAxis() == axis;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        return SeatEntity.create(level, pos, 0.4, player, state.getValue(DIRECTION));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(LEFT, RIGHT);
    }
}
