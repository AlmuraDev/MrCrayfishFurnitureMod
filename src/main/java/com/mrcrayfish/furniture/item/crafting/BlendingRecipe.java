package com.mrcrayfish.furniture.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrcrayfish.furniture.core.ModRecipeSerializers;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

/**
 * Blends a drink from the ingredients in a blender (1.12.2's blender recipes). Each ingredient needs a stack of exactly its count in the
 * blender (as in 1.12.2, since blending uses up the whole stacks), and every stack in the blender has to be used. The result is usually a {@code cfm:drink} whose components give
 * its name, colour and how much it feeds; the blender fills six cups with it.
 */
public class BlendingRecipe implements Recipe<BlendingRecipe.Input>
{
    public static final MapCodec<BlendingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            SizedIngredient.NESTED_CODEC.listOf(1, Input.SIZE).fieldOf("ingredients").forGetter(BlendingRecipe::ingredients),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(BlendingRecipe::result)
    ).apply(i, BlendingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BlendingRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list(Input.SIZE)), BlendingRecipe::ingredients,
            ItemStackTemplate.STREAM_CODEC, BlendingRecipe::result,
            BlendingRecipe::new);

    private final Recipe.CommonInfo commonInfo;
    private final List<SizedIngredient> ingredients;
    private final ItemStackTemplate result;

    public BlendingRecipe(Recipe.CommonInfo commonInfo, List<SizedIngredient> ingredients, ItemStackTemplate result)
    {
        this.commonInfo = commonInfo;
        this.ingredients = List.copyOf(ingredients);
        this.result = result;
    }

    public List<SizedIngredient> ingredients()
    {
        return this.ingredients;
    }

    public ItemStackTemplate result()
    {
        return this.result;
    }

    @Override
    public boolean matches(Input input, Level level)
    {
        List<ItemStack> stacks = input.items().stream().filter(stack -> !stack.isEmpty()).toList();
        return stacks.size() == this.ingredients.size() && this.match(stacks, 0, new boolean[stacks.size()]);
    }

    // Gives each ingredient its own stack; there are at most four, so trying every pairing is cheap
    private boolean match(List<ItemStack> stacks, int ingredient, boolean[] used)
    {
        if(ingredient == this.ingredients.size())
            return true;

        for(int i = 0; i < stacks.size(); i++)
        {
            SizedIngredient sized = this.ingredients.get(ingredient);
            ItemStack stack = stacks.get(i);
            if(!used[i] && sized.ingredient().test(stack) && stack.getCount() == sized.count())
            {
                used[i] = true;
                if(this.match(stacks, ingredient + 1, used))
                    return true;
                used[i] = false;
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(Input input)
    {
        return this.result.create();
    }

    @Override
    public boolean showNotification()
    {
        return this.commonInfo.showNotification();
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
    public RecipeSerializer<BlendingRecipe> getSerializer()
    {
        return ModRecipeSerializers.BLENDING.get();
    }

    @Override
    public RecipeType<BlendingRecipe> getType()
    {
        return ModRecipeTypes.BLENDING.get();
    }

    @Override
    public PlacementInfo placementInfo()
    {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory()
    {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    /**
     * The stacks in a blender.
     */
    public record Input(List<ItemStack> items) implements RecipeInput
    {
        public static final int SIZE = 4;

        @Override
        public ItemStack getItem(int index)
        {
            return this.items.get(index);
        }

        @Override
        public int size()
        {
            return this.items.size();
        }
    }
}
