package com.mrcrayfish.furniture.client.gui.screen.inventory;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.inventory.container.FreezerMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * Author: MrCrayfish
 */
public class FreezerScreen extends AbstractContainerScreen<FreezerMenu>
{
    private static final Identifier FREEZER_GUI_TEXTURES = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/container/freezer.png");

    public FreezerScreen(FreezerMenu container, Inventory playerInventory, Component title)
    {
        super(container, playerInventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(RenderPipelines.GUI_TEXTURED, FREEZER_GUI_TEXTURES, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        if(this.menu.isFueling())
        {
            int fuelLeft = this.menu.getFuelLeftScaled();
            graphics.blit(RenderPipelines.GUI_TEXTURED, FREEZER_GUI_TEXTURES, this.leftPos + 57, this.topPos + 37 + 12 - fuelLeft, 176, 12 - fuelLeft, 14, fuelLeft + 1, 256, 256);
        }
        int progress = this.menu.getSolidifyProgressionScaled();
        graphics.blit(RenderPipelines.GUI_TEXTURED, FREEZER_GUI_TEXTURES, this.leftPos + 79, this.topPos + 34, 176, 14, progress + 1, 16, 256, 256);
    }
}
