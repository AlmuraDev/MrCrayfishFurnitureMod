package com.mrcrayfish.furniture.common;

import com.mrcrayfish.furniture.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

/**
 * The furniture's damage types (data/cfm/damage_type).
 */
public class ModDamageTypes
{
    public static final ResourceKey<DamageType> ELECTRIC_FENCE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "electric_fence"));

    public static DamageSource electricFence(Level level)
    {
        return level.damageSources().source(ELECTRIC_FENCE);
    }
}
