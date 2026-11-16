package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.entity.SeatEntity;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A toilet. Use it to sit down; use it while sneaking to flush away the items lying around it. A comparator next to it shows whether
 * someone is sitting on it (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class ToiletBlock extends FurnitureHorizontalBlock
{
    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> COLLISION_SHAPES = new EnumMap<>(Direction.class);

    static
    {
        VoxelShape seat = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            SHAPES.put(direction, VoxelShapeHelper.rotate(Shapes.box(0.1, 0.0, 0.15, 1.0, 1.0, 0.85), direction));
            COLLISION_SHAPES.put(direction, VoxelShapeHelper.combineAll(List.of(seat, VoxelShapeHelper.rotate(Shapes.box(0.65, 0.5, 0.0, 1.0, 1.1, 1.0), direction))));
        }
    }

    public ToiletBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state.getValue(DIRECTION));
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return COLLISION_SHAPES.get(state.getValue(DIRECTION));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!player.isShiftKeyDown())
        {
            InteractionResult sat = SeatEntity.create(level, pos, 0.4, player, state.getValue(DIRECTION));
            level.updateNeighbourForOutputSignal(pos, this);
            return sat;
        }

        if(!level.isClientSide())
        {
            for(ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(1.0)))
            {
                item.discard();
            }
            level.playSound(null, pos, ModSounds.BLOCK_TOILET_FLUSH.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction)
    {
        return level.getEntitiesOfClass(SeatEntity.class, new AABB(pos)).stream().anyMatch(seat -> !seat.getPassengers().isEmpty()) ? 1 : 0;
    }
}
