package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.tileentity.ScreenBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.List;

/**
 * Sets the addresses and options of a TV or photo frame from its settings screen.
 */
public record C2SMessageSetScreenSettings(BlockPos pos, List<String> urls, boolean stretch, boolean powered) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SMessageSetScreenSettings> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "set_screen_settings"));
    public static final StreamCodec<ByteBuf, C2SMessageSetScreenSettings> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, C2SMessageSetScreenSettings::pos,
            ByteBufCodecs.stringUtf8(ScreenBlockEntity.MAX_URL_LENGTH).apply(ByteBufCodecs.list(3)), C2SMessageSetScreenSettings::urls,
            ByteBufCodecs.BOOL, C2SMessageSetScreenSettings::stretch,
            ByteBufCodecs.BOOL, C2SMessageSetScreenSettings::powered,
            C2SMessageSetScreenSettings::new);

    @Override
    public Type<C2SMessageSetScreenSettings> type()
    {
        return TYPE;
    }
}
