package com.mrcrayfish.furniture.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mrcrayfish.furniture.core.ModRecipeSerializers;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;

/**
 * Chops the item on a chopping board with a knife (1.12.2's chopping board recipes): bread into slices.
 */
public class ChoppingRecipe extends SingleItemRecipe
{
    public static final MapCodec<ChoppingRecipe> MAP_CODEC = simpleMapCodec(ChoppingRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, ChoppingRecipe> STREAM_CODEC = simpleStreamCodec(ChoppingRecipe::new);

    public ChoppingRecipe(Recipe.CommonInfo commonInfo, Ingredient ingredient, ItemStackTemplate result)
    {
        super(commonInfo, ingredient, result);
    }

    @Override
    public RecipeType<ChoppingRecipe> getType()
    {
        return ModRecipeTypes.CHOPPING.get();
    }

    @Override
    public RecipeSerializer<ChoppingRecipe> getSerializer()
    {
        return ModRecipeSerializers.CHOPPING.get();
    }

    @Override
    public String group()
    {
        return "";
    }

    @Override
    public boolean isSpecial()
    {
        return true;
    }

    @Override
    public RecipeBookCategory recipeBookCategory()
    {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
