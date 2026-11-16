package com.mrcrayfish.furniture.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * GUI helpers for the furniture screens (client only).
 *
 * Author: MrCrayfish
 */
public class RenderUtil
{
    /**
     * Clips what the screen draws next to the area, in GUI coordinates. End it with {@link #endScissor(GuiGraphicsExtractor)}.
     */
    public static void scissor(GuiGraphicsExtractor graphics, int x, int y, int width, int height)
    {
        graphics.enableScissor(x, y, x + width, y + height);
    }

    public static void endScissor(GuiGraphicsExtractor graphics)
    {
        graphics.disableScissor();
    }

    public static boolean isMouseInArea(int mouseX, int mouseY, int x, int y, int width, int height)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
