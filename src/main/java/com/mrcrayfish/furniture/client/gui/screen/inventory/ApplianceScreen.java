package com.mrcrayfish.furniture.client.gui.screen.inventory;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.inventory.container.ApplianceMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.Nullable;

/**
 * An appliance screen from 1.12.2: its texture, the light showing whether it runs, and the Start/Stop button for those that have one
 * (pressing it is a menu button click, handled by the menu on the server).
 *
 * Author: MrCrayfish
 */
public abstract class ApplianceScreen<T extends ApplianceMenu<?>> extends AbstractContainerScreen<T>
{
    private static final int RUNNING = 0xFF00FF00;
    private static final int STOPPED = 0xFFFF0000;

    protected final Identifier texture;
    @Nullable
    private Button startButton;

    protected ApplianceScreen(T menu, Inventory playerInventory, Component title, String texture, int imageHeight)
    {
        super(menu, playerInventory, title, 176, imageHeight);
        this.texture = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/container/" + texture + ".png");
        this.inventoryLabelY = imageHeight - 94;
    }

    protected abstract boolean isRunning();

    /**
     * Where the Start/Stop button goes, as {x, y} inside the screen, or null for none.
     */
    protected int @Nullable [] startButtonPosition()
    {
        return null;
    }

    @Override
    protected void init()
    {
        super.init();
        int[] position = this.startButtonPosition();
        if(position != null)
        {
            this.startButton = this.addRenderableWidget(Button.builder(this.startButtonLabel(), button -> {
                if(this.minecraft.gameMode != null)
                {
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
                }
            }).bounds(this.leftPos + position[0], this.topPos + position[1], 32, 20).build());
        }
    }

    private Component startButtonLabel()
    {
        return Component.translatable(this.isRunning() ? "gui.cfm.stop" : "gui.cfm.start");
    }

    @Override
    protected void containerTick()
    {
        super.containerTick();
        if(this.startButton != null)
        {
            this.startButton.setMessage(this.startButtonLabel());
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        this.blit(graphics, 0, 0, 0, 0, this.imageWidth, this.imageHeight);
        this.extractProgress(graphics, mouseX, mouseY);
    }

    protected abstract void extractProgress(GuiGraphicsExtractor graphics, int mouseX, int mouseY);

    /**
     * Draws part of the screen texture at a place inside the screen.
     */
    protected void blit(GuiGraphicsExtractor graphics, int x, int y, int u, int v, int width, int height)
    {
        if(width > 0 && height > 0)
        {
            graphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, this.leftPos + x, this.topPos + y, u, v, width, height, 256, 256);
        }
    }

    /**
     * The light in the corner: green while it runs, red while it doesn't.
     */
    protected void extractRunningLight(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        graphics.fill(this.leftPos + 37, this.topPos + 9, this.leftPos + 48, this.topPos + 20, this.isRunning() ? RUNNING : STOPPED);
        if(this.isHovering(37, 9, 11, 11, mouseX, mouseY))
        {
            graphics.setTooltipForNextFrame(Component.translatable(this.isRunning() ? "gui.cfm.running" : "gui.cfm.stopped"), mouseX, mouseY);
        }
    }
}
