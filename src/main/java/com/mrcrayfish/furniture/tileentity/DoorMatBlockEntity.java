package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
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

/**
 * Author: MrCrayfish
 */
public class DoorMatBlockEntity extends BlockEntity
{
    @Nullable
    private String message = null;

    protected DoorMatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public DoorMatBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.DOOR_MAT.get(), pos, state);
    }

    /**
     * Sets the message once: a door mat is written on just after it is placed and can't be changed after.
     */
    public void setMessage(String message)
    {
        if(this.message == null)
        {
            this.message = message;
            this.setChanged();
            BlockEntityUtil.sendUpdatePacket(this);
        }
    }

    public boolean hasMessage()
    {
        return this.message != null;
    }

    public String getMessage()
    {
        return this.message != null ? this.message : "";
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        input.getString("Message").ifPresent(message -> this.message = message);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if(this.message != null)
        {
            output.putString("Message", this.message);
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        return this.saveWithoutMetadata(registries);
    }
}
