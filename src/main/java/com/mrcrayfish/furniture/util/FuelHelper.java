package com.mrcrayfish.furniture.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.loot.NeoForgeLootContextParams;

import java.util.Optional;

/**
 * Burn times of furnace fuels, which 26.3 keeps on the item (the cooking fuel component) and works out in a container's loot context.
 */
public class FuelHelper
{
    public static <T extends BlockEntity & Container> int getBurnTime(ServerLevel level, T container, ItemStack fuel)
    {
        LootContext context = new LootContext.Builder(new LootParams.Builder(level)
                .withParameter(LootContextParams.BLOCK_STATE, container.getBlockState())
                .withParameter(LootContextParams.BLOCK_ENTITY, container)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(container.getBlockPos()))
                .withParameter(LootContextParams.CONTAINER, container)
                .withOptionalParameter(NeoForgeLootContextParams.QUERIED_STACK, fuel.isEmpty() ? null : fuel)
                .create(LootContextParamSets.CONTAINER_PROCESS)).create(Optional.empty());
        return ResolvableInt.getFromItem(fuel, DataComponents.COOKING_FUEL, CookingFuel::burnTime, context, 0);
    }
}
