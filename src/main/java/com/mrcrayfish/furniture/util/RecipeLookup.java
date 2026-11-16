package com.mrcrayfish.furniture.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * Finds the furniture's single-item recipes on either side. The server has every recipe; the client only has the types the server sends
 * it on joining or reloading (see CommonEvents#onDatapackSync), which the client stores here.
 */
public class RecipeLookup
{
    private static volatile RecipeMap clientRecipes = RecipeMap.EMPTY;

    public static <T extends Recipe<SingleRecipeInput>> Optional<RecipeHolder<T>> find(Level level, RecipeType<T> type, ItemStack stack)
    {
        SingleRecipeInput input = new SingleRecipeInput(stack);
        if(level instanceof ServerLevel serverLevel)
        {
            return serverLevel.recipeAccess().getRecipeFor(type, input, serverLevel);
        }
        return clientRecipes.getRecipesFor(type, input, level).findFirst();
    }

    /**
     * Called on the client with the recipes the server sent.
     */
    public static void setClientRecipes(RecipeMap recipes)
    {
        clientRecipes = recipes;
    }
}
