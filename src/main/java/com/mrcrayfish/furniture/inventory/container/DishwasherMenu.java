package com.mrcrayfish.furniture.inventory.container;

import com.mrcrayfish.furniture.core.ModContainers;
import com.mrcrayfish.furniture.inventory.container.slot.ContainerRuleSlot;
import com.mrcrayfish.furniture.tileentity.DishwasherBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

/**
 * The dishwasher: six tool slots, the soapy water, and the Start/Stop button.
 *
 * Author: MrCrayfish
 */
public class DishwasherMenu extends ApplianceMenu<DishwasherBlockEntity>
{
    public DishwasherMenu(int windowId, Inventory playerInventory, DishwasherBlockEntity blockEntity)
    {
        super(ModContainers.DISHWASHER.get(), windowId, playerInventory, blockEntity, blockEntity.getWasherData());
        for(int i = 0; i < DishwasherBlockEntity.TOOL_SLOTS; i++)
        {
            this.addSlot(new ContainerRuleSlot(blockEntity, i, 56 + (i % 3) * 24, 43 + (i / 3) * 31));
        }
        this.addSlot(new ContainerRuleSlot(blockEntity, DishwasherBlockEntity.TOOL_SLOTS, 125, 7));
        this.addPlayerSlots(playerInventory, 146);
    }

    @Override
    public boolean clickMenuButton(Player player, int id)
    {
        if(id != DishwasherBlockEntity.BUTTON_START_STOP)
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
