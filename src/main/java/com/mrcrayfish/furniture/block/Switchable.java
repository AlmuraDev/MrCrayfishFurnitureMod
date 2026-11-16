package com.mrcrayfish.furniture.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A light a light switch can turn on and off (IPowered in 1.12.2).
 */
public interface Switchable
{
    void setPowered(Level level, BlockPos pos, BlockState state, boolean powered);
}
