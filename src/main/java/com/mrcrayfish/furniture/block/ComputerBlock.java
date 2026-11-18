package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.tileentity.ComputerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * A computer (1.12.2) running MineBay, a shop that sells items for emeralds and other currencies (cfm:minebay recipes).
 *
 * Author: MrCrayfish
 */
public class ComputerBlock extends ApplianceBlock
{
    public ComputerBlock(Properties properties)
    {
        super(properties, Block.box(0.5, 0, 1, 14.5, 16, 15));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ComputerBlockEntity(pos, state);
    }
}
