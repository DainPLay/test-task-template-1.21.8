package net.dainplay.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class TestTaskClient implements ClientModInitializer {
    private static KeyMapping openScreenKey;
    private static boolean packetRegistered = false;

    @Override
    public void onInitializeClient() {
        registerPacketType();

        openScreenKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.testtask.open_screen",
                GLFW.GLFW_KEY_M,
                "category.testtask"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openScreenKey.consumeClick()) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().setScreen(new MessageScreen(null));
                }
            }
        });
    }

    private static void registerPacketType() {
        if (!packetRegistered) {
            try {
                PayloadTypeRegistry.playC2S().register(
                        net.dainplay.network.MessagePayload.TYPE,
                        net.dainplay.network.MessagePayload.STREAM_CODEC
                );
                packetRegistered = true;
            } catch (IllegalArgumentException e) {
                packetRegistered = true;
            }
        }
    }
}