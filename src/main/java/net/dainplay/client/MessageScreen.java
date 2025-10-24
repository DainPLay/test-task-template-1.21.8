package net.dainplay.client;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class MessageScreen extends Screen {
    private EditBox textField;
    private Button sendButton;
    private final Screen parent;

    public MessageScreen(Screen parent) {
        super(Component.literal("Send Message"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        this.textField = new EditBox(
            this.font,
            this.width / 2 - 150,
            this.height / 2 - 20,
            300,
            20,
            Component.literal("Enter message")
        );
        this.addRenderableWidget(this.textField);

        this.sendButton = Button.builder(
            Component.literal("Send"),
            button -> this.sendMessage()
        )
        .bounds(this.width / 2 - 50, this.height / 2 + 20, 100, 20)
        .build();
        
        this.addRenderableWidget(this.sendButton);
    }

    private void sendMessage() {
        String messageText = this.textField.getValue();
        if (!messageText.isEmpty()) {
            MessagePacket.sendToServer(messageText);
            this.onClose();
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredString(
            this.font,
            Component.literal("Send Message to Database"),
            this.width / 2,
            this.height / 2 - 50,
            0xFFFFFF
        );
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (this.minecraft.level != null) {
            this.renderTransparentBackground(context);
        } else {
            super.renderBackground(context, mouseX, mouseY, delta);
        }
    }
}