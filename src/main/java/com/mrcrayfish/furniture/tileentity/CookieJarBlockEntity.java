package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.block.CookieJarBlock;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A cookie jar keeps its cookies in its block state; this is here so the client can draw them, and so they spill out when the jar is
 * broken.
 *
 * Author: MrCrayfish
 */
public class CookieJarBlockEntity extends BlockEntity
{
    public CookieJarBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.COOKIE_JAR.get(), pos, state);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        if(this.level != null && state.hasProperty(CookieJarBlock.COOKIES))
        {
            int cookies = state.getValue(CookieJarBlock.COOKIES);
            if(cookies > 0)
            {
                Containers.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.COOKIE, cookies));
            }
        }
    }
}
