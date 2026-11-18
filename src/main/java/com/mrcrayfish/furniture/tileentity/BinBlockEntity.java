package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.inventory.container.BinMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The twelve slots in a bin, with its lid sounds (1.12.2).
 *
 * Author: MrCrayfish
 */
public class BinBlockEntity extends BasicLootBlockEntity
{
    public BinBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.BIN.get(), pos, state);
    }

    @Override
    public int getContainerSize()
    {
        return 12;
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.bin");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return new BinMenu(windowId, playerInventory, this);
    }

    @Override
    public boolean isMatchingContainerMenu(AbstractContainerMenu menu)
    {
        return menu instanceof BinMenu binMenu && binMenu.getBlockEntity() == this;
    }

    @Override
    public void onOpen(Level level, BlockPos pos, BlockState state)
    {
        level.playSound(null, pos, ModSounds.BLOCK_BIN_OPEN.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
    }

    @Override
    public void onClose(Level level, BlockPos pos, BlockState state)
    {
        level.playSound(null, pos, ModSounds.BLOCK_BIN_CLOSE.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
    }
}
