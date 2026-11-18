package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The nine slots in a TV stand. It sounds like a cabinet when opened and closed, as in 1.12.2.
 *
 * Author: MrCrayfish
 */
public class TVStandBlockEntity extends BasicLootBlockEntity
{
    public TVStandBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.TV_STAND.get(), pos, state);
    }

    @Override
    public int getContainerSize()
    {
        return 9;
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.tv_stand");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return new ChestMenu(MenuType.GENERIC_9x1, windowId, playerInventory, this, 1);
    }

    @Override
    public void onOpen(Level level, BlockPos pos, BlockState state)
    {
        this.playSound(level, ModSounds.BLOCK_CABINET_OPEN.get(), 0.9F);
    }

    @Override
    public void onClose(Level level, BlockPos pos, BlockState state)
    {
        this.playSound(level, ModSounds.BLOCK_CABINET_CLOSE.get(), 0.8F);
    }

    private void playSound(Level level, SoundEvent sound, float pitch)
    {
        level.playSound(null, this.worldPosition, sound, SoundSource.BLOCKS, 0.75F, pitch);
    }
}
