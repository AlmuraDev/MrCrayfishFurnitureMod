package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The modern bed in one of sixteen colours (from 1.12.2). A real bed: sleep in it to set your spawn and skip the night. Beds side by
 * side facing the same way join into a wider bed.
 *
 * Author: MrCrayfish
 */
public class ModernBedBlock extends BedBlock
{
    public static final EnumProperty<Side> TYPE = EnumProperty.create("type", Side.class);

    private static final VoxelShape OUTLINE = Block.box(0, 0, 0, 16, 9, 16);
    private static final Map<Direction, VoxelShape> HEAD_SHAPES = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> FOOT_SHAPES = new EnumMap<>(Direction.class);

    static
    {
        VoxelShape base = Block.box(0, 4, 0, 16, 8, 16);
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            // 1.12.2's frames: the headboard is at the far end of the head, the foot board at the near end of the foot
            HEAD_SHAPES.put(direction, VoxelShapeHelper.combineAll(List.of(base, VoxelShapeHelper.rotate(Block.box(14, 0, 0, 16, 16, 16), direction))));
            FOOT_SHAPES.put(direction, VoxelShapeHelper.combineAll(List.of(base, VoxelShapeHelper.rotate(Block.box(0, 0, 0, 2, 8, 16), direction))));
        }
    }

    public ModernBedBlock(DyeColor color, Properties properties)
    {
        super(color, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE, Side.NONE));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return OUTLINE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return (state.getValue(PART) == BedPart.HEAD ? HEAD_SHAPES : FOOT_SHAPES).get(state.getValue(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state.setValue(TYPE, sideAt(context.getLevel(), context.getClickedPos(), state.getValue(FACING)));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        BlockState updated = super.updateShape(state, level, ticks, pos, direction, neighbourPos, neighbourState, random);
        if(updated.is(this) && direction.getAxis() != updated.getValue(FACING).getAxis() && direction.getAxis().isHorizontal())
        {
            updated = updated.setValue(TYPE, sideAt(level, pos, updated.getValue(FACING)));
        }
        return updated;
    }

    // 1.12.2's naming: a bed with a neighbour on its left is the "right" side of a wider bed, and the other way round
    private static Side sideAt(LevelReader level, BlockPos pos, Direction facing)
    {
        boolean right = joins(level, pos.relative(facing.getClockWise()), facing);
        boolean left = joins(level, pos.relative(facing.getCounterClockWise()), facing);
        if(left && right)
            return Side.BOTH;
        if(left)
            return Side.RIGHT;
        if(right)
            return Side.LEFT;
        return Side.NONE;
    }

    private static boolean joins(LevelReader level, BlockPos pos, Direction facing)
    {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof ModernBedBlock && state.getValue(FACING) == facing;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    public enum Side implements net.minecraft.util.StringRepresentable
    {
        NONE,
        LEFT,
        RIGHT,
        BOTH;

        @Override
        public String getSerializedName()
        {
            return this.name().toLowerCase(java.util.Locale.ROOT);
        }
    }
}
