package com.mrcrayfish.furniture.core;

import com.mojang.serialization.Codec;
import com.mrcrayfish.furniture.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

/**
 * Data the furniture keeps on items.
 */
public class ModDataComponents
{
    public static final DeferredRegister.DataComponents REGISTER = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Reference.MOD_ID);

    // The lights a light switch item (or placed switch) turns on and off
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<BlockPos>>> LINKED_LIGHTS = REGISTER.registerComponentType("linked_lights",
            builder -> builder.persistent(BlockPos.CODEC.listOf()).networkSynchronized(BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list())));

    // What is in a present (not vanilla's container component, so the tooltip doesn't give it away)
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> PRESENT_CONTENTS = REGISTER.registerComponentType("present_contents",
            builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC));

    // Who wrapped a present; a present without it is still open
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> PRESENT_FROM = REGISTER.registerComponentType("present_from",
            builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));
}
