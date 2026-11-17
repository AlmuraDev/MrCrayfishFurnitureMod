package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.tileentity.Appliance;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

/**
 * A kitchen or laundry appliance from 1.12.2. Comparators read 1 while it runs, as in 1.12.2. Appliances with a screen open it when
 * used ({@link #opensMenu()}).
 *
 * Author: MrCrayfish
 */
public abstract class ApplianceBlock extends FurnitureHorizontalBlock implements EntityBlock
{
    private final Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);

    /**
     * @param shape the shape facing east, the way 1.12.2 built the models
     */
    protected ApplianceBlock(Properties properties, VoxelShape shape)
    {
        super(properties);
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            this.shapes.put(direction, VoxelShapeHelper.rotate(shape, direction));
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return this.shapes.get(state.getValue(DIRECTION));
    }

    protected boolean opensMenu()
    {
        return true;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!this.opensMenu())
            return InteractionResult.PASS;

        if(!level.isClientSide() && level.getBlockEntity(pos) instanceof MenuProvider provider)
        {
            player.openMenu(provider, pos);
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
        return level.getBlockEntity(pos) instanceof Appliance appliance && appliance.isRunning() ? 1 : 0;
    }
}
