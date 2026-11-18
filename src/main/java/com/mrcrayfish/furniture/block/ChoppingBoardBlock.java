package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModItems;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.item.crafting.ChoppingRecipe;
import com.mrcrayfish.furniture.tileentity.ChoppingBoardBlockEntity;
import com.mrcrayfish.furniture.util.RecipeLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * A chopping board (1.12.2). Using it with something that can be chopped (cfm:chopping recipes, such as bread) puts it on the board;
 * using it with a knife chops it, which takes one use of the knife. Using it otherwise takes the food back.
 *
 * Author: MrCrayfish
 */
public class ChoppingBoardBlock extends ApplianceBlock
{
    public ChoppingBoardBlock(Properties properties)
    {
        super(properties, Block.box(3, 0, 0.5, 13, 1.5, 15.5));
    }

    @Override
    protected boolean opensMenu()
    {
        return false;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if(!(level.getBlockEntity(pos) instanceof ChoppingBoardBlockEntity board))
            return InteractionResult.TRY_WITH_EMPTY_HAND;

        ItemStack food = board.getItem();
        if(stack.is(ModItems.KNIFE.get()) && !food.isEmpty())
        {
            Optional<RecipeHolder<ChoppingRecipe>> recipe = RecipeLookup.find(level, ModRecipeTypes.CHOPPING.get(), food);
            if(recipe.isEmpty())
                return InteractionResult.TRY_WITH_EMPTY_HAND;

            if(!level.isClientSide())
            {
                ItemStack chopped = recipe.get().value().assemble(new SingleRecipeInput(food));
                Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, chopped);
                board.setItem(ItemStack.EMPTY, board.getRotation());
                level.playSound(null, pos, ModSounds.BLOCK_CHOPPING_BOARD_CHOP.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
                stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
            return InteractionResult.SUCCESS;
        }

        if(food.isEmpty() && RecipeLookup.find(level, ModRecipeTypes.CHOPPING.get(), stack).isPresent())
        {
            if(!level.isClientSide())
            {
                board.setItem(stack.copyWithCount(1), state.getValue(DIRECTION).get2DDataValue());
                stack.consume(1, player);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(level.getBlockEntity(pos) instanceof ChoppingBoardBlockEntity board && !board.getItem().isEmpty())
        {
            if(!level.isClientSide())
            {
                board.takeItem();
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction)
    {
        return level.getBlockEntity(pos) instanceof ChoppingBoardBlockEntity board && !board.getItem().isEmpty() ? 1 : 0;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ChoppingBoardBlockEntity(pos, state);
    }
}
