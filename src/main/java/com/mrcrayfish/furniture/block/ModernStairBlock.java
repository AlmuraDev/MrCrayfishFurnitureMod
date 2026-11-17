package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A floating modern stair step (from 1.12.2). Steps placed one up and forward of each other join into a flight; the model changes with
 * the steps above and below.
 *
 * Author: MrCrayfish
 */
public class ModernStairBlock extends FurnitureHorizontalBlock
{
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static
    {
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            SHAPES.put(direction, VoxelShapeHelper.combineAll(List.of(
                    VoxelShapeHelper.rotate(Block.box(0, 0, 0, 16, 8, 16), direction),
                    VoxelShapeHelper.rotate(Block.box(8, 0, 0, 16, 16, 16), direction))));
        }
    }

    public ModernStairBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(TYPE, Type.NONE));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state.getValue(DIRECTION));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = super.getStateForPlacement(context);
        return state.setValue(TYPE, this.typeAt(context.getLevel(), context.getClickedPos(), state.getValue(DIRECTION)));
    }

    // The steps a flight joins are diagonal neighbours, which don't get shape updates, so they are refreshed here
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
    {
        if(!oldState.is(this))
        {
            this.refreshNeighbours(level, pos);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston)
    {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        this.refreshNeighbours(level, pos);
    }

    private void refreshNeighbours(Level level, BlockPos pos)
    {
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            this.refresh(level, pos.above().relative(direction));
            this.refresh(level, pos.below().relative(direction));
        }
    }

    private void refresh(Level level, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);
        if(state.is(this))
        {
            BlockState updated = state.setValue(TYPE, this.typeAt(level, pos, state.getValue(DIRECTION)));
            if(updated != state)
            {
                level.setBlock(pos, updated, Block.UPDATE_CLIENTS);
            }
        }
    }

    private Type typeAt(LevelReader level, BlockPos pos, Direction facing)
    {
        BlockState upState = level.getBlockState(pos.above().relative(facing));
        BlockState downState = level.getBlockState(pos.below().relative(facing.getOpposite()));
        boolean up = upState.is(this) && upState.getValue(DIRECTION) == facing;
        boolean down = downState.is(this) && downState.getValue(DIRECTION) == facing;
        if(up && down)
            return Type.BOTH;
        if(up)
            return Type.DOWN;
        if(down)
            return Type.UP;
        return Type.NONE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    public enum Type implements StringRepresentable
    {
        NONE,
        UP,
        DOWN,
        BOTH;

        @Override
        public String getSerializedName()
        {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
