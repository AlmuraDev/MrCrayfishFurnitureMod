package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * The colour of a digital clock's numbers (1.12.2), white until dyed.
 *
 * Author: MrCrayfish
 */
public class DigitalClockBlockEntity extends BlockEntity
{
    private DyeColor textColour = DyeColor.WHITE;

    public DigitalClockBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.DIGITAL_CLOCK.get(), pos, state);
    }

    public DyeColor getTextColour()
    {
        return this.textColour;
    }

    public void setTextColour(DyeColor colour)
    {
        this.textColour = colour;
        BlockEntityUtil.sendUpdatePacket(this);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.textColour = input.read("TextColour", DyeColor.CODEC).orElse(DyeColor.WHITE);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store("TextColour", DyeColor.CODEC, this.textColour);
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
