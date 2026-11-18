package com.mrcrayfish.furniture.inventory.container;

import com.mrcrayfish.furniture.core.ModContainers;
import com.mrcrayfish.furniture.inventory.container.slot.ContainerRuleSlot;
import com.mrcrayfish.furniture.tileentity.PrinterBlockEntity;
import net.minecraft.world.entity.player.Inventory;

/**
 * The printer: the book on top, the ink at the side and the copy below.
 *
 * Author: MrCrayfish
 */
public class PrinterMenu extends ApplianceMenu<PrinterBlockEntity>
{
    public PrinterMenu(int windowId, Inventory playerInventory, PrinterBlockEntity blockEntity)
    {
        super(ModContainers.PRINTER.get(), windowId, playerInventory, blockEntity, blockEntity.getPrinterData());
        this.addSlot(new ContainerRuleSlot(blockEntity, PrinterBlockEntity.SLOT_INPUT, 80, 5));
        this.addSlot(new ContainerRuleSlot(blockEntity, PrinterBlockEntity.SLOT_INK, 55, 30));
        this.addSlot(new ContainerRuleSlot(blockEntity, PrinterBlockEntity.SLOT_OUTPUT, 80, 61));
        this.addPlayerSlots(playerInventory, 103);
    }
}
