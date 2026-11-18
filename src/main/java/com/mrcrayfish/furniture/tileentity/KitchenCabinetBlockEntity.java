package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The two rows of slots in a kitchen cabinet (1.12.2 had 15; a chest-style screen needs whole rows).
 *
 * Author: MrCrayfish
 */
public class KitchenCabinetBlockEntity extends BasicLootBlockEntity
{
    public KitchenCabinetBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.KITCHEN_CABINET.get(), pos, state);
    }

    @Override
    public int getContainerSize()
    {
        return 18;
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.kitchen_cabinet");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return new ChestMenu(MenuType.GENERIC_9x2, windowId, playerInventory, this, 2);
    }

    @Override
    public void onOpen(Level level, BlockPos pos, BlockState state)
    {
        level.playSound(null, pos, ModSounds.BLOCK_CABINET_OPEN.get(), SoundSource.BLOCKS, 0.75F, 0.9F);
    }

    @Override
    public void onClose(Level level, BlockPos pos, BlockState state)
    {
        level.playSound(null, pos, ModSounds.BLOCK_CABINET_CLOSE.get(), SoundSource.BLOCKS, 0.75F, 0.8F);
    }
}
