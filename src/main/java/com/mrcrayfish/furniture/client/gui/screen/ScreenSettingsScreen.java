package com.mrcrayfish.furniture.client.gui.screen;

import com.mrcrayfish.furniture.network.message.C2SMessageSetScreenSettings;
import com.mrcrayfish.furniture.tileentity.ScreenBlockEntity;
import com.mrcrayfish.furniture.tileentity.TVBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.List;

/**
 * The settings of a TV (three channel addresses, stretch, on/off) or photo frame (the picture's address, stretch), as 1.12.2's value
 * screen had them. The gap above the Save and Cancel row matches the gap below it.
 *
 * Author: MrCrayfish
 */
public class ScreenSettingsScreen extends Screen
{
    private static final int WIDTH = 240;
    private static final int PADDING = 8;
    private static final int ROW = 20;
    private static final int SPACING = 4;
    private static final int PANEL = 0xE0101010;
    private static final int BORDER = 0xFF606060;

    private final ScreenBlockEntity screen;
    private final boolean tv;
    private final List<EditBox> urlFields = new ArrayList<>();
    private boolean stretch;
    private boolean powered;
    private int left;
    private int top;

    public ScreenSettingsScreen(ScreenBlockEntity screen)
    {
        super(Component.translatable(screen instanceof TVBlockEntity ? "gui.cfm.tv_settings" : "gui.cfm.photo_frame_settings"));
        this.screen = screen;
        this.tv = screen instanceof TVBlockEntity;
        this.stretch = screen.isStretched();
        this.powered = screen instanceof TVBlockEntity tvScreen && tvScreen.isPowered();
    }

    @Override
    protected void init()
    {
        int fields = this.screen.getMaxUrls();
        this.left = (this.width - WIDTH) / 2;
        this.top = (this.height - this.panelHeight()) / 2;
        int inner = WIDTH - PADDING * 2;
        int x = this.left + PADDING;
        int y = this.top + PADDING + this.font.lineHeight + SPACING;

        List<String> urls = this.screen.getUrls();
        this.urlFields.clear();
        for(int i = 0; i < fields; i++)
        {
            EditBox field = new EditBox(this.font, x, y, inner, ROW, CommonComponents.EMPTY);
            field.setMaxLength(ScreenBlockEntity.MAX_URL_LENGTH);
            field.setHint(Component.translatable(this.tv ? "gui.cfm.channel" : "gui.cfm.photo_url", i + 1));
            field.setValue(i < urls.size() ? urls.get(i) : "");
            this.urlFields.add(this.addRenderableWidget(field));
            y += ROW + SPACING;
        }

        int half = (inner - SPACING) / 2;
        this.addRenderableWidget(CycleButton.onOffBuilder(this.stretch).create(x, y, this.tv ? half : inner, ROW,
                Component.translatable("gui.cfm.stretch"), (button, value) -> this.stretch = value));
        if(this.tv)
        {
            this.addRenderableWidget(CycleButton.onOffBuilder(this.powered).create(x + half + SPACING, y, half, ROW,
                    Component.translatable("gui.cfm.powered"), (button, value) -> this.powered = value));
        }
        y += ROW + PADDING;

        this.addRenderableWidget(Button.builder(Component.translatable("gui.button.cfm.save"), button -> this.save()).bounds(x, y, half, ROW).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.button.cfm.cancel"), button -> this.onClose()).bounds(x + half + SPACING, y, half, ROW).build());
        this.setInitialFocus(this.urlFields.getFirst());
    }

    private void save()
    {
        List<String> urls = this.urlFields.stream().map(EditBox::getValue).toList();
        ClientPacketDistributor.sendToServer(new C2SMessageSetScreenSettings(this.screen.getBlockPos(), urls, this.stretch, this.powered));
        this.onClose();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        graphics.fill(this.left - 1, this.top - 1, this.left + WIDTH + 1, this.top + this.panelHeight() + 1, BORDER);
        graphics.fill(this.left, this.top, this.left + WIDTH, this.top + this.panelHeight(), PANEL);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        graphics.text(this.font, this.title, this.left + PADDING, this.top + PADDING, 0xFFFFFFFF);
    }

    private int panelHeight()
    {
        int fields = this.screen.getMaxUrls();
        return PADDING + this.font.lineHeight + SPACING + fields * (ROW + SPACING) + ROW + PADDING + ROW + PADDING;
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
