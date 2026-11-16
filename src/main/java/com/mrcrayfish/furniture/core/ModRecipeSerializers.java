package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.item.crafting.FreezerSolidifyRecipe;
import com.mrcrayfish.furniture.item.crafting.GrillCookingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Author: MrCrayfish
 */
public class ModRecipeSerializers
{
    public static final DeferredRegister<RecipeSerializer<?>> REGISTER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Reference.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GrillCookingRecipe>> GRILL_COOKING = REGISTER.register("grill_cooking", () -> new RecipeSerializer<>(GrillCookingRecipe.MAP_CODEC, GrillCookingRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FreezerSolidifyRecipe>> FREEZER_SOLIDIFY = REGISTER.register("freezer_solidify", () -> new RecipeSerializer<>(FreezerSolidifyRecipe.MAP_CODEC, FreezerSolidifyRecipe.STREAM_CODEC));
}
