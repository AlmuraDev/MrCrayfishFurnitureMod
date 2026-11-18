package com.mrcrayfish.furniture.client.gui.widget.button;

import com.mrcrayfish.furniture.client.event.CreativeScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

/**
 * Author: MrCrayfish
 */
public class TagButton extends Button
{
    private static final Identifier TAB_SELECTED = Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_2");
    private static final Identifier TAB_UNSELECTED = Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_2");
    private static final int TAB_WIDTH = 26;
    private static final int TAB_HEIGHT = 32;

    private final CreativeScreenEvents.TagFilter category;
    private final ItemStack stack;
    private boolean toggled;
    private @Nullable Tooltip tooltip;

    public TagButton(int x, int y, CreativeScreenEvents.TagFilter category, OnPress onPress)
    {
        super(x, y, 32, 26, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        this.category = category;
        this.stack = category.getIcon();
        this.toggled = category.isEnabled();
        this.setTooltip(Tooltip.create(category.getName()));
    }

    public CreativeScreenEvents.TagFilter getCategory()
    {
        return this.category;
    }

    @Override
    public void onPress(InputWithModifiers input)
    {
        this.toggled = !this.toggled;
        this.category.setEnabled(this.toggled);
        super.onPress(input);
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks)
    {
        int width = this.toggled ? 32 : 28;
        this.drawRotatedTab(graphics, this.toggled ? TAB_SELECTED : TAB_UNSELECTED, this.getX(), this.getY(), width, 26);
        graphics.item(this.stack, this.getX() + 8, this.getY() + 5);
    }

    /**
     * Draws a creative tab on its side with its open end facing right: the sprite's horizontal axis runs down the
     * screen and its vertical axis runs across it, like the quad MrCrayfish drew in 1.20.
     */
    private void drawRotatedTab(GuiGraphicsExtractor graphics, Identifier spriteId, int x, int y, int width, int height)
    {
        // GUI sprites live in their own atlas, which the sprite lookup on GuiGraphicsExtractor refuses
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.GUI).getSprite(spriteId);
        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        // A quarter turn (screen x = local y, screen y = -local x) with the u range reversed below gives the
        // transposed sprite while keeping the quad's winding, which the culled GUI pipeline needs
        graphics.pose().mul(new Matrix3x2f(0.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        float u0 = sprite.getU((float) height / TAB_WIDTH);
        float u1 = sprite.getU(0.0F);
        float v0 = sprite.getV(0.0F);
        float v1 = sprite.getV((float) width / TAB_HEIGHT);
        graphics.blit(sprite.atlasLocation(), -height, 0, 0, width, u0, u1, v0, v1);
        graphics.pose().popMatrix();
    }

    public void updateState()
    {
        this.toggled = this.category.isEnabled();
    }

    @Override
    public void setTooltip(@Nullable Tooltip tooltip)
    {
        super.setTooltip(tooltip);
        this.tooltip = tooltip;
    }

    /**
     * Shows the tooltip at the mouse rather than beside the button
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
