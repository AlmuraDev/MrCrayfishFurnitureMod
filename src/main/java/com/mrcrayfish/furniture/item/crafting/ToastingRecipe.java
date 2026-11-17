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
 * Toasts a slice of bread in the toaster (1.12.2's toaster recipes).
 *
 * Author: MrCrayfish
 */
public class ToastingRecipe extends AbstractCookingRecipe
{
    public static final MapCodec<ToastingRecipe> MAP_CODEC = cookingMapCodec(ToastingRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, ToastingRecipe> STREAM_CODEC = cookingStreamCodec(ToastingRecipe::new);

    public ToastingRecipe(Recipe.CommonInfo commonInfo, CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime)
    {
        super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
    }

    @Override
    protected Item furnaceIcon()
    {
        return ModBlocks.TOASTER.get().asItem();
    }

    @Override
    public RecipeSerializer<ToastingRecipe> getSerializer()
    {
        return ModRecipeSerializers.TOASTING.get();
    }

    @Override
    public RecipeType<ToastingRecipe> getType()
    {
        return ModRecipeTypes.TOASTING.get();
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
