package net.dainplay.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class MessagePacket {
    public static void sendToServer(String text) {
        byte[] messageData = MessageUtils.serializeMessage(text);
        MessagePayload payload = new MessagePayload(messageData);
        ClientPlayNetworking.send(payload);
    }
}