package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import com.mrcrayfish.furniture.util.WaterHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

/**
 * An outdoor tap. Using it moves a water source from two blocks below to the space in front of the tap (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class TapBlock extends FurnitureHorizontalBlock
{
    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static
    {
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            SHAPES.put(direction, VoxelShapeHelper.rotate(Shapes.box(0.125, 0.0, 0.4, 0.5625, 1.0, 0.6), direction));
        }
    }

    public TapBlock(Properties properties)
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!level.isClientSide())
        {
            BlockPos spout = pos.relative(state.getValue(DIRECTION).getOpposite());
            if(!WaterHelper.hasWaterSource(level, pos))
            {
                player.sendSystemMessage(Component.translatable("message.cfm.tap_no_water"));
            }
            else if(!level.isEmptyBlock(spout))
            {
                player.sendSystemMessage(Component.translatable("message.cfm.tap_blocked"));
            }
            else
            {
                WaterHelper.takeWaterSource(level, pos);
                level.setBlockAndUpdate(spout, Blocks.WATER.defaultBlockState());
                level.playSound(null, pos, ModSounds.BLOCK_TAP.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
