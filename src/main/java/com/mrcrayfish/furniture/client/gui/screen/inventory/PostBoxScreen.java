package com.mrcrayfish.furniture.client.gui.screen.inventory;

import com.mojang.blaze3d.platform.InputConstants;
import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.client.gui.widget.button.IconButton;
import com.mrcrayfish.furniture.common.mail.MailBoxEntry;
import com.mrcrayfish.furniture.inventory.container.PostBoxMenu;
import com.mrcrayfish.furniture.network.message.C2SMessageSendMail;
import com.mrcrayfish.furniture.client.RenderUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: MrCrayfish
 */
public class PostBoxScreen extends AbstractContainerScreen<PostBoxMenu>
{
    private static final Identifier GUI_TEXTURE = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/container/post_box.png");
    private static final Identifier ICONS_TEXTURE = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/icons.png");
    private static final int LIST_WIDTH = 116;
    private static final int LIST_HEIGHT = 57;
    private static final int SCROLL_BAR_WIDTH = 12;
    private static final int SCROLL_BAR_HEIGHT = 15;
    private static final int ITEM_WIDTH = 116;
    private static final int ITEM_HEIGHT = 24;
    private static final int MAX_VISIBLE_ITEMS = (int) Math.ceil((double) LIST_HEIGHT / (double) ITEM_HEIGHT) + 1;

    private EditBox searchField;
    private int scroll;
    private int pressedMouseY = -1;
    private MailBoxEntry selected;
    private Button btnSend;

    private final List<MailBoxEntry> mailBoxList;
    private List<MailBoxEntry> filteredMailBoxList = new ArrayList<>();

    public PostBoxScreen(PostBoxMenu container, Inventory playerInventory, Component title)
    {
        super(container, playerInventory, title, 176, 187);
        this.mailBoxList = container.getMailBoxes();
        this.filteredMailBoxList.addAll(this.mailBoxList);
        this.inventoryLabelY = 94;
    }

    @Override
    protected void init()
    {
        super.init();
        this.searchField = new EditBox(this.font, this.leftPos + 22, this.topPos + 19, 101, 9, Component.translatable("gui.cfm.post_box.search"));
        this.searchField.setBordered(false);
        this.searchField.setMaxLength(32);
        this.searchField.setTextColor(0xFFFFFFFF);
        this.addWidget(this.searchField);
        this.btnSend = this.addRenderableWidget(new IconButton(this.leftPos + 147, this.topPos + 53, this::sendMail, ICONS_TEXTURE, 32, 0));
        this.btnSend.setTooltip(Tooltip.create(Component.translatable("gui.button.cfm.send_mail")));
        this.btnSend.active = false;
    }

    private void sendMail(Button button)
    {
        if(this.selected != null && !this.menu.getMail().isEmpty())
        {
            ClientPacketDistributor.sendToServer(new C2SMessageSendMail(this.selected.getOwnerId(), this.selected.getMailBoxId()));
        }
    }

    @Override
    public void containerTick()
    {
        this.btnSend.active = this.selected != null && !this.menu.getMail().isEmpty();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, startX, startY, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        if(this.menu.getMail().isEmpty())
        {
            graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, startX + 149, startY + 33, 116, 202, 16, 16, 256, 256);
        }

        this.searchField.extractRenderState(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        super.extractLabels(graphics, mouseX, mouseY);

        int scrollBarY = this.getScrollBarY(mouseY);
        int scrollBarUOffset = this.getMaxScroll() <= 0 ? SCROLL_BAR_WIDTH : 0;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, 128, 32 + scrollBarY, 116 + scrollBarUOffset, 187, SCROLL_BAR_WIDTH, SCROLL_BAR_HEIGHT, 256, 256);

        // The pose is already translated to the screen's top left, and the scissor box follows it
        RenderUtil.scissor(graphics, 8, 32, LIST_WIDTH, LIST_HEIGHT);
        {
            int scroll = this.scroll;
            if(this.pressedMouseY != -1)
            {
                scroll = (int) (this.getMaxScroll() * (scrollBarY / (double) (LIST_HEIGHT - SCROLL_BAR_HEIGHT)) + 0.5);
            }
            int startIndex = scroll / ITEM_HEIGHT;
            for(int i = startIndex; i < Math.min(startIndex + MAX_VISIBLE_ITEMS, this.filteredMailBoxList.size()); i++)
            {
                Matrix3x2fStack poseStack = graphics.pose();
                poseStack.pushMatrix();
                poseStack.translate(8, 32);
                poseStack.translate(0, -scroll);
                poseStack.translate(0, i * ITEM_HEIGHT);

                MailBoxEntry entry = this.filteredMailBoxList.get(i);
                boolean isSelected = entry == selected;

                graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, 0, 0, 0, 211 - (isSelected ? ITEM_HEIGHT : 0), ITEM_WIDTH, ITEM_HEIGHT, 256, 256);

                if(isSelected)
                {
                    graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, ITEM_WIDTH - 20, 5, 140, 187, 14, 12, 256, 256);
                    graphics.text(this.font, ChatFormatting.BOLD + entry.getName(), 3, 3, 0xFFFFFF55);
                    graphics.text(this.font, entry.getOwnerName(), 3, 13, 0xFFFFFFFF);
                }
                else
                {
                    graphics.text(this.font, entry.getName(), 3, 3, 0xFFFFFFFF);
                    graphics.text(this.font, entry.getOwnerName(), 3, 13, 0xFF777777);
                }

                poseStack.popMatrix();
            }
        }
        RenderUtil.endScissor(graphics);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
    {
        double mouseX = event.x();
        double mouseY = event.y();
        if(RenderUtil.isMouseInArea((int) mouseX, (int) mouseY, this.leftPos + 8, this.topPos + 32, 116, 57))
        {
            int clickedIndex = (int) ((mouseY - this.topPos - 32 + scroll) / ITEM_HEIGHT);
            if(clickedIndex >= 0 && clickedIndex < this.filteredMailBoxList.size())
            {
                MailBoxEntry entry = this.filteredMailBoxList.get(clickedIndex);
                this.selected = this.selected == entry ? null : entry;
                return true;
            }
        }
        int scrollBarY = (int) ((LIST_HEIGHT - SCROLL_BAR_HEIGHT) * (scroll / (double) this.getMaxScroll()));
        if(this.getMaxScroll() > 0 && RenderUtil.isMouseInArea((int) mouseX, (int) mouseY, this.leftPos + 128, this.topPos + 32 + scrollBarY, SCROLL_BAR_WIDTH, SCROLL_BAR_HEIGHT))
        {
            this.pressedMouseY = (int) mouseY;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event)
    {
        if(this.pressedMouseY != -1 && event.button() == InputConstants.MOUSE_BUTTON_LEFT)
        {
            this.scroll = (int) (this.getMaxScroll() * (this.getScrollBarY((int) event.y()) / (double) (LIST_HEIGHT - SCROLL_BAR_HEIGHT)) + 0.5);
            this.pressedMouseY = -1;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        if(RenderUtil.isMouseInArea((int) mouseX, (int) mouseY, this.leftPos + 8, this.topPos + 32, 116, 57))
        {
            this.scroll = (int) Math.max(0, Math.min(this.getMaxScroll(), this.scroll - (scrollY * 10)));
            return true;
        }
        return false;
    }

    @Override
    public boolean charTyped(CharacterEvent event)
    {
        String s = this.searchField.getValue();
        if(this.searchField.charTyped(event))
        {
            if(!Objects.equals(s, this.searchField.getValue()))
            {
                this.updateMailBoxList();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event)
    {
        String s = this.searchField.getValue();
        if(this.searchField.keyPressed(event))
        {
            if(!Objects.equals(s, this.searchField.getValue()))
            {
                this.updateMailBoxList();
            }
            return true;
        }
        return this.searchField.isFocused() && this.searchField.isVisible() && !event.isEscape() || super.keyPressed(event);
    }

    private void updateMailBoxList()
    {
        if(this.searchField.getValue().isEmpty())
        {
            this.filteredMailBoxList = this.mailBoxList;
        }
        else
        {
            Stream<MailBoxEntry> stream = this.mailBoxList.stream().filter(entry ->
            {
                String searchText = this.searchField.getValue().toLowerCase(Locale.ENGLISH).trim();
                if(entry.getName().toLowerCase().contains(searchText))
                {
                    return true;
                }
                return entry.getOwnerName().toLowerCase().contains(searchText);
            });
            this.filteredMailBoxList = stream.collect(Collectors.toList());
        }
    }

    private int getScrollBarY(int mouseY)
    {
        int scrollOffset = 0;
        if(this.pressedMouseY != -1)
        {
            scrollOffset = (mouseY - pressedMouseY);
        }
        int scrollBarY = (int) ((LIST_HEIGHT - SCROLL_BAR_HEIGHT) * (scroll / (double) this.getMaxScroll()));
        return Mth.clamp(scrollBarY + scrollOffset, 0, LIST_HEIGHT - SCROLL_BAR_HEIGHT);
    }

    private int getMaxScroll()
    {
        return Math.max(0, ITEM_HEIGHT * this.filteredMailBoxList.size() - LIST_HEIGHT);
    }
}
