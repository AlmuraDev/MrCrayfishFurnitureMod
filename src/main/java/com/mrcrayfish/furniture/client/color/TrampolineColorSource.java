package com.mrcrayfish.furniture.client.color;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Tints a trampoline item with its colour (the item's {@link DataComponents#BASE_COLOR}), as {@code "type": "cfm:trampoline_color"} in
 * its item model.
 */
public record TrampolineColorSource() implements ItemTintSource
{
    public static final MapCodec<TrampolineColorSource> MAP_CODEC = MapCodec.unit(new TrampolineColorSource());

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner)
    {
        DyeColor color = stack.get(DataComponents.BASE_COLOR);
        return color != null ? ARGB.opaque(color.getMapColor().col) : -1;
    }

    @Override
    public MapCodec<TrampolineColorSource> type()
    {
        return MAP_CODEC;
    }
}
