package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModItems;
import com.mrcrayfish.furniture.tileentity.CupBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * A cup set down on a block (1.12.2). A cup with a drink in it keeps the drink: sneak and use it with an empty hand to drink it there,
 * and breaking it gives the drink back.
 *
 * Author: MrCrayfish
 */
public class CupBlock extends FurnitureBlock implements EntityBlock
{
    private static final VoxelShape SHAPE = Block.box(5, 0, 5, 11, 7.5, 11);

    public CupBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, placer, stack);
        if(stack.is(ModItems.DRINK.get()) && level.getBlockEntity(pos) instanceof CupBlockEntity cup)
        {
            cup.setDrink(stack.copyWithCount(1));
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!player.isSecondaryUseActive() || !(level.getBlockEntity(pos) instanceof CupBlockEntity cup))
            return InteractionResult.PASS;

        ItemStack drink = cup.getDrink();
        FoodProperties food = drink.get(DataComponents.FOOD);
        if(food == null || !player.canEat(food.canAlwaysEat()))
            return InteractionResult.PASS;

        if(!level.isClientSide())
        {
            player.getFoodData().eat(food);
            cup.setDrink(ItemStack.EMPTY);
            level.playSound(null, pos, SoundEvents.GENERIC_DRINK.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            level.playSound(null, pos, SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }

    // The loot table drops an empty cup; a cup with a drink drops the drink instead
    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params)
    {
        List<ItemStack> drops = super.getDrops(state, params);
        if(params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof CupBlockEntity cup && !cup.getDrink().isEmpty())
        {
            return drops.stream().map(stack -> stack.is(ModItems.CUP.get()) ? cup.getDrink().copy() : stack).toList();
        }
        return drops;
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData)
    {
        if(level.getBlockEntity(pos) instanceof CupBlockEntity cup && !cup.getDrink().isEmpty())
        {
            return cup.getDrink().copy();
        }
        return new ItemStack(ModItems.CUP.get());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new CupBlockEntity(pos, state);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return false;
    }
}
