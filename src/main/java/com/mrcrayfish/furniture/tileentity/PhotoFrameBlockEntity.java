package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * The picture (PNG or JPEG from the web) in a photo frame (1.12.2).
 *
 * Author: MrCrayfish
 */
public class PhotoFrameBlockEntity extends ScreenBlockEntity
{
    public PhotoFrameBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.PHOTO_FRAME.get(), pos, state);
    }

    @Override
    public int getMaxUrls()
    {
        return 1;
    }

    @Nullable
    @Override
    public String getCurrentUrl()
    {
        return this.urls.isEmpty() ? null : this.urls.getFirst();
    }
}
