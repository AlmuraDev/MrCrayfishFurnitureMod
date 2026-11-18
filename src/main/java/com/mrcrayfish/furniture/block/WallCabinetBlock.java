package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.tileentity.BasicLootBlockEntity;
import com.mrcrayfish.furniture.tileentity.WallCabinetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/**
 * A wall cabinet (1.12.2), the bathroom kind: nine slots, hung on the side of a block. Hoppers only put potions in.
 *
 * Author: MrCrayfish
 */
public class WallCabinetBlock extends WallFurnitureBlock implements EntityBlock
{
    public WallCabinetBlock(Properties properties)
    {
        super(properties, Block.box(10.96, 1.6, 2, 16, 14.4, 14));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!level.isClientSide() && level.getBlockEntity(pos) instanceof WallCabinetBlockEntity cabinet)
        {
            player.openMenu(cabinet);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if(level.getBlockEntity(pos) instanceof BasicLootBlockEntity blockEntity)
        {
            blockEntity.updateOpenerCount();
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new WallCabinetBlockEntity(pos, state);
    }
}
