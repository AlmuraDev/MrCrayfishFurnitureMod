package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import com.mrcrayfish.furniture.inventory.container.OvenMenu;
import com.mrcrayfish.furniture.item.crafting.OvenBakingRecipe;
import com.mrcrayfish.furniture.util.RecipeLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * An oven (1.12.2): four slots of food on top, each baking into the slot below it. It bakes the first one it can, then the next.
 *
 * Author: MrCrayfish
 */
public class OvenBlockEntity extends ApplianceContainerBlockEntity
{
    public static final int ROW = 4;
    private static final int[] SLOTS_INPUT = new int[]{0, 1, 2, 3};
    private static final int[] SLOTS_OUTPUT = new int[]{4, 5, 6, 7};

    private int cookingTime;
    private int cookingTimeTotal;
    // The input slot baking now, or -1
    private int cookingSlot = -1;

    private final ContainerData data = new ContainerData()
    {
        @Override
        public int get(int index)
        {
            return switch(index)
            {
                case 0 -> OvenBlockEntity.this.cookingTime;
                case 1 -> OvenBlockEntity.this.cookingTimeTotal;
                case 2 -> OvenBlockEntity.this.cookingSlot;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value)
        {
            switch(index)
            {
                case 0 -> OvenBlockEntity.this.cookingTime = value;
                case 1 -> OvenBlockEntity.this.cookingTimeTotal = value;
                case 2 -> OvenBlockEntity.this.cookingSlot = value;
            }
        }

        @Override
        public int getCount()
        {
            return 3;
        }
    };

    protected OvenBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public OvenBlockEntity(BlockPos pos, BlockState state)
    {
        this(ModBlockEntities.OVEN.get(), pos, state);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, OvenBlockEntity oven)
    {
        if(oven.isRunning())
        {
            RandomSource random = level.getRandom();
            level.addParticle(ParticleTypes.SMOKE, pos.getX() + random.nextDouble(), pos.getY() + 1.0, pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, OvenBlockEntity oven)
    {
        int slot = -1;
        RecipeHolder<OvenBakingRecipe> recipe = null;
        for(int i = 0; i < ROW; i++)
        {
            int input = i;
            recipe = oven.findRecipe(oven.items.get(i)).filter(r -> oven.canHold(input + ROW, oven.getResult(input, r))).orElse(null);
            if(recipe != null)
            {
                slot = i;
                break;
            }
        }

        if(slot != oven.cookingSlot)
        {
            oven.cookingSlot = slot;
            oven.cookingTime = 0;
            oven.cookingTimeTotal = recipe != null ? recipe.value().cookingTime() : 0;
            oven.onRunningChanged();
        }

        if(recipe != null && ++oven.cookingTime >= oven.cookingTimeTotal)
        {
            oven.bake(slot, recipe);
            oven.cookingTime = 0;
            oven.setChanged();
        }
    }

    private Optional<RecipeHolder<OvenBakingRecipe>> findRecipe(ItemStack stack)
    {
        if(this.level == null || stack.isEmpty())
            return Optional.empty();
        return RecipeLookup.find(this.level, ModRecipeTypes.OVEN_BAKING.get(), stack);
    }

    private ItemStack getResult(int slot, RecipeHolder<OvenBakingRecipe> recipe)
    {
        return recipe.value().assemble(new SingleRecipeInput(this.items.get(slot)));
    }

    // Whether the slot under the food has room for what it bakes into
    private boolean canHold(int slot, ItemStack result)
    {
        if(result.isEmpty())
            return false;
        ItemStack current = this.items.get(slot);
        return current.isEmpty() || ItemStack.isSameItemSameComponents(current, result) && current.getCount() + result.getCount() <= Math.min(this.getMaxStackSize(), current.getMaxStackSize());
    }

    private void bake(int slot, RecipeHolder<OvenBakingRecipe> recipe)
    {
        ItemStack food = this.items.get(slot);
        ItemStack result = this.getResult(slot, recipe);
        if(!this.canHold(slot + ROW, result))
            return;

        ItemStack current = this.items.get(slot + ROW);
        if(current.isEmpty())
        {
            this.items.set(slot + ROW, result);
        }
        else
        {
            current.grow(result.getCount());
        }

        ItemStackTemplate remainder = food.getCraftingRemainder();
        food.shrink(1);
        if(remainder != null && food.isEmpty())
        {
            this.items.set(slot, remainder.create());
        }
    }

    @Override
    public boolean isRunning()
    {
        return this.cookingSlot >= 0;
    }

    public ContainerData getOvenData()
    {
        return this.data;
    }

    @Override
    public int getContainerSize()
    {
        return ROW * 2;
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.oven");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return new OvenMenu(windowId, playerInventory, this);
    }

    @Override
    public boolean isMatchingContainerMenu(AbstractContainerMenu menu)
    {
        return menu instanceof OvenMenu ovenMenu && ovenMenu.getBlockEntity() == this;
    }

    @Override
    public int[] getSlotsForFace(Direction direction)
    {
        return direction == Direction.DOWN ? SLOTS_OUTPUT : SLOTS_INPUT;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack)
    {
        return index < ROW && this.findRecipe(stack).isPresent();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return index >= ROW;
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.cookingTime = input.getIntOr("CookingTime", 0);
        this.cookingTimeTotal = input.getIntOr("CookingTimeTotal", 0);
        this.cookingSlot = input.getIntOr("CookingSlot", -1);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putInt("CookingTime", this.cookingTime);
        output.putInt("CookingTimeTotal", this.cookingTimeTotal);
        output.putInt("CookingSlot", this.cookingSlot);
    }
}
