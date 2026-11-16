package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * Runs a shower head while it's on: water falls, it sounds, and every quarter second it washes the players standing under it: potion
 * effects and fire come off, and so does the dye on leather armour (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class ShowerHeadBlockEntity extends BlockEntity
{
    private int timer = 20;

    public ShowerHeadBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.SHOWER_HEAD.get(), pos, state);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, ShowerHeadBlockEntity shower)
    {
        RandomSource random = level.getRandom();
        double posX = pos.getX() + 0.35D + random.nextDouble() / 3;
        double posZ = pos.getZ() + 0.35D + random.nextDouble() / 3;
        level.addParticle(ParticleTypes.FALLING_WATER, posX, pos.getY() + 0.065D, posZ, 0.0D, 0.0D, 0.0D);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ShowerHeadBlockEntity shower)
    {
        if(shower.timer % 5 == 0)
        {
            for(Player player : level.getEntitiesOfClass(Player.class, new AABB(pos.below())))
            {
                player.removeAllEffects();
                player.clearFire();
                for(EquipmentSlot slot : EquipmentSlot.values())
                {
                    if(slot.isArmor())
                    {
                        ItemStack armour = player.getItemBySlot(slot);
                        if(armour.has(DataComponents.DYED_COLOR))
                        {
                            armour.remove(DataComponents.DYED_COLOR);
                        }
                    }
                }
            }
        }
        if(shower.timer >= 20)
        {
            level.playSound(null, pos, ModSounds.BLOCK_SHOWER.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
            shower.timer = 0;
        }
        shower.timer++;
    }
}
