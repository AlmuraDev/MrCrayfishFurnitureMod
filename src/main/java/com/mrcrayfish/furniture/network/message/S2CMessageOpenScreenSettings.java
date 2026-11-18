package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Opens the settings screen of the TV or photo frame the player used.
 */
public record S2CMessageOpenScreenSettings(BlockPos pos) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CMessageOpenScreenSettings> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "open_screen_settings"));
    public static final StreamCodec<ByteBuf, S2CMessageOpenScreenSettings> STREAM_CODEC = BlockPos.STREAM_CODEC.map(S2CMessageOpenScreenSettings::new, S2CMessageOpenScreenSettings::pos);

    @Override
    public Type<S2CMessageOpenScreenSettings> type()
    {
        return TYPE;
    }
}
