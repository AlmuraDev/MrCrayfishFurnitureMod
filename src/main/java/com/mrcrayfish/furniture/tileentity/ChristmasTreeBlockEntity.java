package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * The ornaments hung on one half of a Christmas tree, one on each side (1.12.2). The client draws them.
 *
 * Author: MrCrayfish
 */
public class ChristmasTreeBlockEntity extends BlockEntity
{
    private NonNullList<ItemStack> ornaments = NonNullList.withSize(4, ItemStack.EMPTY);

    public ChristmasTreeBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.CHRISTMAS_TREE.get(), pos, state);
    }

    public NonNullList<ItemStack> getOrnaments()
    {
        return this.ornaments;
    }

    /**
     * Hangs an ornament on the side facing the given way (the way the player looks), dropping what hung there.
     */
    public void hangOrnament(Direction facing, ItemStack ornament)
    {
        int side = facing.get2DDataValue();
        ItemStack old = this.ornaments.get(side);
        if(!old.isEmpty() && this.level != null)
        {
            Containers.dropItemStack(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.0, this.worldPosition.getZ() + 0.5, old);
        }
        this.ornaments.set(side, ornament);
        BlockEntityUtil.sendUpdatePacket(this);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        if(this.level != null)
        {
            Containers.dropContents(this.level, pos, this.ornaments);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.ornaments = NonNullList.withSize(4, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.ornaments);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.ornaments);
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
