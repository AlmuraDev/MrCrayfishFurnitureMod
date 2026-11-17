package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModItems;
import com.mrcrayfish.furniture.tileentity.BlenderBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/**
 * A blender (1.12.2). Use it with an item to put the whole stack in (up to four stacks); sneak and use it with an empty hand to blend
 * them into a drink, which fills six cups. Using it with an empty hand otherwise takes the last stack back out; using it with a cup
 * pours a drink.
 *
 * Author: MrCrayfish
 */
public class BlenderBlock extends ApplianceBlock
{
    public BlenderBlock(Properties properties)
    {
        super(properties, Block.box(4.5, 0, 4.5, 11.5, 15, 11.5));
    }

    @Override
    protected boolean opensMenu()
    {
        return false;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if(!(level.getBlockEntity(pos) instanceof BlenderBlockEntity blender) || blender.isRunning())
            return InteractionResult.TRY_WITH_EMPTY_HAND;

        if(blender.hasDrinks())
        {
            if(!stack.is(ModItems.CUP.get()))
                return InteractionResult.TRY_WITH_EMPTY_HAND;

            if(!level.isClientSide())
            {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, blender.pourDrink()));
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.5F);
            }
            return InteractionResult.SUCCESS;
        }

        if(blender.isFull())
            return InteractionResult.TRY_WITH_EMPTY_HAND;

        if(!level.isClientSide())
        {
            blender.addIngredient(stack.copy());
            if(!player.getAbilities().instabuild)
            {
                player.setItemInHand(hand, ItemStack.EMPTY);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(level.getBlockEntity(pos) instanceof BlenderBlockEntity blender && !blender.isRunning() && !blender.hasDrinks())
        {
            if(!level.isClientSide())
            {
                if(player.isSecondaryUseActive())
                {
                    blender.startBlending();
                }
                else
                {
                    blender.removeIngredient();
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new BlenderBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return createTickerHelper(type, ModBlockEntities.BLENDER.get(), level.isClientSide() ? BlenderBlockEntity::clientTick : BlenderBlockEntity::serverTick);
    }
}
