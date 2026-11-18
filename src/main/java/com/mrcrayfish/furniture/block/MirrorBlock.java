package com.mrcrayfish.furniture.block;

import net.minecraft.world.level.block.Block;

/**
 * A mirror on the wall (1.12.2). 1.12.2 showed a live reflection; here it is a mirror to look at, without one.
 *
 * Author: MrCrayfish
 */
public class MirrorBlock extends WallFurnitureBlock
{
    public MirrorBlock(Properties properties)
    {
        super(properties, Block.box(15, 0, 0, 16, 16, 16));
    }
}
