package com.mrcrayfish.furniture.inventory.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

/**
 * A slot that only takes what its container allows there ({@link Container#canPlaceItem}), so the screen and hoppers follow the same
 * rules.
 */
public class ContainerRuleSlot extends Slot
{
    public ContainerRuleSlot(Container container, int index, int x, int y)
    {
        super(container, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return this.container.canPlaceItem(this.getContainerSlot(), stack);
    }
}
