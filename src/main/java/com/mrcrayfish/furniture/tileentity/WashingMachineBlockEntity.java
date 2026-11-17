package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.common.ModTags;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.inventory.container.WashingMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A washing machine (1.12.2): four armour slots, one for each place armour is worn (#cfm:washing_machine_cleanable).
 *
 * Author: MrCrayfish
 */
public class WashingMachineBlockEntity extends WasherBlockEntity
{
    public static final int ARMOUR_SLOTS = 4;

    public WashingMachineBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.WASHING_MACHINE.get(), pos, state, ARMOUR_SLOTS);
    }

    @Override
    protected TagKey<Item> cleanable()
    {
        return ModTags.Items.WASHING_MACHINE_CLEANABLE;
    }

    @Override
    protected SoundEvent washingSound()
    {
        return ModSounds.BLOCK_WASHING_MACHINE.get();
    }

    // Head, chest, legs and feet, in that order; anything else (the elytra is worn on the chest) goes anywhere
    @Override
    protected int slotFor(ItemStack stack)
    {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if(equippable == null)
            return -1;
        return switch(equippable.slot())
        {
            case HEAD -> 0;
            case CHEST -> 1;
            case LEGS -> 2;
            case FEET -> 3;
            default -> -1;
        };
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.washing_machine");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return new WashingMachineMenu(windowId, playerInventory, this);
    }

    @Override
    public boolean isMatchingContainerMenu(AbstractContainerMenu menu)
    {
        return menu instanceof WashingMachineMenu washerMenu && washerMenu.getBlockEntity() == this;
    }
}
