package com.mrcrayfish.furniture.tileentity;

import com.mojang.serialization.Codec;
import com.mrcrayfish.furniture.block.FreezerBlock;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.event.FreezerFuelTimeEvent;
import com.mrcrayfish.furniture.inventory.container.FreezerMenu;
import com.mrcrayfish.furniture.item.crafting.FreezerSolidifyRecipe;
import com.mrcrayfish.furniture.util.RecipeLookup;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.jspecify.annotations.Nullable;

import java.util.Map;

/**
 * The freezer under a fridge: freezes items with ice as fuel, like a furnace.
 *
 * Author: MrCrayfish
 */
public class FreezerBlockEntity extends BasicLootBlockEntity
{
    private static final int[] SLOTS_SOURCE = new int[]{0};
    private static final int[] SLOTS_FUEL = new int[]{1};
    private static final int[] SLOTS_RESULT = new int[]{2};
    private static final Codec<Map<ResourceKey<Recipe<?>>, Integer>> RECIPES_USED_CODEC = Codec.unboundedMap(Recipe.KEY_CODEC, Codec.INT);

    private int fuelTime;
    private int fuelTimeTotal;
    private int freezeTime;
    private int freezeTimeTotal;

    protected final SimpleContainerData freezerData = new SimpleContainerData(4)
    {
        @Override
        public int get(int index)
        {
            return switch(index)
            {
                case 0 -> FreezerBlockEntity.this.fuelTime;
                case 1 -> FreezerBlockEntity.this.fuelTimeTotal;
                case 2 -> FreezerBlockEntity.this.freezeTime;
                case 3 -> FreezerBlockEntity.this.freezeTimeTotal;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value)
        {
            switch(index)
            {
                case 0 -> FreezerBlockEntity.this.fuelTime = value;
                case 1 -> FreezerBlockEntity.this.fuelTimeTotal = value;
                case 2 -> FreezerBlockEntity.this.freezeTime = value;
                case 3 -> FreezerBlockEntity.this.freezeTimeTotal = value;
            }
        }
    };

    private final Reference2IntOpenHashMap<ResourceKey<Recipe<?>>> usedRecipeCount = new Reference2IntOpenHashMap<>();

    protected FreezerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public FreezerBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.FREEZER.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FreezerBlockEntity blockEntity)
    {
        boolean shouldMarkDirty = false;
        if(blockEntity.isFreezing())
        {
            --blockEntity.fuelTime;
        }

        ItemStack fuelStack = blockEntity.items.get(1);
        if(blockEntity.isFreezing() || !fuelStack.isEmpty() && !blockEntity.items.get(0).isEmpty())
        {
            RecipeHolder<FreezerSolidifyRecipe> recipe = blockEntity.findRecipe(blockEntity.items.get(0));
            if(!blockEntity.isFreezing() && blockEntity.canFreeze(recipe))
            {
                blockEntity.fuelTime = blockEntity.getFreezeTime(fuelStack);
                blockEntity.fuelTimeTotal = blockEntity.fuelTime;
                if(blockEntity.isFreezing())
                {
                    shouldMarkDirty = true;
                    blockEntity.consumeFuel(fuelStack);
                }
            }

            if(blockEntity.isFreezing() && blockEntity.canFreeze(recipe))
            {
                ++blockEntity.freezeTime;
                if(blockEntity.freezeTime == blockEntity.freezeTimeTotal)
                {
                    blockEntity.freezeTime = 0;
                    blockEntity.freezeTimeTotal = blockEntity.getFreezeTime();
                    blockEntity.freeze(recipe);
                    shouldMarkDirty = true;
                }
            }
            else
            {
                blockEntity.freezeTime = 0;
            }
        }
        else if(blockEntity.freezeTime > 0)
        {
            blockEntity.freezeTime = Mth.clamp(blockEntity.freezeTime - 2, 0, blockEntity.freezeTimeTotal);
        }

        if(shouldMarkDirty)
        {
            blockEntity.setChanged();
        }
    }

    private void consumeFuel(ItemStack fuel)
    {
        ItemStackTemplate remainder = fuel.getCraftingRemainder();
        ItemStack newFuel = fuel;
        fuel.shrink(1);
        if(remainder != null)
        {
            if(fuel.isEmpty())
            {
                newFuel = remainder.create();
            }
            else if(this.level != null)
            {
                Containers.dropItemStack(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), remainder.create());
            }
        }
        this.items.set(1, newFuel);
    }

    @Nullable
    private RecipeHolder<FreezerSolidifyRecipe> findRecipe(ItemStack stack)
    {
        if(this.level == null || stack.isEmpty())
            return null;
        return RecipeLookup.find(this.level, ModRecipeTypes.FREEZER_SOLIDIFY.get(), stack).orElse(null);
    }

    private boolean isFreezing()
    {
        return this.fuelTime > 0;
    }

    public int getFreezeTime(ItemStack stack)
    {
        if(stack.isEmpty())
        {
            return 0;
        }
        else if(stack.is(Items.ICE))
        {
            return 2000;
        }
        else if(stack.is(Items.BLUE_ICE))
        {
            return 162000;
        }
        else if(stack.is(Items.PACKED_ICE))
        {
            return 18000;
        }
        FreezerFuelTimeEvent event = new FreezerFuelTimeEvent(stack);
        NeoForge.EVENT_BUS.post(event);
        return event.getFuelTime();
    }

    private ItemStack getResult(RecipeHolder<FreezerSolidifyRecipe> recipe)
    {
        return recipe.value().assemble(new SingleRecipeInput(this.items.get(0)));
    }

    private boolean canFreeze(@Nullable RecipeHolder<FreezerSolidifyRecipe> recipe)
    {
        if(!this.items.get(0).isEmpty() && recipe != null)
        {
            ItemStack outputStack = this.getResult(recipe);
            if(outputStack.isEmpty())
            {
                return false;
            }

            ItemStack resultStack = this.items.get(2);
            if(resultStack.isEmpty())
            {
                return true;
            }
            else if(!ItemStack.isSameItem(resultStack, outputStack))
            {
                return false;
            }
            else if(resultStack.getCount() + outputStack.getCount() <= this.getMaxStackSize() && resultStack.getCount() + outputStack.getCount() <= resultStack.getMaxStackSize())
            {
                return true;
            }
            return resultStack.getCount() + outputStack.getCount() <= outputStack.getMaxStackSize();
        }
        return false;
    }

    private void freeze(@Nullable RecipeHolder<FreezerSolidifyRecipe> recipe)
    {
        if(recipe != null && this.canFreeze(recipe))
        {
            ItemStack sourceStack = this.items.get(0);
            ItemStack outputStack = this.getResult(recipe);
            ItemStack resultStack = this.items.get(2);
            if(resultStack.isEmpty())
            {
                this.items.set(2, outputStack.copy());
            }
            else if(resultStack.is(outputStack.getItem()))
            {
                resultStack.grow(outputStack.getCount());
            }

            this.usedRecipeCount.addTo(recipe.id(), 1);

            ItemStackTemplate remainder = sourceStack.getCraftingRemainder();
            if(remainder != null)
            {
                this.items.set(0, remainder.create());
            }
            else
            {
                sourceStack.shrink(1);
            }
        }
    }

    @Override
    public int getContainerSize()
    {
        return 3;
    }

    @Override
    public void setItem(int index, ItemStack stack)
    {
        super.setItem(index, stack);
        if(index == 0)
        {
            this.freezeTimeTotal = this.getFreezeTime();
            this.freezeTime = 0;
            this.setChanged();
        }
    }

    protected int getFreezeTime()
    {
        RecipeHolder<FreezerSolidifyRecipe> recipe = this.findRecipe(this.items.get(0));
        return recipe != null ? recipe.value().cookingTime() : 300;
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.freezer");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return new FreezerMenu(windowId, playerInventory, this);
    }

    @Override
    public boolean isMatchingContainerMenu(AbstractContainerMenu menu)
    {
        return menu instanceof FreezerMenu freezerMenu && freezerMenu.getBlockEntity() == this;
    }

    @Override
    public int[] getSlotsForFace(Direction direction)
    {
        return switch(direction)
        {
            case UP -> SLOTS_SOURCE;
            case DOWN -> SLOTS_RESULT;
            default -> SLOTS_FUEL;
        };
    }

    /**
     * Gives the player the experience of everything frozen since it was last taken out.
     */
    public void spawnExperience(Player player)
    {
        if(player.level() instanceof ServerLevel level)
        {
            for(Map.Entry<ResourceKey<Recipe<?>>, Integer> entry : this.usedRecipeCount.entrySet())
            {
                level.recipeAccess().byKey(entry.getKey()).ifPresent(recipe ->
                {
                    if(recipe.value() instanceof FreezerSolidifyRecipe freezerRecipe)
                    {
                        spawnExperienceOrbs(level, player.position(), entry.getValue(), freezerRecipe.experience());
                    }
                });
            }
            this.usedRecipeCount.clear();
        }
    }

    private static void spawnExperienceOrbs(ServerLevel level, Vec3 pos, int count, float exp)
    {
        int xpReward = Mth.floor(count * exp);
        float xpFraction = Mth.frac(count * exp);
        if(xpFraction != 0.0F && level.getRandom().nextFloat() < xpFraction)
        {
            xpReward++;
        }
        ExperienceOrb.award(level, pos, xpReward);
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return index == 2;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack)
    {
        if(index == 2)
        {
            return false;
        }
        else if(index != 1)
        {
            return true;
        }
        return this.getFreezeTime(stack) > 0;
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.freezeTime = input.getIntOr("FreezeTime", 0);
        this.freezeTimeTotal = input.getIntOr("FreezeTimeTotal", 0);
        this.fuelTime = input.getIntOr("FuelTime", 0);
        this.fuelTimeTotal = this.getFreezeTime(this.items.get(1));
        this.usedRecipeCount.clear();
        this.usedRecipeCount.putAll(input.read("RecipesUsed", RECIPES_USED_CODEC).orElse(Map.of()));
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putInt("FreezeTime", this.freezeTime);
        output.putInt("FreezeTimeTotal", this.freezeTimeTotal);
        output.putInt("FuelTime", this.fuelTime);
        output.store("RecipesUsed", RECIPES_USED_CODEC, this.usedRecipeCount);
    }

    public ContainerData getFreezerData()
    {
        return this.freezerData;
    }

    @Override
    public void onOpen(Level level, BlockPos pos, BlockState state)
    {
        this.playDoorSound(state, ModSounds.BLOCK_FRIDGE_OPEN.get());
        this.setDoorState(state, true);
    }

    @Override
    public void onClose(Level level, BlockPos pos, BlockState state)
    {
        this.playDoorSound(state, ModSounds.BLOCK_FRIDGE_CLOSE.get());
        this.setDoorState(state, false);
    }

    private void playDoorSound(BlockState state, SoundEvent event)
    {
        Vec3i directionVec = state.getValue(FreezerBlock.DIRECTION).getOpposite().getUnitVec3i();
        double x = this.worldPosition.getX() + 0.5D + directionVec.getX() / 2.0D;
        double y = this.worldPosition.getY() + 0.5D + directionVec.getY() / 2.0D;
        double z = this.worldPosition.getZ() + 0.5D + directionVec.getZ() / 2.0D;
        Level level = this.getLevel();
        if(level != null)
        {
            level.playSound(null, x, y, z, event, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private void setDoorState(BlockState state, boolean open)
    {
        Level level = this.getLevel();
        if(level != null)
        {
            level.setBlock(this.getBlockPos(), state.setValue(FreezerBlock.OPEN, open), 3);
        }
    }
}
