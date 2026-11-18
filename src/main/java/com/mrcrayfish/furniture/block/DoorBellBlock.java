package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A door bell (1.12.2): pressing it rings and gives a redstone signal for a second and a half, like a button. An arrow stuck in it holds
 * it down.
 *
 * Author: MrCrayfish
 */
public class DoorBellBlock extends WallFurnitureBlock
{
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final int PRESS_TICKS = 30;

    public DoorBellBlock(Properties properties)
    {
        super(properties, Block.box(13.6, 4.8, 6.4, 16, 11.2, 9.6));
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(POWERED, false));
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Shapes.empty();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(state.getValue(POWERED))
            return InteractionResult.CONSUME;

        if(!level.isClientSide())
        {
            this.setPowered(state, level, pos, true);
            level.playSound(null, pos, ModSounds.BLOCK_DOOR_BELL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            level.scheduleTick(pos, this, PRESS_TICKS);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if(state.getValue(POWERED))
        {
            this.checkArrows(state, level, pos);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise)
    {
        if(!level.isClientSide() && entity instanceof AbstractArrow && !state.getValue(POWERED))
        {
            this.checkArrows(state, level, pos);
        }
    }

    private void checkArrows(BlockState state, Level level, BlockPos pos)
    {
        boolean arrow = !level.getEntitiesOfClass(AbstractArrow.class, state.getShape(level, pos).bounds().move(pos)).isEmpty();
        if(arrow != state.getValue(POWERED))
        {
            this.setPowered(state, level, pos, arrow);
            level.playSound(null, pos, SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.3F, arrow ? 0.6F : 0.5F);
        }
        if(arrow)
        {
            level.scheduleTick(pos, this, PRESS_TICKS);
        }
    }

    private void setPowered(BlockState state, Level level, BlockPos pos, boolean powered)
    {
        level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_ALL);
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(state.getValue(DIRECTION)), this);
    }

    @Override
    protected boolean isSignalSource(BlockState state)
    {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return state.getValue(POWERED) ? 15 : 0;
    }

    // Strongly powers the block it hangs on
    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return state.getValue(POWERED) && state.getValue(DIRECTION).getOpposite() == direction ? 15 : 0;
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston)
    {
        if(!movedByPiston && state.getValue(POWERED))
        {
            level.updateNeighborsAt(pos, this);
            level.updateNeighborsAt(pos.relative(state.getValue(DIRECTION)), this);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }
}
