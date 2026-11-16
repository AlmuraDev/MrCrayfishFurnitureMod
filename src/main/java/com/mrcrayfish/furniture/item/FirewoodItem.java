package com.mrcrayfish.furniture.item;

import com.mrcrayfish.furniture.block.FirePitBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Firewood (the 1.12.2 log item). Placing it starts a fire pit; using it on an unlit fire pit adds another log, up to four.
 *
 * Author: MrCrayfish
 */
public class FirewoodItem extends BlockItem
{
    public FirewoodItem(Block block, Item.Properties properties)
    {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if(state.getBlock() instanceof FirePitBlock && !state.getValue(FirePitBlock.LIT))
        {
            int stage = state.getValue(FirePitBlock.STAGE);
            if(stage >= FirePitBlock.MAX_STAGE)
                return InteractionResult.FAIL;

            if(!level.isClientSide())
            {
                level.setBlock(pos, state.setValue(FirePitBlock.STAGE, stage + 1), Block.UPDATE_ALL);
                level.playSound(null, pos, SoundType.WOOD.getPlaceSound(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                context.getItemInHand().consume(1, context.getPlayer());
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
