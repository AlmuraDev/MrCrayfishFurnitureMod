package com.mrcrayfish.furniture.item;

import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.tileentity.TVBlockEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * A TV remote (1.12.2): pointed at a TV up to 16 blocks away it changes channel; sneaking it turns the TV on or off.
 *
 * Author: MrCrayfish
 */
public class TVRemoteItem extends Item
{
    private static final double RANGE = 16.0;

    public TVRemoteItem(Item.Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        Vec3 eye = player.getEyePosition();
        Vec3 end = eye.add(player.getLookAngle().scale(RANGE));
        BlockHitResult hit = level.clip(new ClipContext(eye, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        if(hit.getType() != HitResult.Type.BLOCK || !(level.getBlockEntity(hit.getBlockPos()) instanceof TVBlockEntity tv))
            return InteractionResult.PASS;

        if(!level.isClientSide())
        {
            if(player.isSecondaryUseActive())
            {
                tv.setPowered(!tv.isPowered());
            }
            else if(tv.nextChannel())
            {
                level.playSound(null, hit.getBlockPos(), ModSounds.BLOCK_TV_STATIC.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
