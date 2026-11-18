package com.mrcrayfish.furniture.client.gui.screen.inventory;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.inventory.container.ComputerMenu;
import com.mrcrayfish.furniture.item.crafting.MineBayRecipe;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

/**
 * MineBay on the computer (1.12.2): the item for sale between the ones before and after it, its price, and Buy.
 *
 * Author: MrCrayfish
 */
public class ComputerScreen extends AbstractContainerScreen<ComputerMenu>
{
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/container/computer.png");

    public ComputerScreen(ComputerMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title, 176, 187);
        this.inventoryLabelY = 84;
    }

    @Override
    protected void init()
    {
        super.init();
        this.addRenderableWidget(Button.builder(Component.literal("<"), button -> this.press(ComputerMenu.BUTTON_PREVIOUS)).bounds(this.leftPos + 40, this.topPos + 13, 15, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal(">"), button -> this.press(ComputerMenu.BUTTON_NEXT)).bounds(this.leftPos + 122, this.topPos + 13, 15, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.cfm.buy"), button -> this.press(ComputerMenu.BUTTON_BUY)).bounds(this.leftPos + 40, this.topPos + 36, 29, 20).build());
    }

    private void press(int id)
    {
        if(this.minecraft.gameMode != null)
        {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, id);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        // The monitor reaches above the screen's top, as in 1.12.2
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos - 10, 0, 0, this.imageWidth, this.imageHeight + 21, 256, 256);

        List<RecipeHolder<MineBayRecipe>> offers = this.menu.getOffers();
        if(offers.isEmpty())
            return;

        int selected = Math.min(this.menu.getSelected(), offers.size() - 1);
        if(selected > 0)
        {
            this.item(graphics, offers.get(selected - 1).value().result().create(), 57, 16);
        }
        MineBayRecipe offer = offers.get(selected).value();
        ItemStack stock = offer.result().create();
        this.item(graphics, stock, 80, 16);
        if(selected + 1 < offers.size())
        {
            this.item(graphics, offers.get(selected + 1).value().result().create(), 103, 16);
        }

        List<ItemStack> currency = offer.price().ingredient().items().map(ItemStack::new).toList();
        if(!currency.isEmpty())
        {
            this.item(graphics, currency.get((int) (System.currentTimeMillis() / 1000 % currency.size())), 73, 40);
        }
        graphics.text(this.font, "x" + offer.price().count(), this.leftPos + 90, this.topPos + 44, 0xFF000000, false);

        if(this.isHovering(80, 16, 16, 16, mouseX, mouseY))
        {
            graphics.setTooltipForNextFrame(this.font, stock, mouseX, mouseY);
        }
    }

    private void item(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y)
    {
        graphics.item(stack, this.leftPos + x, this.topPos + y);
        graphics.itemDecorations(this.font, stack, this.leftPos + x, this.topPos + y);
    }

    // The monitor has no room for the name
    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        graphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
    }
}
