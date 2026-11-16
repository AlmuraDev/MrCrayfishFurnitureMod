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
 * Cooks an item on the grill.
 *
 * Author: MrCrayfish
 */
public class GrillCookingRecipe extends AbstractCookingRecipe
{
    public static final MapCodec<GrillCookingRecipe> MAP_CODEC = cookingMapCodec(GrillCookingRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, GrillCookingRecipe> STREAM_CODEC = cookingStreamCodec(GrillCookingRecipe::new);

    public GrillCookingRecipe(Recipe.CommonInfo commonInfo, CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime)
    {
        super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
    }

    @Override
    protected Item furnaceIcon()
    {
        return ModBlocks.GRILL_RED.get().asItem();
    }

    @Override
    public RecipeSerializer<GrillCookingRecipe> getSerializer()
    {
        return ModRecipeSerializers.GRILL_COOKING.get();
    }

    @Override
    public RecipeType<GrillCookingRecipe> getType()
    {
        return ModRecipeTypes.GRILL_COOKING.get();
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
