package com.mrcrayfish.furniture.inventory.container;

import com.mrcrayfish.furniture.core.ModContainers;
import com.mrcrayfish.furniture.inventory.container.slot.ContainerRuleSlot;
import com.mrcrayfish.furniture.tileentity.WashingMachineBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

/**
 * The washing machine: the four armour slots, the soapy water, and the Start/Stop button.
 *
 * Author: MrCrayfish
 */
public class WashingMachineMenu extends ApplianceMenu<WashingMachineBlockEntity>
{
    public WashingMachineMenu(int windowId, Inventory playerInventory, WashingMachineBlockEntity blockEntity)
    {
        super(ModContainers.WASHING_MACHINE.get(), windowId, playerInventory, blockEntity, blockEntity.getWasherData());
        this.addSlot(new ContainerRuleSlot(blockEntity, 0, 80, 44));
        this.addSlot(new ContainerRuleSlot(blockEntity, 1, 64, 60));
        this.addSlot(new ContainerRuleSlot(blockEntity, 2, 96, 60));
        this.addSlot(new ContainerRuleSlot(blockEntity, 3, 80, 76));
        this.addSlot(new ContainerRuleSlot(blockEntity, WashingMachineBlockEntity.ARMOUR_SLOTS, 125, 7));
        this.addPlayerSlots(playerInventory, 146);
    }

    @Override
    public boolean clickMenuButton(Player player, int id)
    {
        if(id != WashingMachineBlockEntity.BUTTON_START_STOP)
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
