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
 * Freezes an item in the freezer.
 *
 * Author: MrCrayfish
 */
public class FreezerSolidifyRecipe extends AbstractCookingRecipe
{
    public static final MapCodec<FreezerSolidifyRecipe> MAP_CODEC = cookingMapCodec(FreezerSolidifyRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, FreezerSolidifyRecipe> STREAM_CODEC = cookingStreamCodec(FreezerSolidifyRecipe::new);

    public FreezerSolidifyRecipe(Recipe.CommonInfo commonInfo, CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime)
    {
        super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
    }

    @Override
    protected Item furnaceIcon()
    {
        return ModBlocks.FRIDGE_LIGHT.get().asItem();
    }

    @Override
    public RecipeSerializer<FreezerSolidifyRecipe> getSerializer()
    {
        return ModRecipeSerializers.FREEZER_SOLIDIFY.get();
    }

    @Override
    public RecipeType<FreezerSolidifyRecipe> getType()
    {
        return ModRecipeTypes.FREEZER_SOLIDIFY.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory()
    {
        return RecipeBookCategories.FURNACE_MISC;
    }

    @Override
    public boolean isSpecial()
    {
        return true;
    }
}
