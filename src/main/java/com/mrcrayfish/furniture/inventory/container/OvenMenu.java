package com.mrcrayfish.furniture.inventory.container;

import com.mrcrayfish.furniture.core.ModContainers;
import com.mrcrayfish.furniture.inventory.container.slot.ContainerRuleSlot;
import com.mrcrayfish.furniture.tileentity.OvenBlockEntity;
import net.minecraft.world.entity.player.Inventory;

/**
 * The oven: four slots of food on top, the baked food below.
 *
 * Author: MrCrayfish
 */
public class OvenMenu extends ApplianceMenu<OvenBlockEntity>
{
    public OvenMenu(int windowId, Inventory playerInventory, OvenBlockEntity blockEntity)
    {
        super(ModContainers.OVEN.get(), windowId, playerInventory, blockEntity, blockEntity.getOvenData());
        for(int i = 0; i < OvenBlockEntity.ROW; i++)
        {
            this.addSlot(new ContainerRuleSlot(blockEntity, i, 53 + i * 18, 31));
        }
        for(int i = 0; i < OvenBlockEntity.ROW; i++)
        {
            this.addSlot(new ContainerRuleSlot(blockEntity, i + OvenBlockEntity.ROW, 53 + i * 18, 73));
        }
        this.addPlayerSlots(playerInventory, 146);
    }
}
