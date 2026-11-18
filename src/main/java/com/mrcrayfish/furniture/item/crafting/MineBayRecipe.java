package com.mrcrayfish.furniture.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrcrayfish.furniture.core.ModRecipeSerializers;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

/**
 * Something for sale on MineBay, the computer's shop (1.12.2): the item, and its price (so many of a currency item, put in the
 * computer's slot). Servers change what is for sale with datapacks.
 */
public class MineBayRecipe implements Recipe<SingleRecipeInput>
{
    public static final MapCodec<MineBayRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(MineBayRecipe::result),
            SizedIngredient.NESTED_CODEC.fieldOf("price").forGetter(MineBayRecipe::price)
    ).apply(i, MineBayRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MineBayRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
            ItemStackTemplate.STREAM_CODEC, MineBayRecipe::result,
            SizedIngredient.STREAM_CODEC, MineBayRecipe::price,
            MineBayRecipe::new);

    private final Recipe.CommonInfo commonInfo;
    private final ItemStackTemplate result;
    private final SizedIngredient price;

    public MineBayRecipe(Recipe.CommonInfo commonInfo, ItemStackTemplate result, SizedIngredient price)
    {
        this.commonInfo = commonInfo;
        this.result = result;
        this.price = price;
    }

    public ItemStackTemplate result()
    {
        return this.result;
    }

    public SizedIngredient price()
    {
        return this.price;
    }

    // Whether the stack pays for it
    @Override
    public boolean matches(SingleRecipeInput input, Level level)
    {
        return this.price.test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input)
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
    public RecipeSerializer<MineBayRecipe> getSerializer()
    {
        return ModRecipeSerializers.MINEBAY.get();
    }

    @Override
    public RecipeType<MineBayRecipe> getType()
    {
        return ModRecipeTypes.MINEBAY.get();
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
}
