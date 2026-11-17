package com.mrcrayfish.furniture.inventory.container;

import com.mrcrayfish.furniture.core.ModContainers;
import com.mrcrayfish.furniture.inventory.container.slot.ContainerRuleSlot;
import com.mrcrayfish.furniture.tileentity.MicrowaveBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

/**
 * The microwave: one slot, and the Start/Stop button (a menu button, so no packet of its own).
 *
 * Author: MrCrayfish
 */
public class MicrowaveMenu extends ApplianceMenu<MicrowaveBlockEntity>
{
    public MicrowaveMenu(int windowId, Inventory playerInventory, MicrowaveBlockEntity blockEntity)
    {
        super(ModContainers.MICROWAVE.get(), windowId, playerInventory, blockEntity, blockEntity.getMicrowaveData());
        this.addSlot(new ContainerRuleSlot(blockEntity, 0, 65, 43));
        this.addPlayerSlots(playerInventory, 92);
    }

    @Override
    public boolean clickMenuButton(Player player, int id)
    {
        if(id != MicrowaveBlockEntity.BUTTON_START_STOP)
            return false;

        if(this.blockEntity.isRunning())
        {
            this.blockEntity.stop();
        }
        else
        {
            this.blockEntity.start();
        }
        return true;
    }
}
