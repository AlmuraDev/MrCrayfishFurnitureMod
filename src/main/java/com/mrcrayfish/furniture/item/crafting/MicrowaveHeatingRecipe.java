package com.mrcrayfish.furniture.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.core.ModRecipeSerializers;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Heats food in the microwave, quicker than the oven (1.12.2's microwave recipes).
 *
 * Author: MrCrayfish
 */
public class MicrowaveHeatingRecipe extends AbstractCookingRecipe
{
    public static final MapCodec<MicrowaveHeatingRecipe> MAP_CODEC = cookingMapCodec(MicrowaveHeatingRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, MicrowaveHeatingRecipe> STREAM_CODEC = cookingStreamCodec(MicrowaveHeatingRecipe::new);

    public MicrowaveHeatingRecipe(Recipe.CommonInfo commonInfo, CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime)
    {
        super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
    }

    @Override
    protected Item furnaceIcon()
    {
        return ModBlocks.MICROWAVE.get().asItem();
    }

    @Override
    public RecipeSerializer<MicrowaveHeatingRecipe> getSerializer()
    {
        return ModRecipeSerializers.MICROWAVE_HEATING.get();
    }

    @Override
    public RecipeType<MicrowaveHeatingRecipe> getType()
    {
        return ModRecipeTypes.MICROWAVE_HEATING.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory()
    {
        return RecipeBookCategories.FURNACE_FOOD;
    }

    @Override
    public boolean isSpecial()
    {
        return true;
    }
}
