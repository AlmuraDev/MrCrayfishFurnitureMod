package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import com.mrcrayfish.furniture.util.WaterHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A bathroom basin. It holds one lot of water: fill it with a bucket or bottle, or by hand from a water source two blocks below; take
 * it out with a bucket or bottle. A comparator shows whether it's full (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class BasinBlock extends FurnitureHorizontalBlock
{
    public static final BooleanProperty FILLED = BooleanProperty.create("filled");

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static
    {
        VoxelShape top = Block.box(0, 11, 0, 16, 16, 16);
        SHAPES.put(Direction.SOUTH, VoxelShapeHelper.combineAll(List.of(top, Block.box(3, 0, 8, 13, 11, 16))));
        SHAPES.put(Direction.WEST, VoxelShapeHelper.combineAll(List.of(top, Block.box(0, 0, 3, 8, 11, 13))));
        SHAPES.put(Direction.NORTH, VoxelShapeHelper.combineAll(List.of(top, Block.box(3, 0, 0, 13, 11, 8))));
        SHAPES.put(Direction.EAST, VoxelShapeHelper.combineAll(List.of(top, Block.box(8, 0, 3, 16, 11, 13))));
    }

    public BasinBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(FILLED, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state.getValue(DIRECTION));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack heldItem, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        WaterHelper.Use use = WaterHelper.useOf(heldItem);
        boolean filled = state.getValue(FILLED);
        if(use == WaterHelper.Use.TAKE && filled || use == WaterHelper.Use.ADD && !filled)
        {
            if(!level.isClientSide())
            {
                WaterHelper.exchange(level, pos, player, hand, heldItem, use);
                this.setFilled(level, pos, state, use == WaterHelper.Use.ADD);
                if(use == WaterHelper.Use.ADD)
                {
                    WaterHelper.splash(level, pos, 1.05);
                }
            }
            return InteractionResult.SUCCESS;
        }
        // A bucket or bottle that can't be used here does nothing (and doesn't scoop up water behind the basin)
        if(use != WaterHelper.Use.NONE)
        {
            return InteractionResult.CONSUME;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    // With an empty hand (or any other item), the tap fills the basin from a water source two blocks below
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!level.isClientSide() && !state.getValue(FILLED))
        {
            if(WaterHelper.hasWaterSource(level, pos))
            {
                WaterHelper.takeWaterSource(level, pos);
                this.setFilled(level, pos, state, true);
                level.playSound(null, pos, ModSounds.BLOCK_TAP.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
                WaterHelper.splash(level, pos, 1.05);
            }
            else
            {
                player.sendSystemMessage(Component.translatable("message.cfm.basin"));
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void setFilled(Level level, BlockPos pos, BlockState state, boolean filled)
    {
        level.setBlock(pos, state.setValue(FILLED, filled), Block.UPDATE_ALL);
        level.updateNeighbourForOutputSignal(pos, this);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction)
    {
        return state.getValue(FILLED) ? 1 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FILLED);
    }
}
