package com.mrcrayfish.furniture.client.gui.screen.inventory;

import com.mrcrayfish.furniture.inventory.container.PrinterMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * The printer screen (1.12.2): how much ink is left and how far the copy is along, side by side.
 *
 * Author: MrCrayfish
 */
public class PrinterScreen extends ApplianceScreen<PrinterMenu>
{
    public PrinterScreen(PrinterMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title, "printer", 185);
        this.inventoryLabelY = 92;
    }

    @Override
    protected boolean isRunning()
    {
        return this.menu.getData(0) > 0 && this.menu.getData(2) > 0;
    }

    @Override
    protected void extractProgress(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        int ink = this.menu.getData(0);
        int inkTotal = this.menu.getData(1);
        int print = this.menu.getData(2);
        int printTotal = this.menu.getData(3);
        if(ink > 0 && inkTotal > 0)
        {
            int height = ink * 16 / inkTotal;
            this.blit(graphics, 74, 46 - height, 179, 16 - height, 3, height);
        }
        if(print > 0 && printTotal > 0)
        {
            int height = print * 16 / printTotal;
            this.blit(graphics, 80, 45 - height, 176, 15 - height, 3, height + 1);
        }

        if(this.isHovering(73, 30, 5, 18, mouseX, mouseY))
        {
            graphics.setTooltipForNextFrame(Component.translatable("gui.cfm.ink_level", ink, inkTotal), mouseX, mouseY);
        }
        if(this.isHovering(79, 30, 5, 18, mouseX, mouseY))
        {
            graphics.setTooltipForNextFrame(Component.translatable("gui.cfm.print_progress", print, printTotal), mouseX, mouseY);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        graphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
    }
}
