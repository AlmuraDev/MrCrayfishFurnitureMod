package com.mrcrayfish.furniture.inventory.container;

import com.mrcrayfish.furniture.tileentity.BasicLootBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * The screen side of an appliance: its slots, then the player's inventory, and the numbers its screen shows.
 *
 * Author: MrCrayfish
 */
public abstract class ApplianceMenu<T extends BasicLootBlockEntity> extends AbstractContainerMenu
{
    protected final T blockEntity;
    protected final ContainerData data;
    private final int containerSlots;

    protected ApplianceMenu(MenuType<?> type, int windowId, Inventory playerInventory, T blockEntity, ContainerData data)
    {
        super(type, windowId);
        checkContainerSize(blockEntity, blockEntity.getContainerSize());
        blockEntity.startOpen(playerInventory.player);
        this.blockEntity = blockEntity;
        this.data = data;
        this.containerSlots = blockEntity.getContainerSize();
        this.addDataSlots(data);
    }

    /**
     * Adds the player's inventory with its top row at the given height; called after the appliance's own slots.
     */
    protected void addPlayerSlots(Inventory playerInventory, int y)
    {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, y + i * 18));
            }
        }

        for(int i = 0; i < 9; i++)
        {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, y + 58));
        }
    }

    public T getBlockEntity()
    {
        return this.blockEntity;
    }

    public int getData(int index)
    {
        return this.data.get(index);
    }

    @Override
    public boolean stillValid(Player player)
    {
        return this.blockEntity.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        Slot slot = this.slots.get(index);
        if(!slot.hasItem())
            return ItemStack.EMPTY;

        ItemStack slotStack = slot.getItem();
        ItemStack copyStack = slotStack.copy();
        int inventoryStart = this.containerSlots;
        int hotbarStart = inventoryStart + 27;
        int end = hotbarStart + 9;
        if(index < inventoryStart)
        {
            if(!this.moveItemStackTo(slotStack, inventoryStart, end, true))
                return ItemStack.EMPTY;
            slot.onQuickCraft(slotStack, copyStack);
        }
        else if(!this.moveItemStackTo(slotStack, 0, inventoryStart, false))
        {
            if(index < hotbarStart)
            {
                if(!this.moveItemStackTo(slotStack, hotbarStart, end, false))
                    return ItemStack.EMPTY;
            }
            else if(!this.moveItemStackTo(slotStack, inventoryStart, hotbarStart, false))
            {
                return ItemStack.EMPTY;
            }
        }

        if(slotStack.isEmpty())
        {
            slot.set(ItemStack.EMPTY);
        }
        else
        {
            slot.setChanged();
        }

        if(slotStack.getCount() == copyStack.getCount())
            return ItemStack.EMPTY;

        slot.onTake(player, slotStack);
        return copyStack;
    }

    @Override
    public void removed(Player player)
    {
        super.removed(player);
        this.blockEntity.stopOpen(player);
    }
}
