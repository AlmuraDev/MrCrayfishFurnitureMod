package com.mrcrayfish.furniture.common;

import com.mrcrayfish.furniture.core.ModItems;
import com.mrcrayfish.furniture.tileentity.GrillBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DispenserBlock;

/**
 * Author: MrCrayfish
 */
public class CommonHandler
{
    public static void setup()
    {
        // A dispenser holding a spatula flips the items on the grill in front of it, one block down
        DispenserBlock.registerBehavior(ModItems.SPATULA.get(), (source, stack) ->
        {
            Direction direction = source.state().getValue(DispenserBlock.FACING);
            BlockPos pos = source.pos().relative(direction).below();
            if(source.level().getBlockEntity(pos) instanceof GrillBlockEntity grill)
            {
                grill.flipItems();
            }
            return stack;
        });
    }
}
