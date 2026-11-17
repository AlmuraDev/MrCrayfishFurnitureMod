package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.tileentity.OvenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * An oven (1.12.2): it bakes the food in its top row of slots, one slot at a time, into the slot below it.
 *
 * Author: MrCrayfish
 */
public class OvenBlock extends ApplianceBlock
{
    public OvenBlock(Properties properties)
    {
        super(properties, Block.box(0.5, 0, 1, 16, 16, 15));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new OvenBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return createTickerHelper(type, ModBlockEntities.OVEN.get(), level.isClientSide() ? OvenBlockEntity::clientTick : OvenBlockEntity::serverTick);
    }
}
