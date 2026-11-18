package com.mrcrayfish.furniture.inventory.container;

import com.mrcrayfish.furniture.core.ModContainers;
import com.mrcrayfish.furniture.tileentity.BinBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;

/**
 * The bin: four rows of three slots, and Empty (a menu button), which destroys everything in it.
 *
 * Author: MrCrayfish
 */
public class BinMenu extends ApplianceMenu<BinBlockEntity>
{
    public static final int BUTTON_EMPTY = 0;

    public BinMenu(int windowId, Inventory playerInventory, BinBlockEntity blockEntity)
    {
        super(ModContainers.BIN.get(), windowId, playerInventory, blockEntity, new SimpleContainerData(0));
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                this.addSlot(new Slot(blockEntity, j + i * 3, 62 + j * 18, 18 + i * 18));
            }
        }
        this.addPlayerSlots(playerInventory, 115);
    }

    @Override
    public boolean clickMenuButton(Player player, int id)
    {
        if(id != BUTTON_EMPTY)
            return false;
        this.blockEntity.clearContent();
        this.blockEntity.setChanged();
        return true;
    }
}
