package com.mrcrayfish.furniture.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

/**
 * A ceiling light. In right-click mode using it turns it on and off; sneak-use switches it to redstone mode, where redstone power does.
 * A light switch can work it either way (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class CeilingLightBlock extends FurnitureBlock implements Switchable
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);

    private static final VoxelShape SHAPE = Block.box(5, 6.4, 5, 11, 16, 11);

    public CeilingLightBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(LIT, false).setValue(MODE, Mode.RIGHT_CLICK));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    // Only goes on the underside of a block
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        if(context.getClickedFace() != Direction.DOWN)
            return null;
        return this.defaultBlockState();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        return !level.isEmptyBlock(pos.above());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return direction == Direction.UP && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!level.isClientSide())
        {
            if(player.isShiftKeyDown())
            {
                Mode mode = state.getValue(MODE) == Mode.RIGHT_CLICK ? Mode.REDSTONE : Mode.RIGHT_CLICK;
                BlockState newState = state.setValue(MODE, mode);
                if(mode == Mode.REDSTONE)
                {
                    newState = newState.setValue(LIT, level.hasNeighborSignal(pos));
                }
                level.setBlock(pos, newState, Block.UPDATE_ALL);
                player.sendSystemMessage(Component.translatable(mode == Mode.REDSTONE ? "message.cfm.ceiling_light.redstone" : "message.cfm.ceiling_light.right_click"));
            }
            else if(state.getValue(MODE) == Mode.RIGHT_CLICK)
            {
                level.setBlock(pos, state.setValue(LIT, !state.getValue(LIT)), Block.UPDATE_ALL);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston)
    {
        if(level instanceof ServerLevel && state.getValue(MODE) == Mode.REDSTONE)
        {
            boolean powered = level.hasNeighborSignal(pos);
            if(powered != state.getValue(LIT))
            {
                level.setBlock(pos, state.setValue(LIT, powered), Block.UPDATE_CLIENTS);
            }
        }
    }

    @Override
    public void setPowered(Level level, BlockPos pos, BlockState state, boolean powered)
    {
        level.setBlock(pos, state.setValue(LIT, powered), Block.UPDATE_ALL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(LIT, MODE);
    }

    public enum Mode implements StringRepresentable
    {
        RIGHT_CLICK,
        REDSTONE;

        @Override
        public String getSerializedName()
        {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
