package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.item.crafting.ToastingRecipe;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import com.mrcrayfish.furniture.util.RecipeLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;

/**
 * The two slices in a toaster and how far they have toasted. The toast pops out of the top when it is done.
 *
 * Author: MrCrayfish
 */
public class ToasterBlockEntity extends BlockEntity implements Appliance
{
    private NonNullList<ItemStack> slices = NonNullList.withSize(2, ItemStack.EMPTY);
    private boolean toasting;
    private int toastingTime;
    private int toastingTimeTotal;

    public ToasterBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.TOASTER.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ToasterBlockEntity toaster)
    {
        if(!toaster.toasting)
            return;

        if(++toaster.toastingTime < toaster.toastingTimeTotal)
            return;

        for(int i = 0; i < toaster.slices.size(); i++)
        {
            ItemStack slice = toaster.slices.get(i);
            toaster.findRecipe(slice).ifPresent(recipe -> toaster.popOut(recipe.value().assemble(new SingleRecipeInput(slice))));
            toaster.slices.set(i, ItemStack.EMPTY);
        }
        level.playSound(null, pos, ModSounds.BLOCK_TOASTER_UP.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
        toaster.toasting = false;
        toaster.toastingTime = 0;
        BlockEntityUtil.sendUpdatePacket(toaster);
        level.updateNeighbourForOutputSignal(pos, state.getBlock());
    }

    private Optional<RecipeHolder<ToastingRecipe>> findRecipe(ItemStack stack)
    {
        if(this.level == null || stack.isEmpty())
            return Optional.empty();
        return RecipeLookup.find(this.level, ModRecipeTypes.TOASTING.get(), stack);
    }

    // As long as the slowest slice takes
    private int getToastingTime()
    {
        return this.slices.stream().map(this::findRecipe).flatMap(Optional::stream).mapToInt(recipe -> recipe.value().cookingTime()).max().orElse(0);
    }

    public boolean canToast(ItemStack stack)
    {
        return this.slices.stream().anyMatch(ItemStack::isEmpty) && this.findRecipe(stack).isPresent();
    }

    public boolean addSlice(ItemStack stack)
    {
        for(int i = 0; i < this.slices.size(); i++)
        {
            if(this.slices.get(i).isEmpty())
            {
                this.slices.set(i, stack);
                BlockEntityUtil.sendUpdatePacket(this);
                return true;
            }
        }
        return false;
    }

    public void removeSlice()
    {
        for(int i = 0; i < this.slices.size(); i++)
        {
            ItemStack slice = this.slices.get(i);
            if(!slice.isEmpty())
            {
                this.popOut(slice);
                this.slices.set(i, ItemStack.EMPTY);
                BlockEntityUtil.sendUpdatePacket(this);
                return;
            }
        }
    }

    public void startToasting()
    {
        if(this.level == null || this.toasting)
            return;

        int time = this.getToastingTime();
        if(time <= 0)
            return;

        this.toasting = true;
        this.toastingTime = 0;
        this.toastingTimeTotal = time;
        this.level.playSound(null, this.worldPosition, ModSounds.BLOCK_TOASTER_DOWN.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
        BlockEntityUtil.sendUpdatePacket(this);
        this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
    }

    private void popOut(ItemStack stack)
    {
        if(this.level != null)
        {
            Containers.dropItemStack(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.6, this.worldPosition.getZ() + 0.5, stack);
        }
    }

    public NonNullList<ItemStack> getSlices()
    {
        return this.slices;
    }

    @Override
    public boolean isRunning()
    {
        return this.toasting;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        if(this.level != null)
        {
            Containers.dropContents(this.level, pos, this.slices);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.slices = NonNullList.withSize(2, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.slices);
        this.toasting = input.getBooleanOr("Toasting", false);
        this.toastingTime = input.getIntOr("ToastingTime", 0);
        this.toastingTimeTotal = input.getIntOr("ToastingTimeTotal", 200);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.slices);
        output.putBoolean("Toasting", this.toasting);
        output.putInt("ToastingTime", this.toastingTime);
        output.putInt("ToastingTimeTotal", this.toastingTimeTotal);
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
