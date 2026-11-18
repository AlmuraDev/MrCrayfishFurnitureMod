package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.tileentity.DigitalClockBlockEntity;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/**
 * A digital clock (1.12.2, one block per case colour) showing the time of day. Using it with a dye colours its numbers.
 *
 * Author: MrCrayfish
 */
public class DigitalClockBlock extends ApplianceBlock
{
    public DigitalClockBlock(Properties properties)
    {
        // 1.12.2 built this model facing north
        super(properties, VoxelShapeHelper.rotate(Block.box(3.8, 0, 7, 12.2, 4.5, 9.5), Direction.SOUTH));
    }

    @Override
    protected boolean opensMenu()
    {
        return false;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        DyeColor dye = stack.get(DataComponents.DYE);
        if(dye == null || !(level.getBlockEntity(pos) instanceof DigitalClockBlockEntity clock))
            return InteractionResult.TRY_WITH_EMPTY_HAND;

        if(clock.getTextColour() != dye && !level.isClientSide())
        {
            clock.setTextColour(dye);
            stack.consume(1, player);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DigitalClockBlockEntity(pos, state);
    }
}
