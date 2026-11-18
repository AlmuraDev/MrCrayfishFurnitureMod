package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The food on a plate.
 *
 * Author: MrCrayfish
 */
public class PlateBlockEntity extends DisplayItemBlockEntity
{
    public PlateBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.PLATE.get(), pos, state);
    }
}
