package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * One item lying on a block, turned the way it was put down: the food on a plate or chopping board (1.12.2). The client draws it.
 *
 * Author: MrCrayfish
 */
public abstract class DisplayItemBlockEntity extends BlockEntity
{
    private ItemStack item = ItemStack.EMPTY;
    private int rotation;

    protected DisplayItemBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    /**
     * Quarter turns, from the way the player faced when putting it down.
     */
    public int getRotation()
    {
        return this.rotation;
    }

    public void setItem(ItemStack item, int rotation)
    {
        this.item = item;
        this.rotation = rotation;
        BlockEntityUtil.sendUpdatePacket(this);
        if(this.level != null)
        {
            this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
        }
    }

    /**
     * Drops the item on top of the block.
     */
    public void takeItem()
    {
        if(this.item.isEmpty() || this.level == null)
            return;
        Containers.dropItemStack(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.4, this.worldPosition.getZ() + 0.5, this.item);
        this.setItem(ItemStack.EMPTY, this.rotation);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        if(this.level != null && !this.item.isEmpty())
        {
            Containers.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), this.item);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.item = input.read("Item", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        this.rotation = input.getIntOr("Rotation", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store("Item", ItemStack.OPTIONAL_CODEC, this.item);
        output.putInt("Rotation", this.rotation);
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
