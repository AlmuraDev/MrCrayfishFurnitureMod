package com.mrcrayfish.furniture.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrcrayfish.furniture.core.ModRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.List;

/**
 * A shapeless recipe made with an axe, which takes one use of damage instead of being used up (1.12.2's cfm:cutting_shapeless): an axe
 * and a log make firewood.
 *
 * Author: MrCrayfish
 */
public class CuttingRecipe extends ShapelessRecipe
{
    public static final MapCodec<CuttingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(CuttingRecipe::result),
            Codec.lazyInitialized(() -> Ingredient.CODEC.listOf(1, 9)).fieldOf("ingredients").forGetter(CuttingRecipe::ingredients)
    ).apply(i, CuttingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CuttingRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC, o -> o.bookInfo,
            ItemStackTemplate.STREAM_CODEC, CuttingRecipe::result,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), CuttingRecipe::ingredients,
            CuttingRecipe::new);

    private final List<Ingredient> ingredients;

    public CuttingRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ItemStackTemplate result, List<Ingredient> ingredients)
    {
        super(commonInfo, bookInfo, result, ingredients);
        this.ingredients = ingredients;
    }

    public List<Ingredient> ingredients()
    {
        return this.ingredients;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeSerializer<ShapelessRecipe> getSerializer()
    {
        // The serializer makes CuttingRecipes; it's typed as the shapeless one only because this extends ShapelessRecipe
        return (RecipeSerializer<ShapelessRecipe>) (RecipeSerializer<?>) ModRecipeSerializers.CUTTING.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input)
    {
        NonNullList<ItemStack> remaining = CraftingRecipe.defaultCraftingReminder(input);
        for(int slot = 0; slot < input.size(); slot++)
        {
            ItemStack stack = input.getItem(slot);
            if(stack.is(ItemTags.AXES))
            {
                ItemStack axe = stack.copyWithCount(1);
                axe.setDamageValue(axe.getDamageValue() + 1);
                remaining.set(slot, axe.isDamageableItem() && axe.getDamageValue() >= axe.getMaxDamage() ? ItemStack.EMPTY : axe);
            }
        }
        return remaining;
    }
}
