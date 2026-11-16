package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Renames the player's own mail box.
 *
 * Author: MrCrayfish
 */
public record C2SMessageSetMailBoxName(String name, BlockPos pos) implements CustomPacketPayload
{
    public static final int MAX_LENGTH = 32;

    public static final CustomPacketPayload.Type<C2SMessageSetMailBoxName> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "set_mail_box_name"));
    public static final StreamCodec<ByteBuf, C2SMessageSetMailBoxName> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.stringUtf8(MAX_LENGTH), C2SMessageSetMailBoxName::name,
            BlockPos.STREAM_CODEC, C2SMessageSetMailBoxName::pos,
            C2SMessageSetMailBoxName::new);

    public String getName()
    {
        return this.name;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    @Override
    public Type<C2SMessageSetMailBoxName> type()
    {
        return TYPE;
    }
}
