package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

/**
 * A fire alarm on the ceiling (1.12.2). Every half second it looks for fire within four blocks; when it finds some it sounds, lights up
 * and powers the block above it, until someone uses it to switch it off.
 *
 * Author: MrCrayfish
 */
public class FireAlarmBlock extends FurnitureBlock
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final VoxelShape SHAPE = Block.box(5.6, 14, 5.6, 10.4, 16, 10.4);
    private static final int SCAN_TICKS = 10;
    private static final int ALARM_TICKS = 34;
    private static final int RANGE = 4;

    public FireAlarmBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(LIT, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = this.defaultBlockState();
        return context.getClickedFace() == Direction.DOWN && state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        return !level.getBlockState(pos.above()).isAir();
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return direction == Direction.UP && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
    {
        if(!oldState.is(this))
        {
            level.scheduleTick(pos, this, state.getValue(LIT) ? ALARM_TICKS : 1);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if(state.getValue(LIT))
        {
            level.playSound(null, pos, ModSounds.BLOCK_FIRE_ALARM.get(), SoundSource.BLOCKS, 5.0F, 1.0F);
            level.scheduleTick(pos, this, ALARM_TICKS);
            return;
        }

        for(BlockPos check : BlockPos.betweenClosed(pos.offset(-RANGE, -RANGE, -RANGE), pos.offset(RANGE, RANGE, RANGE)))
        {
            if(level.getBlockState(check).is(BlockTags.FIRE))
            {
                this.setLit(state, level, pos, true);
                level.playSound(null, pos, ModSounds.BLOCK_FIRE_ALARM.get(), SoundSource.BLOCKS, 5.0F, 1.0F);
                level.scheduleTick(pos, this, ALARM_TICKS);
                return;
            }
        }
        level.scheduleTick(pos, this, SCAN_TICKS);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!state.getValue(LIT))
            return InteractionResult.PASS;

        if(!level.isClientSide())
        {
            this.setLit(state, level, pos, false);
            level.scheduleTick(pos, this, SCAN_TICKS);
        }
        return InteractionResult.SUCCESS;
    }

    private void setLit(BlockState state, Level level, BlockPos pos, boolean lit)
    {
        level.setBlock(pos, state.setValue(LIT, lit), Block.UPDATE_ALL);
        level.updateNeighborsAt(pos.above(), this);
    }

    @Override
    protected boolean isSignalSource(BlockState state)
    {
        return true;
    }

    // Powers the block it hangs from, as 1.12.2 did
    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return state.getValue(LIT) && direction == Direction.DOWN ? 15 : 0;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return this.getDirectSignal(state, level, pos, direction);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }
}
