package com.mrcrayfish.furniture.common.mail;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;
import java.util.UUID;

/**
 * A mail box as the post box screen lists it: the server sends the list when a post box opens.
 *
 * Author: MrCrayfish
 */
public record MailBoxEntry(UUID mailBoxId, String name, UUID ownerId, String ownerName)
{
    public static final StreamCodec<ByteBuf, MailBoxEntry> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, MailBoxEntry::mailBoxId,
            ByteBufCodecs.STRING_UTF8, MailBoxEntry::name,
            UUIDUtil.STREAM_CODEC, MailBoxEntry::ownerId,
            ByteBufCodecs.STRING_UTF8, MailBoxEntry::ownerName,
            MailBoxEntry::new);

    public static final StreamCodec<ByteBuf, List<MailBoxEntry>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    public UUID getMailBoxId()
    {
        return this.mailBoxId;
    }

    public String getName()
    {
        return this.name;
    }

    public UUID getOwnerId()
    {
        return this.ownerId;
    }

    public String getOwnerName()
    {
        return this.ownerName;
    }

    // Two entries are the same mail box when their ids match
    @Override
    public boolean equals(Object obj)
    {
        return this == obj || (obj instanceof MailBoxEntry other && this.mailBoxId.equals(other.mailBoxId));
    }

    @Override
    public int hashCode()
    {
        return this.mailBoxId.hashCode();
    }
}
