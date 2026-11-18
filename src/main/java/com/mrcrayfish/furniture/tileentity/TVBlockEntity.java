package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * A TV's three channels (GIFs), which one is on, and whether it's on at all (1.12.2).
 *
 * Author: MrCrayfish
 */
public class TVBlockEntity extends ScreenBlockEntity
{
    private int channel;
    private boolean powered;

    public TVBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.TV.get(), pos, state);
    }

    @Override
    public int getMaxUrls()
    {
        return 3;
    }

    @Nullable
    @Override
    public String getCurrentUrl()
    {
        if(!this.powered || this.urls.isEmpty())
            return null;
        return this.urls.get(Math.floorMod(this.channel, this.urls.size()));
    }

    public boolean isPowered()
    {
        return this.powered;
    }

    public void setPowered(boolean powered)
    {
        this.powered = powered;
        BlockEntityUtil.sendUpdatePacket(this);
    }

    /**
     * @return whether it changed channel (it needs to be on, with more than one channel)
     */
    public boolean nextChannel()
    {
        if(!this.powered || this.urls.size() < 2)
            return false;

        this.channel = (this.channel + 1) % this.urls.size();
        BlockEntityUtil.sendUpdatePacket(this);
        return true;
    }

    @Override
    public void applySettings(List<String> urls, boolean stretch, boolean powered)
    {
        this.powered = powered;
        super.applySettings(urls, stretch, powered);
        if(this.channel >= this.urls.size())
        {
            this.channel = 0;
        }
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.channel = input.getIntOr("Channel", 0);
        this.powered = input.getBooleanOr("Powered", false);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putInt("Channel", this.channel);
        output.putBoolean("Powered", this.powered);
    }
}
