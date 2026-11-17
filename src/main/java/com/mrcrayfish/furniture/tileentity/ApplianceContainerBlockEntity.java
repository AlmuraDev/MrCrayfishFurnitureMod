package com.mrcrayfish.furniture.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * An appliance with an inventory and a screen. Its items and whether it runs are sent to the client whenever they change, which draws
 * them (the food in the oven, the armour in the washing machine).
 *
 * Author: MrCrayfish
 */
public abstract class ApplianceContainerBlockEntity extends BasicLootBlockEntity implements Appliance
{
    protected ApplianceContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    /**
     * What is in it, for its renderer.
     */
    public List<ItemStack> getContents()
    {
        return this.items;
    }

    @Override
    public void setChanged()
    {
        super.setChanged();
        if(this.level != null && !this.level.isClientSide())
        {
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_CLIENTS);
        }
    }

    /**
     * Saves and sends it after it starts or stops, and lets comparators next to it know.
     */
    protected void onRunningChanged()
    {
        this.setChanged();
        if(this.level != null)
        {
            this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
        }
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
