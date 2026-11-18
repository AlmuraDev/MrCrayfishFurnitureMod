package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The food on a chopping board, waiting for the knife.
 *
 * Author: MrCrayfish
 */
public class ChoppingBoardBlockEntity extends DisplayItemBlockEntity
{
    public ChoppingBoardBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.CHOPPING_BOARD.get(), pos, state);
    }
}
