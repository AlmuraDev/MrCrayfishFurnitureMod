package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Locks or unlocks the crate the player has open.
 *
 * Author: MrCrayfish
 */
public record C2SMessageLockCrate() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SMessageLockCrate> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "lock_crate"));
    public static final StreamCodec<ByteBuf, C2SMessageLockCrate> STREAM_CODEC = StreamCodec.unit(new C2SMessageLockCrate());

    @Override
    public Type<C2SMessageLockCrate> type()
    {
        return TYPE;
    }
}
