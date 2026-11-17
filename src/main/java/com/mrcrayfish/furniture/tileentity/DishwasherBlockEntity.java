package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.common.ModTags;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.inventory.container.DishwasherMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A dishwasher (1.12.2): six tool slots, the first five for pickaxes, shovels, swords, axes and hoes, the last for anything else it
 * cleans (#cfm:dishwasher_cleanable).
 *
 * Author: MrCrayfish
 */
public class DishwasherBlockEntity extends WasherBlockEntity
{
    public static final int TOOL_SLOTS = 6;

    public DishwasherBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.DISHWASHER.get(), pos, state, TOOL_SLOTS);
    }

    @Override
    protected TagKey<Item> cleanable()
    {
        return ModTags.Items.DISHWASHER_CLEANABLE;
    }

    @Override
    protected SoundEvent washingSound()
    {
        return ModSounds.BLOCK_DISHWASHER.get();
    }

    @Override
    protected int slotFor(ItemStack stack)
    {
        if(stack.is(ItemTags.PICKAXES))
            return 0;
        if(stack.is(ItemTags.SHOVELS))
            return 1;
        if(stack.is(ItemTags.SWORDS))
            return 2;
        if(stack.is(ItemTags.AXES))
            return 3;
        if(stack.is(ItemTags.HOES))
            return 4;
        return 5;
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.dishwasher");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return new DishwasherMenu(windowId, playerInventory, this);
    }

    @Override
    public boolean isMatchingContainerMenu(AbstractContainerMenu menu)
    {
        return menu instanceof DishwasherMenu washerMenu && washerMenu.getBlockEntity() == this;
    }
}
