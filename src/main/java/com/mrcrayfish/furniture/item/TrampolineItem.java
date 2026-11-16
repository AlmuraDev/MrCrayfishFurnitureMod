package com.mrcrayfish.furniture.item;

import com.mrcrayfish.furniture.Reference;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

/**
 * A trampoline in one of the sixteen colours. The colour is the item's {@link DataComponents#BASE_COLOR}; the trampoline's block entity
 * takes it when placed and gives it back when broken.
 *
 * Author: MrCrayfish
 */
public class TrampolineItem extends BlockItem implements CreativeItem
{
    public TrampolineItem(Block block, Item.Properties properties)
    {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack)
    {
        DyeColor color = stack.get(DataComponents.BASE_COLOR);
        if(color != null)
        {
            return Component.translatable(String.format("block.%s.%s_trampoline", Reference.MOD_ID, color.getName()));
        }
        return super.getName(stack);
    }

    @Override
    public void fill(Consumer<ItemStack> output)
    {
        for(DyeColor color : DyeColor.values())
        {
            ItemStack stack = new ItemStack(this);
            stack.set(DataComponents.BASE_COLOR, color);
            output.accept(stack);
        }
    }
}
