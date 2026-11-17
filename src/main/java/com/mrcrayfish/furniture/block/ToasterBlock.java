package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.tileentity.ToasterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/**
 * A toaster (1.12.2). Use it with a slice of bread to put the slice in (two fit); sneak and use it with an empty hand to toast them, and
 * the toast pops out. Using it with an empty hand otherwise takes a slice back out.
 *
 * Author: MrCrayfish
 */
public class ToasterBlock extends ApplianceBlock
{
    public ToasterBlock(Properties properties)
    {
        super(properties, Block.box(5, 0, 2.5, 11, 7, 13));
    }

    @Override
    protected boolean opensMenu()
    {
        return false;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if(level.getBlockEntity(pos) instanceof ToasterBlockEntity toaster && !toaster.isRunning() && toaster.canToast(stack))
        {
            if(!level.isClientSide() && toaster.addSlice(stack.copyWithCount(1)))
            {
                stack.consume(1, player);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(level.getBlockEntity(pos) instanceof ToasterBlockEntity toaster && !toaster.isRunning())
        {
            if(!level.isClientSide())
            {
                if(player.isSecondaryUseActive())
                {
                    toaster.startToasting();
                }
                else
                {
                    toaster.removeSlice();
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ToasterBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return level.isClientSide() ? null : createTickerHelper(type, ModBlockEntities.TOASTER.get(), ToasterBlockEntity::serverTick);
    }
}
