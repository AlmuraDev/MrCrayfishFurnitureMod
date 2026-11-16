package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Opens a mail box's inventory again (from its settings screen).
 *
 * Author: MrCrayfish
 */
public record C2SMessageOpenMailBox(BlockPos pos) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SMessageOpenMailBox> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "open_mail_box"));
    public static final StreamCodec<ByteBuf, C2SMessageOpenMailBox> STREAM_CODEC = BlockPos.STREAM_CODEC.map(C2SMessageOpenMailBox::new, C2SMessageOpenMailBox::pos);

    public BlockPos getPos()
    {
        return this.pos;
    }

    @Override
    public Type<C2SMessageOpenMailBox> type()
    {
        return TYPE;
    }
}
