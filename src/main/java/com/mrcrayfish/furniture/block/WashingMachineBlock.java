package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.tileentity.WashingMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * A washing machine (1.12.2): with soapy water it repairs the armour in it while it runs.
 *
 * Author: MrCrayfish
 */
public class WashingMachineBlock extends ApplianceBlock
{
    public WashingMachineBlock(Properties properties)
    {
        super(properties, Block.box(0, 0, 1, 15, 16, 15));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new WashingMachineBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return createTickerHelper(type, ModBlockEntities.WASHING_MACHINE.get(), level.isClientSide() ? WashingMachineBlockEntity::clientTick : WashingMachineBlockEntity::serverTick);
    }
}
