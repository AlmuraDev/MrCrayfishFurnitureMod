package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.HashSet;
import java.util.Set;

/**
 * A trampoline. Joined trampolines bounce higher; the count is how many are joined. Its colour comes from the item it was placed from
 * ({@link DataComponents#BASE_COLOR}) and goes back onto the item it drops.
 *
 * Author: MrCrayfish
 */
public class TrampolineBlockEntity extends BlockEntity
{
    private int count = 1;
    private DyeColor colour = DyeColor.WHITE;

    public TrampolineBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.TRAMPOLINE.get(), pos, state);
    }

    protected TrampolineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    @Override
    public void onLoad()
    {
        BlockEntityUtil.sendUpdatePacket(this);
    }

    public void setCount(int count)
    {
        this.count = count;
        this.setChanged();
        BlockEntityUtil.sendUpdatePacket(this);
    }

    public int getCount()
    {
        return this.count;
    }

    public DyeColor getColour()
    {
        return this.colour;
    }

    public void setColour(DyeColor colour)
    {
        this.colour = colour;
        this.setChanged();
    }

    public void updateCount()
    {
        Set<TrampolineBlockEntity> trampolines = new HashSet<>();
        this.isTrampoline(trampolines, this.worldPosition);
        trampolines.forEach(trampoline -> trampoline.setCount(trampolines.size()));
    }

    private void isTrampoline(Set<TrampolineBlockEntity> trampolines, BlockPos pos)
    {
        if(this.level == null)
            return;

        if(this.level.getBlockEntity(pos) instanceof TrampolineBlockEntity trampoline)
        {
            if(trampolines.contains(trampoline))
                return;

            trampolines.add(trampoline);
            this.isTrampoline(trampolines, pos.relative(Direction.NORTH));
            this.isTrampoline(trampolines, pos.relative(Direction.EAST));
            this.isTrampoline(trampolines, pos.relative(Direction.SOUTH));
            this.isTrampoline(trampolines, pos.relative(Direction.WEST));
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components)
    {
        super.applyImplicitComponents(components);
        this.colour = components.getOrDefault(DataComponents.BASE_COLOR, DyeColor.WHITE);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components)
    {
        super.collectImplicitComponents(components);
        components.set(DataComponents.BASE_COLOR, this.colour);
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output)
    {
        super.removeComponentsFromTag(output);
        output.discard("Color");
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.count = input.getIntOr("Count", this.count);
        input.read("Color", DyeColor.CODEC).ifPresent(colour -> this.colour = colour);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putInt("Count", this.count);
        output.store("Color", DyeColor.CODEC, this.colour);
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
