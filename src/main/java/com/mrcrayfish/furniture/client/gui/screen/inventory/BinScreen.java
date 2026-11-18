package com.mrcrayfish.furniture.client.gui.screen.inventory;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.inventory.container.BinMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * The bin screen (1.12.2): its twelve slots and the Empty button.
 *
 * Author: MrCrayfish
 */
public class BinScreen extends AbstractContainerScreen<BinMenu>
{
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/container/bin.png");

    public BinScreen(BinMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title, 176, 197);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init()
    {
        super.init();
        this.addRenderableWidget(Button.builder(Component.translatable("gui.cfm.empty"), button -> {
            if(this.minecraft.gameMode != null)
            {
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, BinMenu.BUTTON_EMPTY);
            }
        }).bounds(this.leftPos + 128, this.topPos + 48, 40, 20).build());
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }
}
