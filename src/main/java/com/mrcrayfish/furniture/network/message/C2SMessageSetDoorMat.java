package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Sets the message written on a door mat the player has just placed.
 *
 * Author: MrCrayfish
 */
public record C2SMessageSetDoorMat(BlockPos pos, String message) implements CustomPacketPayload
{
    public static final int MAX_LENGTH = 64;

    public static final CustomPacketPayload.Type<C2SMessageSetDoorMat> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "set_door_mat"));
    public static final StreamCodec<ByteBuf, C2SMessageSetDoorMat> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, C2SMessageSetDoorMat::pos,
            ByteBufCodecs.stringUtf8(MAX_LENGTH), C2SMessageSetDoorMat::message,
            C2SMessageSetDoorMat::new);

    public BlockPos getPos()
    {
        return this.pos;
    }

    public String getMessage()
    {
        return this.message;
    }

    @Override
    public Type<C2SMessageSetDoorMat> type()
    {
        return TYPE;
    }
}
