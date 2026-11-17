package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.item.crafting.BlendingRecipe;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;

/**
 * The stacks in a blender, and the drink it made. Blending takes ten seconds and makes six drinks; the ingredients are used up (their
 * containers, such as the milk bucket, drop out). Ingredients and drink are sent to the client, which draws them.
 *
 * Author: MrCrayfish
 */
public class BlenderBlockEntity extends BlockEntity implements Appliance
{
    public static final int BLEND_TIME = 200;
    public static final int DRINKS = 6;

    private NonNullList<ItemStack> ingredients = NonNullList.withSize(BlendingRecipe.Input.SIZE, ItemStack.EMPTY);
    private boolean blending;
    private int progress;
    private int drinkCount;
    // What blending makes, and then what is poured into each cup
    private ItemStack drink = ItemStack.EMPTY;

    public BlenderBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.BLENDER.get(), pos, state);
    }

    // The client counts along so the blades spin and the drink fills while it blends
    public static void clientTick(Level level, BlockPos pos, BlockState state, BlenderBlockEntity blender)
    {
        if(blender.blending && blender.progress < BLEND_TIME)
        {
            blender.progress++;
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BlenderBlockEntity blender)
    {
        if(!blender.blending)
            return;

        if(blender.progress % 20 == 0)
        {
            level.playSound(null, pos, ModSounds.BLOCK_BLENDER.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
        }

        if(++blender.progress >= BLEND_TIME)
        {
            for(int i = 0; i < blender.ingredients.size(); i++)
            {
                ItemStack ingredient = blender.ingredients.get(i);
                ItemStackTemplate remainder = ingredient.getCraftingRemainder();
                if(remainder != null)
                {
                    Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, remainder.withCount(ingredient.getCount()).create());
                }
                blender.ingredients.set(i, ItemStack.EMPTY);
            }
            blender.blending = false;
            blender.progress = 0;
            blender.drinkCount = DRINKS;
            BlockEntityUtil.sendUpdatePacket(blender);
            level.updateNeighbourForOutputSignal(pos, state.getBlock());
        }
        else
        {
            blender.setChanged();
        }
    }

    private Optional<ItemStack> findDrink()
    {
        if(!(this.level instanceof ServerLevel level))
            return Optional.empty();

        BlendingRecipe.Input input = new BlendingRecipe.Input(this.ingredients);
        return level.recipeAccess().getRecipeFor(ModRecipeTypes.BLENDING.get(), input, level).map(recipe -> recipe.value().assemble(input));
    }

    public void addIngredient(ItemStack stack)
    {
        for(int i = 0; i < this.ingredients.size(); i++)
        {
            if(this.ingredients.get(i).isEmpty())
            {
                this.ingredients.set(i, stack);
                BlockEntityUtil.sendUpdatePacket(this);
                return;
            }
        }
    }

    // Takes out the last stack put in
    public void removeIngredient()
    {
        for(int i = this.ingredients.size() - 1; i >= 0; i--)
        {
            ItemStack ingredient = this.ingredients.get(i);
            if(!ingredient.isEmpty() && this.level != null)
            {
                Containers.dropItemStack(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.0, this.worldPosition.getZ() + 0.5, ingredient);
                this.ingredients.set(i, ItemStack.EMPTY);
                BlockEntityUtil.sendUpdatePacket(this);
                return;
            }
        }
    }

    public void startBlending()
    {
        if(this.blending || this.level == null)
            return;

        this.findDrink().ifPresent(drink -> {
            this.drink = drink;
            this.blending = true;
            this.progress = 0;
            BlockEntityUtil.sendUpdatePacket(this);
            this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
        });
    }

    /**
     * Takes one of the drinks.
     */
    public ItemStack pourDrink()
    {
        if(this.drinkCount <= 0)
            return ItemStack.EMPTY;

        ItemStack poured = this.drink.copyWithCount(1);
        if(--this.drinkCount == 0)
        {
            this.drink = ItemStack.EMPTY;
        }
        BlockEntityUtil.sendUpdatePacket(this);
        return poured;
    }

    public boolean isFull()
    {
        return this.ingredients.stream().noneMatch(ItemStack::isEmpty);
    }

    public boolean hasDrinks()
    {
        return this.drinkCount > 0;
    }

    public int getDrinkCount()
    {
        return this.drinkCount;
    }

    public ItemStack getDrink()
    {
        return this.drink;
    }

    public NonNullList<ItemStack> getIngredients()
    {
        return this.ingredients;
    }

    public int getProgress()
    {
        return this.progress;
    }

    @Override
    public boolean isRunning()
    {
        return this.blending;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        if(this.level != null)
        {
            Containers.dropContents(this.level, pos, this.ingredients);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.ingredients = NonNullList.withSize(BlendingRecipe.Input.SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.ingredients);
        this.blending = input.getBooleanOr("Blending", false);
        this.progress = input.getIntOr("Progress", 0);
        this.drinkCount = input.getIntOr("DrinkCount", 0);
        this.drink = input.read("Drink", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.ingredients);
        output.putBoolean("Blending", this.blending);
        output.putInt("Progress", this.progress);
        output.putInt("DrinkCount", this.drinkCount);
        output.store("Drink", ItemStack.OPTIONAL_CODEC, this.drink);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
