package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.UUID;

/**
 * Sends the item in the open post box to a player's mail box.
 *
 * Author: MrCrayfish
 */
public record C2SMessageSendMail(UUID playerId, UUID mailBoxId) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SMessageSendMail> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "send_mail"));
    public static final StreamCodec<ByteBuf, C2SMessageSendMail> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, C2SMessageSendMail::playerId,
            UUIDUtil.STREAM_CODEC, C2SMessageSendMail::mailBoxId,
            C2SMessageSendMail::new);

    public UUID getPlayerId()
    {
        return this.playerId;
    }

    public UUID getMailBoxId()
    {
        return this.mailBoxId;
    }

    @Override
    public Type<C2SMessageSendMail> type()
    {
        return TYPE;
    }
}
