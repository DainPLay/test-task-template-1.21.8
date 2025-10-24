package net.dainplay.client;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.dainplay.network.MessagePayload;
import net.dainplay.network.MessageUtils;

@Environment(EnvType.CLIENT)
public class MessagePacket {
    public static void sendToServer(String text) {
        byte[] messageData = MessageUtils.serializeMessage(text);
        MessagePayload payload = new MessagePayload(messageData);
        ClientPlayNetworking.send(payload);
    }
}