package com.mrcrayfish.furniture.client.gui.screen.inventory;

import com.mrcrayfish.furniture.inventory.container.ApplianceMenu;
import com.mrcrayfish.furniture.tileentity.WasherBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * The dishwasher and washing machine screens (1.12.2): the Start/Stop button, the light, a bar that fills each time it repairs the
 * items, and how much soap is left (gold with super soapy water).
 *
 * Author: MrCrayfish
 */
public class WasherScreen<T extends ApplianceMenu<? extends WasherBlockEntity>> extends ApplianceScreen<T>
{
    private final Layout layout;

    public WasherScreen(T menu, Inventory playerInventory, Component title, String texture, Layout layout)
    {
        super(menu, playerInventory, title, texture, 228);
        this.layout = layout;
    }

    @Override
    protected boolean isRunning()
    {
        return this.menu.getData(2) != 0;
    }

    private boolean isSuper()
    {
        return this.menu.getData(3) != 0;
    }

    @Override
    protected int[] startButtonPosition()
    {
        return new int[]{53, 5};
    }

    @Override
    protected void extractProgress(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        Layout l = this.layout;
        if(this.isRunning())
        {
            int interval = this.isSuper() ? 20 : 50;
            int height = (this.menu.getData(0) % interval) * l.progressHeight / interval;
            this.blit(graphics, l.progressX, l.progressBottom - height, 176, l.progressHeight - height, l.progressWidth, height);
        }

        int soap = Math.min(this.menu.getData(1), WasherBlockEntity.SOAP_TIME) * l.soapHeight / WasherBlockEntity.SOAP_TIME;
        this.blit(graphics, l.soapX, l.soapY + l.soapHeight - soap, l.soapU + (this.isSuper() ? l.superOffset : 0), 0, l.soapWidth, soap);
        this.blit(graphics, l.soapX, l.soapY, l.soapGlassU, 0, l.soapWidth, l.soapHeight);
        if(this.isHovering(l.soapX, l.soapY, l.soapWidth, l.soapHeight, mouseX, mouseY))
        {
            graphics.setTooltipForNextFrame(Component.translatable("gui.cfm.soap_level", this.menu.getData(1), WasherBlockEntity.SOAP_TIME), mouseX, mouseY);
        }
        this.extractRunningLight(graphics, mouseX, mouseY);
    }

    // There is no room for the name next to the button
    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        graphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
    }

    /**
     * Where the two textures put the bars.
     */
    public record Layout(int progressX, int progressBottom, int progressWidth, int progressHeight, int soapX, int soapY, int soapWidth, int soapHeight, int soapU, int superOffset, int soapGlassU)
    {
        public static final Layout DISHWASHER = new Layout(39, 94, 9, 55, 129, 39, 7, 55, 185, 14, 192);
        public static final Layout WASHING_MACHINE = new Layout(34, 104, 16, 73, 129, 30, 10, 73, 192, 20, 202);
    }
}
