package com.mrcrayfish.furniture.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;

/**
 * A cup with a drink in it. Used on a block it sets the cup down (the cup block keeps the drink); used in the air it's drunk, as 1.12.2
 * did. The empty cup is the cup block's item, so this doesn't claim the block.
 */
public class DrinkItem extends BlockItem
{
    public DrinkItem(Block block, Item.Properties properties)
    {
        super(block, properties);
    }

    @Override
    public void registerBlocks(Map<Block, Item> map, Item item) {}
}
