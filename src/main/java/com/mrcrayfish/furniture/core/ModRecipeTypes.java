package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.item.crafting.BlendingRecipe;
import com.mrcrayfish.furniture.item.crafting.FreezerSolidifyRecipe;
import com.mrcrayfish.furniture.item.crafting.MicrowaveHeatingRecipe;
import com.mrcrayfish.furniture.item.crafting.OvenBakingRecipe;
import com.mrcrayfish.furniture.item.crafting.ToastingRecipe;
import com.mrcrayfish.furniture.item.crafting.GrillCookingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Author: MrCrayfish
 */
public class ModRecipeTypes
{
    public static final DeferredRegister<RecipeType<?>> REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<GrillCookingRecipe>> GRILL_COOKING = create("grill_cooking");
    public static final DeferredHolder<RecipeType<?>, RecipeType<FreezerSolidifyRecipe>> FREEZER_SOLIDIFY = create("freezer_solidify");
    public static final DeferredHolder<RecipeType<?>, RecipeType<OvenBakingRecipe>> OVEN_BAKING = create("oven_baking");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MicrowaveHeatingRecipe>> MICROWAVE_HEATING = create("microwave_heating");
    public static final DeferredHolder<RecipeType<?>, RecipeType<ToastingRecipe>> TOASTING = create("toasting");
    public static final DeferredHolder<RecipeType<?>, RecipeType<BlendingRecipe>> BLENDING = create("blending");

    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> create(String name)
    {
        return REGISTER.register(name, () -> new RecipeType<>()
        {
            @Override
            public String toString()
            {
                return name;
            }
        });
    }
}
