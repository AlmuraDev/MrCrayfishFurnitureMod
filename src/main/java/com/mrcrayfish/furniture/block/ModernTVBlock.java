package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

/**
 * The modern flat-screen TV (1.12.2): it stands on its foot, or hangs on the wall when placed against the side of a block.
 *
 * Author: MrCrayfish
 */
public class ModernTVBlock extends TVBlock
{
    public static final BooleanProperty MOUNTED = BooleanProperty.create("mounted");

    private final Map<Direction, VoxelShape> mountedShapes = new EnumMap<>(Direction.class);

    public ModernTVBlock(Properties properties)
    {
        super(properties, Block.box(6, 0, -5, 11, 18, 21), 22, 12, 4, -0.35);
        VoxelShape mounted = Block.box(12, 2, -5, 16, 18, 21);
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            this.mountedShapes.put(direction, VoxelShapeHelper.rotate(mounted, direction));
        }
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(MOUNTED, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return state.getValue(MOUNTED) ? this.mountedShapes.get(state.getValue(DIRECTION)) : super.getShape(state, level, pos, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return super.getStateForPlacement(context).setValue(MOUNTED, context.getClickedFace().getAxis().isHorizontal());
    }

    @Override
    public double getScreenZ(BlockState state)
    {
        return state.getValue(MOUNTED) ? 4.65 : super.getScreenZ(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(MOUNTED);
    }
}
