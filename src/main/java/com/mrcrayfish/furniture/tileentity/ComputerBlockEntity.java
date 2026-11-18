package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.inventory.container.ComputerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A computer: the one slot holds the money (emeralds for most things) that MineBay takes.
 *
 * Author: MrCrayfish
 */
public class ComputerBlockEntity extends BasicLootBlockEntity
{
    public ComputerBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.COMPUTER.get(), pos, state);
    }

    @Override
    public int getContainerSize()
    {
        return 1;
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.computer");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return new ComputerMenu(windowId, playerInventory, this);
    }

    @Override
    public boolean isMatchingContainerMenu(AbstractContainerMenu menu)
    {
        return menu instanceof ComputerMenu computerMenu && computerMenu.getBlockEntity() == this;
    }

    // Hoppers may fill the money slot but not empty it
    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return false;
    }
}
