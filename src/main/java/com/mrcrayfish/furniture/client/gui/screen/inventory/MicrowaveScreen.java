package com.mrcrayfish.furniture.client.gui.screen.inventory;

import com.mrcrayfish.furniture.inventory.container.MicrowaveMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * The microwave screen (1.12.2): the item, a progress bar and the Start/Stop button.
 *
 * Author: MrCrayfish
 */
public class MicrowaveScreen extends ApplianceScreen<MicrowaveMenu>
{
    public MicrowaveScreen(MicrowaveMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title, "microwave", 174);
    }

    @Override
    protected boolean isRunning()
    {
        return this.menu.getData(2) != 0;
    }

    @Override
    protected int[] startButtonPosition()
    {
        return new int[]{118, 39};
    }

    @Override
    protected void extractProgress(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        int total = this.menu.getData(1);
        int percent = this.isRunning() && total > 0 ? this.menu.getData(0) * 100 / total : 0;
        this.blit(graphics, 120, 26, 176, 0, percent * 27 / 100, 5);
        if(this.isHovering(120, 26, 27, 5, mouseX, mouseY))
        {
            graphics.setTooltipForNextFrame(Component.translatable("gui.cfm.progress", percent), mouseX, mouseY);
        }
    }

    // The microwave has no room for its name
    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        graphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
    }
}
