package net.dainplay;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTask implements ModInitializer {
    public static final String MOD_ID = "test-task";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ResourceLocation MESSAGE_PACKET_ID =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "message");

    private static boolean packetRegistered = false;

    @Override
    public void onInitialize() {
        try {
            registerPacketType();

            ServerPlayNetworking.registerGlobalReceiver(
                    net.dainplay.network.MessagePayload.TYPE,
                    (payload, context) -> {
                        context.server().execute(() -> {
                            try {
                                net.dainplay.proto.MessageProto.Message message =
                                        net.dainplay.network.MessageUtils.deserializeMessage(payload.messageData());
                                net.dainplay.database.DatabaseService.saveMessage(
                                        context.player().getUUID(), message.getText());
                            } catch (Exception e) {
                                LOGGER.error("Failed to process message", e);
                            }
                        });
                    });

            net.dainplay.database.DatabaseService.initialize();

            LOGGER.info("TestTask mod initialized successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to initialize mod", e);
            throw e;
        }
    }

    private static void registerPacketType() {
        if (!packetRegistered) {
            try {
                PayloadTypeRegistry.playC2S().register(
                        net.dainplay.network.MessagePayload.TYPE,
                        net.dainplay.network.MessagePayload.STREAM_CODEC
                );
                packetRegistered = true;
                LOGGER.info("Packet type registered successfully");
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Packet type already registered, continuing...");
                packetRegistered = true;
            }
        }
    }
}