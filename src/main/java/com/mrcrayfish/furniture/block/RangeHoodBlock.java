package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A kitchen range hood. Using it turns its light on and off. Stacked hoods facing the same way join into a taller vent (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class RangeHoodBlock extends FurnitureHorizontalBlock
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    private static final Map<Direction, VoxelShape> VENT_SHAPES = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> HOOD_SHAPES = new EnumMap<>(Direction.class);

    static
    {
        VoxelShape base = Block.box(0, 0, 0, 16, 4, 16);
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            VoxelShape vent = VoxelShapeHelper.rotate(Shapes.box(0.5, 0.0, 0.2, 1.0, 1.0, 0.8), direction);
            VENT_SHAPES.put(direction, vent);
            HOOD_SHAPES.put(direction, VoxelShapeHelper.combineAll(List.of(vent, base)));
        }
    }

    public RangeHoodBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(LIT, false).setValue(DOWN, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return (state.getValue(DOWN) ? VENT_SHAPES : HOOD_SHAPES).get(state.getValue(DIRECTION));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = super.getStateForPlacement(context);
        return this.connect(state, context.getLevel(), context.getClickedPos());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return direction == Direction.DOWN ? this.connect(state, level, pos) : state;
    }

    // Joins the hood below it when that one faces the same way
    private BlockState connect(BlockState state, LevelReader level, BlockPos pos)
    {
        BlockState below = level.getBlockState(pos.below());
        return state.setValue(DOWN, below.getBlock() instanceof RangeHoodBlock && below.getValue(DIRECTION) == state.getValue(DIRECTION));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!level.isClientSide())
        {
            level.setBlock(pos, state.setValue(LIT, !state.getValue(LIT)), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.6F);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(LIT, DOWN);
    }
}
