package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.entity.SeatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities
{
    public static final DeferredRegister.Entities REGISTER = DeferredRegister.createEntities(Reference.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<SeatEntity>> SEAT = REGISTER.registerEntityType("seat", SeatEntity::new, MobCategory.MISC, builder -> builder.sized(0.0F, 0.0F).noLootTable().noSummon());
}
