package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.inventory.container.MicrowaveMenu;
import com.mrcrayfish.furniture.item.crafting.MicrowaveHeatingRecipe;
import com.mrcrayfish.furniture.util.RecipeLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * A microwave (1.12.2): Start heats the one item in it, and it pings when it is done. Hoppers can only take out what has been heated.
 *
 * Author: MrCrayfish
 */
public class MicrowaveBlockEntity extends ApplianceContainerBlockEntity
{
    public static final int BUTTON_START_STOP = 0;

    private boolean cooking;
    private int progress;
    private int progressTotal;

    private final ContainerData data = new ContainerData()
    {
        @Override
        public int get(int index)
        {
            return switch(index)
            {
                case 0 -> MicrowaveBlockEntity.this.progress;
                case 1 -> MicrowaveBlockEntity.this.progressTotal;
                case 2 -> MicrowaveBlockEntity.this.cooking ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value)
        {
            switch(index)
            {
                case 0 -> MicrowaveBlockEntity.this.progress = value;
                case 1 -> MicrowaveBlockEntity.this.progressTotal = value;
                case 2 -> MicrowaveBlockEntity.this.cooking = value != 0;
            }
        }

        @Override
        public int getCount()
        {
            return 3;
        }
    };

    public MicrowaveBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.MICROWAVE.get(), pos, state);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, MicrowaveBlockEntity microwave)
    {
        if(microwave.cooking)
        {
            RandomSource random = level.getRandom();
            level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.35 + random.nextDouble() / 3, pos.getY() + 0.065, pos.getZ() + 0.35 + random.nextDouble() / 3, 0.0, 0.0, 0.0);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MicrowaveBlockEntity microwave)
    {
        if(!microwave.cooking)
            return;

        RecipeHolder<MicrowaveHeatingRecipe> recipe = microwave.findRecipe(microwave.items.get(0)).orElse(null);
        if(recipe == null)
        {
            microwave.stop();
            return;
        }

        if(microwave.progress % 20 == 0)
        {
            level.playSound(null, pos, ModSounds.BLOCK_MICROWAVE_RUNNING.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
        }

        if(++microwave.progress >= microwave.progressTotal)
        {
            microwave.items.set(0, recipe.value().assemble(new SingleRecipeInput(microwave.items.get(0))));
            level.playSound(null, pos, ModSounds.BLOCK_MICROWAVE_FINISH.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
            microwave.stop();
        }
    }

    private Optional<RecipeHolder<MicrowaveHeatingRecipe>> findRecipe(ItemStack stack)
    {
        if(this.level == null || stack.isEmpty())
            return Optional.empty();
        return RecipeLookup.find(this.level, ModRecipeTypes.MICROWAVE_HEATING.get(), stack);
    }

    public void start()
    {
        if(this.cooking)
            return;

        this.findRecipe(this.items.get(0)).ifPresent(recipe -> {
            this.cooking = true;
            this.progress = 0;
            this.progressTotal = recipe.value().cookingTime();
            this.onRunningChanged();
        });
    }

    public void stop()
    {
        if(!this.cooking)
            return;

        this.cooking = false;
        this.progress = 0;
        this.onRunningChanged();
    }

    @Override
    public boolean isRunning()
    {
        return this.cooking;
    }

    public ContainerData getMicrowaveData()
    {
        return this.data;
    }

    @Override
    public int getContainerSize()
    {
        return 1;
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    @Override
    public void setItem(int index, ItemStack stack)
    {
        super.setItem(index, stack);
        this.stop();
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.microwave");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return new MicrowaveMenu(windowId, playerInventory, this);
    }

    @Override
    public boolean isMatchingContainerMenu(AbstractContainerMenu menu)
    {
        return menu instanceof MicrowaveMenu microwaveMenu && microwaveMenu.getBlockEntity() == this;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack)
    {
        return this.findRecipe(stack).isPresent();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        return !this.cooking && this.canPlaceItem(index, stack);
    }

    // Hoppers only take what is done
    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return !this.cooking && this.findRecipe(stack).isEmpty();
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.cooking = input.getBooleanOr("Cooking", false);
        this.progress = input.getIntOr("Progress", 0);
        this.progressTotal = input.getIntOr("ProgressTotal", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putBoolean("Cooking", this.cooking);
        output.putInt("Progress", this.progress);
        output.putInt("ProgressTotal", this.progressTotal);
    }
}
