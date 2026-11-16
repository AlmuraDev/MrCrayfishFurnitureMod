package com.mrcrayfish.furniture.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.Supplier;

/**
 * An item named after one block that places another: the fridge item places the freezer, which then puts the fridge on top.
 *
 * Author: MrCrayfish
 */
public class BlockSupplierItem extends BlockItem
{
    private final Block block;
    private final Supplier<? extends Block> supplier;

    // The properties use the named block's description (useBlockDescriptionPrefix, given the named block)
    public BlockSupplierItem(Item.Properties properties, Block block, Supplier<? extends Block> supplier)
    {
        super(block, properties);
        this.block = block;
        this.supplier = supplier;
    }

    @Override
    public Block getBlock()
    {
        return this.supplier.get();
    }

    @Override
    public void registerBlocks(Map<Block, Item> map, Item item)
    {
        map.put(this.block, item);
    }
}
