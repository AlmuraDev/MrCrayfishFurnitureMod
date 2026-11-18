package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.tileentity.PrinterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * A printer (1.12.2): with ink it copies written and enchanted books (#cfm:printable).
 *
 * Author: MrCrayfish
 */
public class PrinterBlock extends ApplianceBlock
{
    public PrinterBlock(Properties properties)
    {
        super(properties, Block.box(0, 0, 1, 15.8, 12.2, 15));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new PrinterBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return level.isClientSide() ? null : createTickerHelper(type, ModBlockEntities.PRINTER.get(), PrinterBlockEntity::serverTick);
    }
}
