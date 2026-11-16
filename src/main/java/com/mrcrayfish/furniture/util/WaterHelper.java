package com.mrcrayfish.furniture.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

/**
 * The bathroom furniture's water handling, as 1.12.2 had it: buckets and bottles take water out or put it in, and furniture with a
 * still water source two blocks below (under the block it stands on) can draw it up.
 */
public class WaterHelper
{
    /**
     * What the held item does to a water container.
     */
    public enum Use
    {
        TAKE,
        ADD,
        NONE
    }

    public static Use useOf(ItemStack stack)
    {
        if(stack.is(Items.BUCKET) || stack.is(Items.GLASS_BOTTLE))
            return Use.TAKE;
        if(stack.is(Items.WATER_BUCKET) || isWaterBottle(stack))
            return Use.ADD;
        return Use.NONE;
    }

    private static boolean isWaterBottle(ItemStack stack)
    {
        PotionContents contents = stack.get(net.minecraft.core.component.DataComponents.POTION_CONTENTS);
        return contents != null && contents.is(Potions.WATER);
    }

    /**
     * Swaps the held container for its filled or emptied version and plays its sound. Call on the server after taking or adding water.
     */
    public static void exchange(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack held, Use use)
    {
        ItemStack result;
        SoundEvent sound;
        if(use == Use.TAKE)
        {
            boolean bucket = held.is(Items.BUCKET);
            result = bucket ? new ItemStack(Items.WATER_BUCKET) : PotionContents.createItemStack(Items.POTION, Potions.WATER);
            sound = bucket ? SoundEvents.BUCKET_FILL : SoundEvents.BOTTLE_FILL;
        }
        else
        {
            boolean bucket = held.is(Items.WATER_BUCKET);
            result = new ItemStack(bucket ? Items.BUCKET : Items.GLASS_BOTTLE);
            sound = bucket ? SoundEvents.BUCKET_EMPTY : SoundEvents.BOTTLE_EMPTY;
        }
        player.setItemInHand(hand, ItemUtils.createFilledResult(held, player, result));
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public static boolean hasWaterSource(Level level, BlockPos pos)
    {
        return level.getFluidState(pos.below(2)).isSourceOfType(Fluids.WATER);
    }

    public static void takeWaterSource(Level level, BlockPos pos)
    {
        level.setBlockAndUpdate(pos.below(2), Blocks.AIR.defaultBlockState());
    }

    public static void splash(Level level, BlockPos pos, double height)
    {
        if(level instanceof ServerLevel serverLevel)
        {
            serverLevel.sendParticles(ParticleTypes.SPLASH, pos.getX() + 0.5, pos.getY() + height, pos.getZ() + 0.5, 5, 0.2, 0.0, 0.2, 0.0);
        }
    }
}
