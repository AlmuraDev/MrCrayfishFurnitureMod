package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.entity.SeatEntity;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The modern chair (from 1.12.2). Use it to sit.
 *
 * Author: MrCrayfish
 */
public class ModernChairBlock extends FurnitureHorizontalBlock
{
    private static final VoxelShape OUTLINE = Shapes.box(0.1, 0.0, 0.1, 0.9, 1.2, 0.9);
    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static
    {
        double p = 0.0625;
        List<VoxelShape> base = List.of(
                Shapes.box(1.6 * p, 8 * p, 1.6 * p, 14.4 * p, 9.6 * p, 14.4 * p),
                Shapes.box(1.6 * p, 0, 12.8 * p, 3.2 * p, 8 * p, 14.4 * p),
                Shapes.box(12.8 * p, 0, 12.8 * p, 14.4 * p, 8 * p, 14.4 * p),
                Shapes.box(12.8 * p, 0, 1.6 * p, 14.4 * p, 8 * p, 3.2 * p),
                Shapes.box(1.6 * p, 0, 1.6 * p, 3.2 * p, 8 * p, 3.2 * p));
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            VoxelShape backrest = VoxelShapeHelper.rotate(Shapes.box(0.825, 0.6, 0.1, 0.9, 1.2, 0.9), direction);
            SHAPES.put(direction, VoxelShapeHelper.combineAll(java.util.stream.Stream.concat(base.stream(), java.util.stream.Stream.of(backrest)).toList()));
        }
    }

    public ModernChairBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return OUTLINE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state.getValue(DIRECTION));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(player.isShiftKeyDown())
            return InteractionResult.PASS;
        return SeatEntity.create(level, pos, 6 * 0.0625, player, state.getValue(DIRECTION));
    }
}
