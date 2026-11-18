package com.mrcrayfish.furniture.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import java.util.Comparator;
import java.util.List;
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
     * Every recipe of a type, sorted by id so both sides list them in the same order.
     */
    public static <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> all(Level level, RecipeType<T> type)
    {
        RecipeMap recipes = level instanceof ServerLevel serverLevel ? serverLevel.recipeAccess().recipeMap() : clientRecipes;
        return recipes.byType(type).stream().sorted(Comparator.comparing(holder -> holder.id().identifier().toString())).toList();
    }

    /**
     * Called on the client with the recipes the server sent.
     */
    public static void setClientRecipes(RecipeMap recipes)
    {
        clientRecipes = recipes;
    }
}
