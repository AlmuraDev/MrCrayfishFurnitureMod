package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

/**
 * Curtains (1.12.2, one block per colour). Using them draws them open or closed; closed curtains keep light out. Open curtains next to
 * each other (facing the same way) gather at the ends of the row, as 1.12.2 drew them.
 *
 * Author: MrCrayfish
 */
public class CurtainsBlock extends FurnitureHorizontalBlock
{
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    private final Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);

    public CurtainsBlock(Properties properties)
    {
        super(properties);
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            this.shapes.put(direction, VoxelShapeHelper.rotate(Block.box(14, 0, 0, 16, 16, 16), direction));
        }
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(OPEN, false).setValue(TYPE, Type.DEFAULT));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return this.shapes.get(state.getValue(DIRECTION));
    }

    @Override
    protected int getLightDampening(BlockState state)
    {
        return state.getValue(OPEN) ? 0 : 15;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state)
    {
        return state.getValue(OPEN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = super.getStateForPlacement(context);
        return state.setValue(TYPE, getType(context.getLevel(), context.getClickedPos(), state));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return state.setValue(TYPE, getType(level, pos, state));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!level.isClientSide())
        {
            BlockState toggled = state.cycle(OPEN);
            level.setBlock(pos, toggled.setValue(TYPE, getType(level, pos, toggled)), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.6F, toggled.getValue(OPEN) ? 1.2F : 0.9F);
        }
        return InteractionResult.SUCCESS;
    }

    // How open curtains gather depends on the curtains either side of them (1.12.2's rules)
    private static Type getType(LevelReader level, BlockPos pos, BlockState state)
    {
        if(!state.getValue(OPEN))
            return Type.DEFAULT;

        Direction facing = state.getValue(DIRECTION);
        BlockState left = level.getBlockState(pos.relative(facing.getCounterClockWise()));
        BlockState right = level.getBlockState(pos.relative(facing.getClockWise()));
        boolean leftCurtain = left.getBlock() instanceof CurtainsBlock && left.getValue(DIRECTION) == facing;
        boolean rightCurtain = right.getBlock() instanceof CurtainsBlock && right.getValue(DIRECTION) == facing;
        boolean leftOpen = leftCurtain && left.getValue(OPEN);
        boolean rightOpen = rightCurtain && right.getValue(OPEN);
        boolean leftClosed = leftCurtain && !left.getValue(OPEN);
        boolean rightClosed = rightCurtain && !right.getValue(OPEN);

        if(rightOpen)
            return leftOpen ? Type.NONE : leftClosed ? Type.LEFT_CLOSED : Type.RIGHT_OPEN;
        if(leftOpen)
            return rightClosed ? Type.RIGHT_CLOSED : Type.LEFT_OPEN;
        if(rightClosed)
            return leftClosed ? Type.BOTH : Type.RIGHT;
        if(leftClosed)
            return Type.LEFT;
        return Type.DEFAULT;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN, TYPE);
    }

    public enum Type implements StringRepresentable
    {
        DEFAULT("default"),
        LEFT("left"),
        RIGHT("right"),
        LEFT_OPEN("left_open"),
        RIGHT_OPEN("right_open"),
        LEFT_CLOSED("left_closed"),
        RIGHT_CLOSED("right_closed"),
        BOTH("both"),
        NONE("none");

        private final String id;

        Type(String id)
        {
            this.id = id;
        }

        @Override
        public String getSerializedName()
        {
            return this.id;
        }
    }
}
