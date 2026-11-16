package com.mrcrayfish.furniture.client.gui.widget.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.Nullable;

/**
 * Author: MrCrayfish
 */
public class IconButton extends Button
{
    private Identifier iconResource;
    private int iconU;
    private int iconV;
    private @Nullable Tooltip tooltip;

    public IconButton(int x, int y, OnPress onPress, Identifier iconResource, int iconU, int iconV)
    {
        super(x, y, 20, 20, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        this.iconResource = iconResource;
        this.iconU = iconU;
        this.iconV = iconV;
    }

    public void setIcon(Identifier iconResource, int iconU, int iconV)
    {
        this.iconResource = iconResource;
        this.iconU = iconU;
        this.iconV = iconV;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks)
    {
        this.extractDefaultSprite(graphics);
        int color = this.active ? ARGB.white(this.alpha) : ARGB.colorFromFloat(this.alpha, 0.5F, 0.5F, 0.5F);
        graphics.blit(RenderPipelines.GUI_TEXTURED, this.iconResource, this.getX() + 2, this.getY() + 2, this.iconU, this.iconV, 16, 16, 256, 256, color);
    }

    @Override
    public void setTooltip(@Nullable Tooltip tooltip)
    {
        super.setTooltip(tooltip);
        this.tooltip = tooltip;
    }

    /**
     * Shows the tooltip at the mouse rather than above the button
     */
    @Override
    protected void extractTooltipForNextRenderPass(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        if(this.tooltip != null && this.isHovered())
        {
            graphics.setTooltipForNextFrame(Minecraft.getInstance().font, this.tooltip.toCharSequence(Minecraft.getInstance()), DefaultTooltipPositioner.INSTANCE, mouseX, mouseY, false);
            return;
        }
        super.extractTooltipForNextRenderPass(graphics, mouseX, mouseY);
    }
}
