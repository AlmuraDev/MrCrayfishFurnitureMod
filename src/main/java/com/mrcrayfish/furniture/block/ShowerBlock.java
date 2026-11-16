package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A two-block-tall shower cubicle with glass walls and an open side to step in. Put a shower head inside it to wash (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class ShowerBlock extends FurnitureHorizontalBlock
{
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final Map<Direction, VoxelShape> LOWER_SHAPES = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> UPPER_SHAPES = new EnumMap<>(Direction.class);

    static
    {
        Map<Direction, VoxelShape> sides = new EnumMap<>(Direction.class);
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            sides.put(direction, VoxelShapeHelper.rotate(Shapes.box(0.9375, 0.0, 0.0, 1.0, 1.0, 1.0), direction));
        }
        for(Direction facing : Direction.Plane.HORIZONTAL)
        {
            // Every wall but the one in the open side (as 1.12.2 set them out)
            List<VoxelShape> walls = new ArrayList<>();
            if(facing != Direction.NORTH) walls.add(sides.get(Direction.SOUTH));
            if(facing != Direction.SOUTH) walls.add(sides.get(Direction.NORTH));
            if(facing != Direction.EAST) walls.add(sides.get(Direction.WEST));
            if(facing != Direction.WEST) walls.add(sides.get(Direction.EAST));
            UPPER_SHAPES.put(facing, VoxelShapeHelper.combineAll(walls));
            walls.add(Block.box(0, 0, 0, 16, 1, 16));
            LOWER_SHAPES.put(facing, VoxelShapeHelper.combineAll(walls));
        }
    }

    public ShowerBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return (state.getValue(HALF) == DoubleBlockHalf.LOWER ? LOWER_SHAPES : UPPER_SHAPES).get(state.getValue(DIRECTION));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockPos above = context.getClickedPos().above();
        Level level = context.getLevel();
        if(above.getY() >= level.getMaxY() || !level.getBlockState(above).canBeReplaced(context))
            return null;
        return super.getStateForPlacement(context).setValue(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, placer, stack);
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
    }

    // The two halves need each other
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        DoubleBlockHalf half = state.getValue(HALF);
        if(direction == (half == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN))
        {
            boolean partner = neighbourState.is(this) && neighbourState.getValue(HALF) != half;
            return partner ? state : Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    // Only the lower half drops the shower; in creative the lower half goes without dropping anything
    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        if(!level.isClientSide() && player.preventsBlockDrops() && state.getValue(HALF) == DoubleBlockHalf.UPPER)
        {
            BlockPos below = pos.below();
            BlockState lower = level.getBlockState(below);
            if(lower.is(this) && lower.getValue(HALF) == DoubleBlockHalf.LOWER)
            {
                level.setBlock(below, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                level.levelEvent(player, 2001, below, Block.getId(lower));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }
}
