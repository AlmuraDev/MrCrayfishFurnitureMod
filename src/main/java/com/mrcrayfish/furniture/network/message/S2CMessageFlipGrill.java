package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Plays the flip animation of one item on a grill.
 *
 * Author: MrCrayfish
 */
public record S2CMessageFlipGrill(BlockPos pos, int position) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CMessageFlipGrill> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "flip_grill"));
    public static final StreamCodec<ByteBuf, S2CMessageFlipGrill> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, S2CMessageFlipGrill::pos,
            ByteBufCodecs.VAR_INT, S2CMessageFlipGrill::position,
            S2CMessageFlipGrill::new);

    public BlockPos getPos()
    {
        return this.pos;
    }

    public int getPosition()
    {
        return this.position;
    }

    @Override
    public Type<S2CMessageFlipGrill> type()
    {
        return TYPE;
    }
}
