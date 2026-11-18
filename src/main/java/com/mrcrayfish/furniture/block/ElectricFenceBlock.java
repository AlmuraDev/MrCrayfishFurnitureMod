package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.common.ModDamageTypes;
import com.mrcrayfish.furniture.core.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * An electric fence (1.12.2): it zaps whatever touches it, and charges creepers as lightning would. Players in creative are left alone.
 *
 * Author: MrCrayfish
 */
public class ElectricFenceBlock extends FenceBlock
{
    public ElectricFenceBlock(Properties properties)
    {
        super(properties);
    }

    // A little bigger than the fence, so walking into it counts as touching it
    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity)
    {
        return Shapes.block();
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise)
    {
        if(!(level instanceof ServerLevel serverLevel) || !(entity instanceof LivingEntity living) || !living.isAlive())
            return;

        if(living instanceof Creeper creeper)
        {
            if(!creeper.isPowered())
            {
                LightningBolt lightning = EntityTypes.LIGHTNING_BOLT.create(serverLevel, EntitySpawnReason.TRIGGERED);
                if(lightning != null)
                {
                    lightning.setVisualOnly(true);
                    lightning.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    creeper.thunderHit(serverLevel, lightning);
                }
            }
            return;
        }

        if(living instanceof Player player && (player.isCreative() || player.isSpectator()))
            return;

        if(living.hurtServer(serverLevel, ModDamageTypes.electricFence(serverLevel), 2.0F))
        {
            serverLevel.playSound(null, pos, ModSounds.BLOCK_ELECTRIC_FENCE_ZAP.get(), SoundSource.BLOCKS, 0.2F, 1.0F);
            serverLevel.sendParticles(DustParticleOptions.REDSTONE, pos.getX() + serverLevel.getRandom().nextDouble(), pos.getY() + serverLevel.getRandom().nextDouble(), pos.getZ() + serverLevel.getRandom().nextDouble(), 1, 0.0, 0.0, 0.0, 0.0);
        }
    }
}
