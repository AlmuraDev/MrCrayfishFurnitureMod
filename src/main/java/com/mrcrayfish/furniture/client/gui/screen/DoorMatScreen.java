package com.mrcrayfish.furniture.client.gui.screen;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.network.message.C2SMessageSetDoorMat;
import com.mrcrayfish.furniture.tileentity.DoorMatBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

/**
 * Author: MrCrayfish
 */
public class DoorMatScreen extends Screen
{
    private static final Identifier GUI_TEXTURE = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/mail_box_settings.png");

    private final int xSize = 176;
    private final int ySize = 69;

    private final DoorMatBlockEntity doorMatBlockEntity;
    private EditBox nameField;
    private Button btnSave;

    public DoorMatScreen(DoorMatBlockEntity doorMatBlockEntity)
    {
        super(Component.translatable("gui.cfm.door_mat_message"));
        this.doorMatBlockEntity = doorMatBlockEntity;
    }

    @Override
    protected void init()
    {
        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        this.nameField = new EditBox(this.font, guiLeft + 8, guiTop + 18, 160, 18, CommonComponents.EMPTY)
        {
            @Override
            public void insertText(String textToWrite)
            {
                int lines = DoorMatScreen.this.font.split(FormattedText.of(this.getValue() + textToWrite), 60).size();
                if(lines <= 2)
                {
                    super.insertText(textToWrite);
                }
            }
        };
        if(this.doorMatBlockEntity.getMessage() != null)
        {
            this.nameField.setValue(this.doorMatBlockEntity.getMessage());
        }
        this.addWidget(this.nameField);

        this.btnSave = this.addRenderableWidget(Button.builder(Component.translatable("gui.button.cfm.save"), button -> {
            if(this.isValidName()){
                ClientPacketDistributor.sendToServer(new C2SMessageSetDoorMat(this.doorMatBlockEntity.getBlockPos(), this.nameField.getValue()));
                this.minecraft.player.closeContainer();
            }
        }).pos(guiLeft + 7, guiTop + 42).size(79, 20).build());
        this.btnSave.active = false;

        this.addRenderableWidget(Button.builder(Component.translatable("gui.button.cfm.cancel"), button -> this.onClose()).pos(guiLeft + 91, guiTop + 42).size(79, 20).build());
    }

    @Override
    public void tick()
    {
        super.tick();
        this.btnSave.active = this.isValidName();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, startX, startY, 0, 0, this.xSize, this.ySize, 256, 256);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        graphics.text(this.font, this.title, startX + 8, startY + 6, 0xFF404040);
        this.nameField.extractRenderState(graphics, mouseX, mouseY, partialTicks);
    }

    private boolean isValidName()
    {
        int lines = this.font.split(Component.nullToEmpty(this.doorMatBlockEntity.getMessage()), 45).size();
        return !this.nameField.getValue().equals(this.doorMatBlockEntity.getMessage()) && !this.nameField.getValue().trim().isEmpty() && lines < 2;
    }
}
