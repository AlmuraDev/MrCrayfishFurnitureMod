package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * The drink in a cup that has been set down (empty for an empty cup). The client draws the drink in its colour.
 *
 * Author: MrCrayfish
 */
public class CupBlockEntity extends BlockEntity
{
    private ItemStack drink = ItemStack.EMPTY;

    public CupBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.CUP.get(), pos, state);
    }

    public ItemStack getDrink()
    {
        return this.drink;
    }

    public void setDrink(ItemStack drink)
    {
        this.drink = drink;
        BlockEntityUtil.sendUpdatePacket(this);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.drink = input.read("Drink", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store("Drink", ItemStack.OPTIONAL_CODEC, this.drink);
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
