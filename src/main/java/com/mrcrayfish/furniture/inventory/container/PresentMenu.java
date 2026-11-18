package com.mrcrayfish.furniture.inventory.container;

import com.mrcrayfish.furniture.core.ModContainers;
import com.mrcrayfish.furniture.core.ModDataComponents;
import com.mrcrayfish.furniture.item.PresentItem;
import com.mrcrayfish.furniture.tileentity.PresentBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/**
 * Filling the present in the player's main hand (1.12.2). What goes in is kept on the present itself as it changes, and the present
 * can't be moved while it's open. Wrap (a menu button) signs it with the player's name, after which it can't be opened again.
 *
 * Author: MrCrayfish
 */
public class PresentMenu extends AbstractContainerMenu
{
    public static final int BUTTON_WRAP = 0;

    private final Player player;
    private final ItemStack present;
    private final int presentSlot;
    private final SimpleContainer contents = new SimpleContainer(PresentBlockEntity.SIZE)
    {
        @Override
        public void setChanged()
        {
            super.setChanged();
            PresentMenu.this.save();
        }
    };

    public PresentMenu(int windowId, Inventory playerInventory)
    {
        super(ModContainers.PRESENT.get(), windowId);
        this.player = playerInventory.player;
        this.present = this.player.getMainHandItem();
        this.presentSlot = playerInventory.getSelectedSlot();

        this.present.getOrDefault(ModDataComponents.PRESENT_CONTENTS.get(), ItemContainerContents.EMPTY).copyInto(this.contents.getItems());

        for(int i = 0; i < PresentBlockEntity.SIZE; i++)
        {
            this.addSlot(new Slot(this.contents, i, 71 + (i % 2) * 18, 16 + (i / 2) * 18)
            {
                @Override
                public boolean mayPlace(ItemStack stack)
                {
                    return !(stack.getItem() instanceof PresentItem);
                }
            });
        }

        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for(int i = 0; i < 9; i++)
        {
            boolean locked = i == this.presentSlot;
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142)
            {
                @Override
                public boolean mayPickup(Player player)
                {
                    return !locked;
                }

                @Override
                public boolean mayPlace(ItemStack stack)
                {
                    return !locked;
                }
            });
        }
    }

    private void save()
    {
        if(!this.player.level().isClientSide())
        {
            this.present.set(ModDataComponents.PRESENT_CONTENTS.get(), ItemContainerContents.fromItems(this.contents.getItems()));
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id)
    {
        if(id != BUTTON_WRAP || PresentItem.isWrapped(this.present))
            return false;

        if(this.contents.isEmpty())
        {
            player.sendSystemMessage(Component.translatable("message.cfm.present_empty"));
            return true;
        }

        this.save();
        this.present.set(ModDataComponents.PRESENT_FROM.get(), player.getName().getString());
        player.closeContainer();
        return true;
    }

    // Open only while the present is still in hand, unwrapped
    @Override
    public boolean stillValid(Player player)
    {
        return player.getMainHandItem() == this.present && !PresentItem.isWrapped(this.present);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        Slot slot = this.slots.get(index);
        if(!slot.hasItem() || !slot.mayPickup(player))
            return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        int size = PresentBlockEntity.SIZE;
        if(index < size)
        {
            if(!this.moveItemStackTo(stack, size, this.slots.size(), true))
                return ItemStack.EMPTY;
        }
        else if(!this.moveItemStackTo(stack, 0, size, false))
        {
            return ItemStack.EMPTY;
        }

        if(stack.isEmpty())
        {
            slot.set(ItemStack.EMPTY);
        }
        else
        {
            slot.setChanged();
        }
        return copy;
    }
}
