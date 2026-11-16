package com.mrcrayfish.furniture.network;

import com.mrcrayfish.furniture.network.message.C2SMessageLockCrate;
import com.mrcrayfish.furniture.network.message.C2SMessageOpenMailBox;
import com.mrcrayfish.furniture.network.message.C2SMessageSendMail;
import com.mrcrayfish.furniture.network.message.C2SMessageSetDoorMat;
import com.mrcrayfish.furniture.network.message.C2SMessageSetMailBoxName;
import com.mrcrayfish.furniture.network.message.S2CMessageFlipGrill;
import com.mrcrayfish.furniture.network.message.S2CMessageOpenDoorMat;
import com.mrcrayfish.furniture.network.play.ServerPlayHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.BiConsumer;

/**
 * Registers the furniture payloads. The client's handlers are registered on the client only (ClientHandler), so the server never
 * touches client code.
 *
 * Author: MrCrayfish
 */
public class PacketHandler
{
    public static final String PROTOCOL_VERSION = "2";

    public static void register(RegisterPayloadHandlersEvent event)
    {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToServer(C2SMessageLockCrate.TYPE, C2SMessageLockCrate.STREAM_CODEC, server(ServerPlayHandler::handleLockCrateMessage));
        registrar.playToServer(C2SMessageSendMail.TYPE, C2SMessageSendMail.STREAM_CODEC, server(ServerPlayHandler::handleSendMailMessage));
        registrar.playToServer(C2SMessageSetMailBoxName.TYPE, C2SMessageSetMailBoxName.STREAM_CODEC, server(ServerPlayHandler::handleSetMailBoxNameMessage));
        registrar.playToServer(C2SMessageOpenMailBox.TYPE, C2SMessageOpenMailBox.STREAM_CODEC, server(ServerPlayHandler::handleOpenMailBoxMessage));
        registrar.playToServer(C2SMessageSetDoorMat.TYPE, C2SMessageSetDoorMat.STREAM_CODEC, server(ServerPlayHandler::handleSetDoorMatMessage));
        registrar.playToClient(S2CMessageFlipGrill.TYPE, S2CMessageFlipGrill.STREAM_CODEC);
        registrar.playToClient(S2CMessageOpenDoorMat.TYPE, S2CMessageOpenDoorMat.STREAM_CODEC);
    }

    // Handlers run on the server thread; the sender is always a server player there
    private static <T extends CustomPacketPayload> IPayloadHandler<T> server(BiConsumer<ServerPlayer, T> handler)
    {
        return (payload, context) -> {
            if(context.player() instanceof ServerPlayer player)
            {
                handler.accept(player, payload);
            }
        };
    }
}
