package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * A wrapped present under the tree: what is inside and who it's from (1.12.2). Opening it (breaking it) spills what is inside.
 *
 * Author: MrCrayfish
 */
public class PresentBlockEntity extends BlockEntity
{
    public static final int SIZE = 4;

    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    private String from = "";

    public PresentBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.PRESENT.get(), pos, state);
    }

    public String getFrom()
    {
        return this.from;
    }

    // Takes the contents and sender from the present item when it's placed
    @Override
    protected void applyImplicitComponents(DataComponentGetter components)
    {
        super.applyImplicitComponents(components);
        this.items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        components.getOrDefault(ModDataComponents.PRESENT_CONTENTS.get(), ItemContainerContents.EMPTY).copyInto(this.items);
        this.from = components.getOrDefault(ModDataComponents.PRESENT_FROM.get(), "");
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        if(this.level != null)
        {
            Containers.dropContents(this.level, pos, this.items);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);
        this.from = input.getStringOr("From", "");
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
        output.putString("From", this.from);
    }
}
