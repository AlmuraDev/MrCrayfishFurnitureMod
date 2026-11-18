package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.item.crafting.CuttingRecipe;
import com.mrcrayfish.furniture.item.crafting.MineBayRecipe;
import com.mrcrayfish.furniture.item.crafting.BlendingRecipe;
import com.mrcrayfish.furniture.item.crafting.ToastingRecipe;
import com.mrcrayfish.furniture.item.crafting.MicrowaveHeatingRecipe;
import com.mrcrayfish.furniture.item.crafting.OvenBakingRecipe;
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
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CuttingRecipe>> CUTTING = REGISTER.register("cutting", () -> new RecipeSerializer<>(CuttingRecipe.MAP_CODEC, CuttingRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FreezerSolidifyRecipe>> FREEZER_SOLIDIFY = REGISTER.register("freezer_solidify", () -> new RecipeSerializer<>(FreezerSolidifyRecipe.MAP_CODEC, FreezerSolidifyRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<OvenBakingRecipe>> OVEN_BAKING = REGISTER.register("oven_baking", () -> new RecipeSerializer<>(OvenBakingRecipe.MAP_CODEC, OvenBakingRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MicrowaveHeatingRecipe>> MICROWAVE_HEATING = REGISTER.register("microwave_heating", () -> new RecipeSerializer<>(MicrowaveHeatingRecipe.MAP_CODEC, MicrowaveHeatingRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ToastingRecipe>> TOASTING = REGISTER.register("toasting", () -> new RecipeSerializer<>(ToastingRecipe.MAP_CODEC, ToastingRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BlendingRecipe>> BLENDING = REGISTER.register("blending", () -> new RecipeSerializer<>(BlendingRecipe.MAP_CODEC, BlendingRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MineBayRecipe>> MINEBAY = REGISTER.register("minebay", () -> new RecipeSerializer<>(MineBayRecipe.MAP_CODEC, MineBayRecipe.STREAM_CODEC));
}
