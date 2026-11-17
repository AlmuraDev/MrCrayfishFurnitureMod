package com.mrcrayfish.furniture.client.gui.screen.inventory;

import com.mrcrayfish.furniture.inventory.container.OvenMenu;
import com.mrcrayfish.furniture.tileentity.OvenBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * The oven screen (1.12.2): a flame under the food baking shows how far along it is.
 *
 * Author: MrCrayfish
 */
public class OvenScreen extends ApplianceScreen<OvenMenu>
{
    public OvenScreen(OvenMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title, "oven", 228);
        this.titleLabelX = 70;
    }

    @Override
    protected boolean isRunning()
    {
        return this.menu.getData(2) >= 0;
    }

    @Override
    protected void extractProgress(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        int slot = this.menu.getData(2);
        int total = this.menu.getData(1);
        if(slot >= 0 && slot < OvenBlockEntity.ROW && total > 0)
        {
            int progress = this.menu.getData(0) * 14 / total;
            this.blit(graphics, 54 + slot * 18, 55 + 14 - progress, 176, 14 - progress, 14, progress);
        }
        this.extractRunningLight(graphics, mouseX, mouseY);
    }
}
