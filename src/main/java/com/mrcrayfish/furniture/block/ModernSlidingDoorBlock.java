package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

/**
 * A two-block-tall modern glass sliding door (from 1.12.2). Using it, powering it with redstone or a light switch slides it open or shut,
 * along with the sliding door next to it on its sliding side. It slides towards the side of the block that was clicked when placing it.
 *
 * Author: MrCrayfish
 */
public class ModernSlidingDoorBlock extends FurnitureHorizontalBlock implements Switchable
{
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    private static final Map<Direction, VoxelShape> CLOSED_SHAPES = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> OPEN_SHAPES = new EnumMap<>(Direction.class);

    static
    {
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            CLOSED_SHAPES.put(direction, VoxelShapeHelper.rotate(Block.box(6, 0, 0, 10, 16, 16), direction));
            OPEN_SHAPES.put(direction, VoxelShapeHelper.rotate(Shapes.box(6 * 0.0625, 0, 0, 10 * 0.0625, 1, 2.96 * 0.0625), direction));
        }
    }

    public ModernSlidingDoorBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(HALF, DoubleBlockHalf.LOWER).setValue(OPEN, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return (state.getValue(OPEN) ? OPEN_SHAPES : CLOSED_SHAPES).get(state.getValue(DIRECTION));
    }

    /**
     * Placed on top of a block. Like 1.12.2, which way the door faces (and so which way it slides) follows the half of the block that
     * was clicked.
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if(pos.getY() >= level.getMaxY() || !level.getBlockState(pos.above()).canBeReplaced(context))
            return null;

        Direction playerFacing = context.getHorizontalDirection();
        Vec3 hit = context.getClickLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
        int offsetX = playerFacing.getStepX();
        int offsetZ = playerFacing.getStepZ();
        boolean side = offsetX < 0 && hit.z < 0.5 || offsetX > 0 && hit.z > 0.5 || offsetZ < 0 && hit.x > 0.5 || offsetZ > 0 && hit.x < 0.5;
        Direction facing = side ? playerFacing.getOpposite() : playerFacing;
        boolean powered = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
        return this.defaultBlockState().setValue(DIRECTION, facing).setValue(HALF, DoubleBlockHalf.LOWER).setValue(OPEN, powered);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, placer, stack);
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        DoubleBlockHalf half = state.getValue(HALF);
        if(direction == (half == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN))
        {
            boolean partner = neighbourState.is(this) && neighbourState.getValue(HALF) != half;
            return partner ? state.setValue(OPEN, neighbourState.getValue(OPEN)) : Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    // Only the lower half drops the door; in creative the lower half goes without dropping anything
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!level.isClientSide())
        {
            this.setPowered(level, pos, state, !state.getValue(OPEN));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston)
    {
        if(level instanceof ServerLevel && !(block instanceof ModernSlidingDoorBlock))
        {
            BlockPos other = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
            boolean powered = level.hasNeighborSignal(pos) || level.hasNeighborSignal(other);
            if(powered != state.getValue(OPEN))
            {
                this.setPowered(level, pos, state, powered);
            }
        }
    }

    /**
     * Slides this door (both halves) open or shut, and the door beside it on its sliding side with it.
     */
    @Override
    public void setPowered(Level level, BlockPos pos, BlockState state, boolean powered)
    {
        if(state.getValue(OPEN) == powered)
            return;

        BlockPos lower = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
        BlockState lowerState = level.getBlockState(lower);
        BlockState upperState = level.getBlockState(lower.above());
        if(lowerState.is(this))
        {
            level.setBlock(lower, lowerState.setValue(OPEN, powered), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
        }
        if(upperState.is(this))
        {
            level.setBlock(lower.above(), upperState.setValue(OPEN, powered), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
        }
        level.playSound(null, pos, powered ? ModSounds.BLOCK_SLIDING_DOOR_OPEN.get() : ModSounds.BLOCK_SLIDING_DOOR_CLOSE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

        BlockPos neighbour = pos.relative(state.getValue(DIRECTION).getCounterClockWise().getOpposite());
        BlockState neighbourState = level.getBlockState(neighbour);
        if(neighbourState.getBlock() instanceof ModernSlidingDoorBlock door)
        {
            door.setPowered(level, neighbour, neighbourState, powered);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(HALF, OPEN);
    }
}
