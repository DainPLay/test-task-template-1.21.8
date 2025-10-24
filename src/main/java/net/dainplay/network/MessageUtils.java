package net.dainplay.network;

import net.dainplay.proto.MessageProto;
import java.io.IOException;

public class MessageUtils {
    public static byte[] serializeMessage(String text) {
        MessageProto.Message message = MessageProto.Message.newBuilder()
            .setText(text)
            .build();
        return message.toByteArray();
    }
    
    public static MessageProto.Message deserializeMessage(byte[] data) throws IOException {
        return MessageProto.Message.parseFrom(data);
    }
}