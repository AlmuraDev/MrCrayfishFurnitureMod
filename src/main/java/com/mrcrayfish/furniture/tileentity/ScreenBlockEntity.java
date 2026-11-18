package com.mrcrayfish.furniture.tileentity;

import com.mojang.serialization.Codec;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Something that shows a picture from the web: the TV (up to three channels, which can be switched off) and the photo frame (one
 * picture). The server only keeps the addresses; each client downloads the picture itself, and only from the domains its own config
 * trusts.
 *
 * Author: MrCrayfish
 */
public abstract class ScreenBlockEntity extends BlockEntity
{
    public static final int MAX_URL_LENGTH = 256;

    protected List<String> urls = new ArrayList<>();
    protected boolean stretch;

    protected ScreenBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    /**
     * How many addresses it keeps.
     */
    public abstract int getMaxUrls();

    /**
     * The address of the picture it shows now, or null for none.
     */
    @Nullable
    public abstract String getCurrentUrl();

    public List<String> getUrls()
    {
        return this.urls;
    }

    public boolean isStretched()
    {
        return this.stretch;
    }

    /**
     * Applies what a player set in its settings screen; addresses that are empty or too long are dropped.
     */
    public void applySettings(List<String> urls, boolean stretch, boolean powered)
    {
        this.urls = new ArrayList<>(urls.stream().map(String::trim).filter(url -> !url.isEmpty() && url.length() <= MAX_URL_LENGTH).limit(this.getMaxUrls()).toList());
        this.stretch = stretch;
        BlockEntityUtil.sendUpdatePacket(this);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.urls = new ArrayList<>(input.read("Urls", Codec.STRING.listOf()).orElse(List.of()));
        this.stretch = input.getBooleanOr("Stretch", false);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store("Urls", Codec.STRING.listOf(), this.urls);
        output.putBoolean("Stretch", this.stretch);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
