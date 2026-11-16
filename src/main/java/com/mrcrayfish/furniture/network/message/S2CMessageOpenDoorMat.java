package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Opens the door mat writing screen for the player who just placed the mat (like a sign's editor).
 */
public record S2CMessageOpenDoorMat(BlockPos pos) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CMessageOpenDoorMat> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "open_door_mat"));
    public static final StreamCodec<ByteBuf, S2CMessageOpenDoorMat> STREAM_CODEC = BlockPos.STREAM_CODEC.map(S2CMessageOpenDoorMat::new, S2CMessageOpenDoorMat::pos);

    @Override
    public Type<S2CMessageOpenDoorMat> type()
    {
        return TYPE;
    }
}
