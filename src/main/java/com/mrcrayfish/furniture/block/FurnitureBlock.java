package com.mrcrayfish.furniture.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import org.jspecify.annotations.Nullable;

/**
 * Author: MrCrayfish
 */
public abstract class FurnitureBlock extends Block
{
    public FurnitureBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction)
    {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return state.getBlock() instanceof EntityBlock;
    }

    // Containers drop their items themselves (BlockEntity#preRemoveSideEffects); comparators next to them update here
    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston)
    {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    @Override
    protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int type)
    {
        super.triggerEvent(state, level, pos, id, type);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(id, type);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type1, BlockEntityType<E> type2, BlockEntityTicker<? super E> ticker)
    {
        return type2 == type1 ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type)
    {
        return false;
    }
}
