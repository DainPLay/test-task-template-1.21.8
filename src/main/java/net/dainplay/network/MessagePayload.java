package net.dainplay.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.dainplay.TestTask;
import org.jetbrains.annotations.NotNull;

public record MessagePayload(byte[] messageData) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessagePayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TestTask.MOD_ID, "message"));

    public static final StreamCodec<FriendlyByteBuf, MessagePayload> STREAM_CODEC =
            StreamCodec.ofMember(MessagePayload::write, MessagePayload::new);

    public MessagePayload(FriendlyByteBuf buf) {
        this(buf.readByteArray());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByteArray(messageData);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}