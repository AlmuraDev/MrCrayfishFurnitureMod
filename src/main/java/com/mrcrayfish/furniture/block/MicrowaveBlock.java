package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.tileentity.MicrowaveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * A microwave (1.12.2): Start in its screen heats the one item in it.
 *
 * Author: MrCrayfish
 */
public class MicrowaveBlock extends ApplianceBlock
{
    public MicrowaveBlock(Properties properties)
    {
        super(properties, Block.box(3, 0, 1, 13, 8, 15));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new MicrowaveBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return createTickerHelper(type, ModBlockEntities.MICROWAVE.get(), level.isClientSide() ? MicrowaveBlockEntity::clientTick : MicrowaveBlockEntity::serverTick);
    }
}
