package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.item.crafting.GrillCookingRecipe;
import com.mrcrayfish.furniture.network.message.S2CMessageFlipGrill;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import com.mrcrayfish.furniture.util.FuelHelper;
import com.mrcrayfish.furniture.util.RecipeLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public class GrillBlockEntity extends BlockEntity implements WorldlyContainer
{
    /* Used for animations on client only */
    public static final int MAX_FLIPPING_COUNTER = 15;
    public static final int[] ALL_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    public static final int[] GRILL_SLOTS = new int[]{9, 10, 11, 12};

    private final NonNullList<ItemStack> fuel = NonNullList.withSize(9, ItemStack.EMPTY);
    private final NonNullList<ItemStack> grill = NonNullList.withSize(4, ItemStack.EMPTY);
    private final int[] cookingTimes = new int[4];
    private final int[] cookingTotalTimes = new int[4];
    private final boolean[] flipped = new boolean[4];
    private final float[] experience = new float[4];
    private final byte[] rotations = new byte[4];
    private int remainingFuel = 0;

    /* Used for animations on client only */
    private final boolean[] flipping = new boolean[4];
    private final int[] flippingCounter = new int[4];

    protected GrillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public GrillBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.GRILL.get(), pos, state);
    }

    public void setFlipping(int position)
    {
        this.flipping[position] = true;
        this.flippingCounter[position] = 0;
    }

    public boolean isFlipping(int position)
    {
        return this.flipping[position];
    }

    public int getFlippingCount(int position)
    {
        return this.flippingCounter[position];
    }

    public NonNullList<ItemStack> getGrill()
    {
        return this.grill;
    }

    public NonNullList<ItemStack> getFuel()
    {
        return this.fuel;
    }

    public byte[] getRotations()
    {
        return this.rotations;
    }

    public boolean isFlipped(int position)
    {
        return this.flipped[position];
    }

    public boolean addItem(ItemStack stack, int position, int cookTime, float experience, byte rotation)
    {
        if(this.grill.get(position).isEmpty())
        {
            ItemStack copy = stack.copy();
            copy.setCount(1);
            this.grill.set(position, copy);
            this.resetPosition(position, cookTime, experience, rotation);

            /* Play place sound */
            Level level = this.getLevel();
            if(level != null)
            {
                level.playSound(null, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.0, this.worldPosition.getZ() + 0.5, ModSounds.BLOCK_GRILL_PLACE.get(), SoundSource.BLOCKS, 0.75F, level.getRandom().nextFloat() * 0.2F + 0.9F);
            }

            return true;
        }
        return false;
    }

    private void resetPosition(int position, int cookTime, float experience, byte rotation)
    {
        this.cookingTimes[position] = 0;
        this.cookingTotalTimes[position] = cookTime / 2; //Half the time because it has to cook both sides
        this.flipped[position] = false;
        this.experience[position] = experience;
        this.rotations[position] = rotation;

        /* Send updates to client */

        BlockEntityUtil.sendUpdatePacket(this);
    }

    public boolean addFuel(ItemStack stack)
    {
        for(int i = 0; i < this.fuel.size(); i++)
        {
            if(this.fuel.get(i).isEmpty())
            {
                ItemStack fuel = stack.copy();
                fuel.setCount(1);
                this.fuel.set(i, fuel);

                /* Send updates to client */

                BlockEntityUtil.sendUpdatePacket(this);

                return true;
            }
        }
        return false;
    }

    public void flipItem(int position)
    {
        if(!this.grill.get(position).isEmpty())
        {
            if(!this.flipped[position] && this.cookingTimes[position] == this.cookingTotalTimes[position])
            {
                this.flipped[position] = true;
                this.cookingTimes[position] = 0;

                /* Sends a packet to players tracking the chunk the Grill is in indicating that animation should play */
                if(this.level instanceof ServerLevel serverLevel)
                {
                    PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(this.worldPosition), new S2CMessageFlipGrill(this.worldPosition, position));
                }

                /* Send updates to client */

                BlockEntityUtil.sendUpdatePacket(this);

                /* Play flip sound */
                Level level = this.getLevel();
                if(level != null)
                {
                    level.playSound(null, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.0, this.worldPosition.getZ() + 0.5, ModSounds.BLOCK_GRILL_FLIP.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
                }
            }
            else if(this.flipped[position] && this.cookingTimes[position] == this.cookingTotalTimes[position])
            {
                this.removeItem(position);
            }
        }
    }

    public void flipItems()
    {
        for(int i = 0; i < 4; i++)
        {
            if(!this.grill.get(i).isEmpty())
            {
                if(!this.flipped[i] && this.cookingTimes[i] == this.cookingTotalTimes[i])
                {
                    this.flipItem(i);
                    return;
                }
            }
        }
    }

    public void removeItem(int position)
    {
        if(!this.grill.get(position).isEmpty())
        {
            double posX = worldPosition.getX() + 0.3 + 0.4 * (position % 2);
            double posY = worldPosition.getY() + 1.0;
            double posZ = worldPosition.getZ() + 0.3 + 0.4 * (position / 2);

            /* Spawns the item */
            ItemEntity entity = new ItemEntity(this.level, posX, posY + 0.1, posZ, this.grill.get(position).copy());
            this.level.addFreshEntity(entity);

            /* Remove the item from the inventory */
            this.grill.set(position, ItemStack.EMPTY);

            /* Spawn experience orbs */
            if(this.flipped[position] && this.cookingTimes[position] == this.cookingTotalTimes[position])
            {
                int amount = (int) experience[position];
                while(amount > 0)
                {
                    int splitAmount = ExperienceOrb.getExperienceValue(amount);
                    amount -= splitAmount;
                    this.level.addFreshEntity(new ExperienceOrb(this.level, posX, posY, posZ, splitAmount));
                }
            }

            /* Send updates to client */

            BlockEntityUtil.sendUpdatePacket(this);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GrillBlockEntity blockEntity)
    {
        boolean canCook = blockEntity.canCook();
        if(blockEntity.remainingFuel == 0 && canCook)
        {
            for(int i = blockEntity.fuel.size() - 1; i >= 0; i--)
            {
                if(!blockEntity.fuel.get(i).isEmpty())
                {
                    blockEntity.remainingFuel = level instanceof ServerLevel serverLevel ? FuelHelper.getBurnTime(serverLevel, blockEntity, blockEntity.fuel.get(i)) : 0;
                    blockEntity.fuel.set(i, ItemStack.EMPTY);

                    /* Send updates to client */

                    BlockEntityUtil.sendUpdatePacket(blockEntity);
                    break;
                }
            }
        }

        if(canCook && blockEntity.remainingFuel > 0)
        {
            blockEntity.cookItems();
            blockEntity.remainingFuel--;
            if(blockEntity.remainingFuel == 0)
            {
                /* Send updates to client */
                BlockEntityUtil.sendUpdatePacket(blockEntity);
            }
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, GrillBlockEntity blockEntity)
    {
        blockEntity.spawnParticles();

        for(int i = 0; i < blockEntity.flipping.length; i++)
        {
            if(blockEntity.flipping[i] && blockEntity.flippingCounter[i] < MAX_FLIPPING_COUNTER)
            {
                blockEntity.flippingCounter[i]++;
                if(blockEntity.flippingCounter[i] == MAX_FLIPPING_COUNTER)
                {
                    blockEntity.flipping[i] = false;
                }
            }
        }
    }

    private boolean canCook()
    {
        for(int i = 0; i < this.grill.size(); i++)
        {
            if(!this.grill.get(i).isEmpty() && this.cookingTimes[i] != this.cookingTotalTimes[i])
            {
                return true;
            }
        }
        return false;
    }

    private void cookItems()
    {
        boolean itemsChanged = false;
        for(int i = 0; i < this.grill.size(); i++)
        {
            if(!this.grill.get(i).isEmpty())
            {
                if(this.cookingTimes[i] < this.cookingTotalTimes[i])
                {
                    this.cookingTimes[i]++;
                    if(this.cookingTimes[i] == this.cookingTotalTimes[i])
                    {
                        /* Set to result on cooked and flipped */
                        if(this.flipped[i])
                        {
                            ItemStack raw = this.grill.get(i);
                            Optional<RecipeHolder<GrillCookingRecipe>> optional = this.findRecipe(raw);
                            if(optional.isPresent())
                            {
                                this.grill.set(i, optional.get().value().assemble(new SingleRecipeInput(raw)));
                            }
                        }
                        itemsChanged = true;
                    }
                }
            }
        }
        if(itemsChanged)
        {
            /* Send updates to client */
            BlockEntityUtil.sendUpdatePacket(this);
        }
    }

    private void spawnParticles()
    {
        Level level = this.getLevel();
        if(level != null)
        {
            if(this.isCooking() && this.remainingFuel > 0)
            {
                double posX = worldPosition.getX() + 0.2 + 0.6 * level.getRandom().nextDouble();
                double posY = worldPosition.getY() + 0.85;
                double posZ = worldPosition.getZ() + 0.2 + 0.6 * level.getRandom().nextDouble();
                level.addParticle(ParticleTypes.FLAME, posX, posY, posZ, 0.0, 0.0, 0.0);
            }

            BlockPos pos = this.getBlockPos();
            for(int i = 0; i < this.grill.size(); i++)
            {
                if(!this.grill.get(i).isEmpty() && level.getRandom().nextFloat() < 0.1F)
                {
                    double posX = pos.getX() + 0.3 + 0.4 * (i % 2);
                    double posY = pos.getY() + 1.0;
                    double posZ = pos.getZ() + 0.3 + 0.4 * (i / 2);
                    if(!this.flipped[i] && this.cookingTimes[i] == this.cookingTotalTimes[i])
                    {
                        for(int j = 0; j < 4; j++)
                        {
                            level.addParticle(ParticleTypes.SMOKE, posX, posY, posZ, 0.0D, 5.0E-4D, 0.0D);
                        }
                    }
                }
            }
        }
    }

    private boolean isCooking()
    {
        for(int i = 0; i < this.grill.size(); i++)
        {
            if(!this.grill.get(i).isEmpty() && (this.cookingTimes[i] != this.cookingTotalTimes[i] || !this.flipped[i]))
            {
                return true;
            }
        }
        return false;
    }

    public Optional<RecipeHolder<GrillCookingRecipe>> findMatchingRecipe(ItemStack input)
    {
        return this.grill.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.findRecipe(input);
    }

    private Optional<RecipeHolder<GrillCookingRecipe>> findRecipe(ItemStack input)
    {
        if(this.level == null || input.isEmpty())
            return Optional.empty();
        return RecipeLookup.find(this.level, ModRecipeTypes.GRILL_COOKING.get(), input);
    }

    @Override
    public int getContainerSize()
    {
        return this.fuel.size() + this.grill.size();
    }

    @Override
    public boolean isEmpty()
    {
        for(ItemStack stack : this.fuel)
        {
            if(!stack.isEmpty())
            {
                return false;
            }
        }
        for(ItemStack stack : this.grill)
        {
            if(!stack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index)
    {
        if(index - this.fuel.size() >= 0)
        {
            return this.grill.get(index - this.fuel.size());
        }
        return this.fuel.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count)
    {
        if(index - this.fuel.size() >= 0)
        {
            index -= this.fuel.size();
            ItemStack result = ContainerHelper.removeItem(this.grill, index, count);

            if(this.grill.get(index).isEmpty())
            {
                if(this.flipped[index] && this.cookingTimes[index] == this.cookingTotalTimes[index])
                {
                    double posX = worldPosition.getX() + 0.3 + 0.4 * (index % 2);
                    double posY = worldPosition.getY() + 1.0;
                    double posZ = worldPosition.getZ() + 0.3 + 0.4 * (index / 2);
                    int amount = (int) experience[index];
                    while(amount > 0)
                    {
                        int splitAmount = ExperienceOrb.getExperienceValue(amount);
                        amount -= splitAmount;
                        this.level.addFreshEntity(new ExperienceOrb(this.level, posX, posY, posZ, splitAmount));
                    }
                }
            }

            /* Send updates to client */

            BlockEntityUtil.sendUpdatePacket(this);

            return result;
        }

        ItemStack result = ContainerHelper.removeItem(this.fuel, index, count);

        /* Send updates to client */

        BlockEntityUtil.sendUpdatePacket(this);

        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        if(index - this.fuel.size() >= 0)
        {
            return ContainerHelper.takeItem(this.grill, index - this.fuel.size());
        }
        return ContainerHelper.takeItem(this.fuel, index);
    }

    @Override
    public void setItem(int index, ItemStack stack)
    {
        NonNullList<ItemStack> inventory = this.fuel;
        if(index - this.fuel.size() >= 0)
        {
            index -= this.fuel.size();
            inventory = this.grill;
            int finalIndex = index;
            Optional<RecipeHolder<GrillCookingRecipe>> optional = this.findRecipe(stack);
            if(optional.isPresent())
            {
                GrillCookingRecipe recipe = optional.get().value();
                this.resetPosition(finalIndex, recipe.cookingTime(), recipe.experience(), (byte) 0);
            }
        }
        inventory.set(index, stack);
        if(stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }

        /* Send updates to client */

        BlockEntityUtil.sendUpdatePacket(this);
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    @Override
    public void clearContent()
    {
        this.fuel.clear();
        this.grill.clear();
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.grill.clear();
        loadItems(input, "Grill", this.grill);
        this.fuel.clear();
        loadItems(input, "Fuel", this.fuel);
        this.remainingFuel = input.getIntOr("RemainingFuel", 0);
        input.getIntArray("CookingTimes").ifPresent(times -> System.arraycopy(times, 0, this.cookingTimes, 0, Math.min(this.cookingTimes.length, times.length)));
        input.getIntArray("CookingTotalTimes").ifPresent(times -> System.arraycopy(times, 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, times.length)));
        input.getIntArray("Flipped").ifPresent(flipped -> {
            for(int i = 0; i < Math.min(this.flipped.length, flipped.length); i++)
            {
                this.flipped[i] = flipped[i] == 1;
            }
        });
        input.getIntArray("Experience").ifPresent(experience -> {
            for(int i = 0; i < Math.min(this.experience.length, experience.length); i++)
            {
                this.experience[i] = Float.intBitsToFloat(experience[i]);
            }
        });
        input.getIntArray("Rotations").ifPresent(rotations -> {
            for(int i = 0; i < Math.min(this.rotations.length, rotations.length); i++)
            {
                this.rotations[i] = (byte) rotations[i];
            }
        });
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        saveItems(output, "Grill", this.grill);
        saveItems(output, "Fuel", this.fuel);
        output.putInt("RemainingFuel", this.remainingFuel);
        output.putIntArray("CookingTimes", this.cookingTimes);
        output.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
        int[] flipped = new int[this.flipped.length];
        for(int i = 0; i < this.flipped.length; i++)
        {
            flipped[i] = this.flipped[i] ? 1 : 0;
        }
        output.putIntArray("Flipped", flipped);
        int[] experience = new int[this.experience.length];
        for(int i = 0; i < this.experience.length; i++)
        {
            experience[i] = Float.floatToIntBits(this.experience[i]);
        }
        output.putIntArray("Experience", experience);
        int[] rotations = new int[this.rotations.length];
        for(int i = 0; i < this.rotations.length; i++)
        {
            rotations[i] = this.rotations[i];
        }
        output.putIntArray("Rotations", rotations);
    }

    private static void saveItems(ValueOutput output, String key, NonNullList<ItemStack> items)
    {
        ValueOutput.TypedOutputList<ItemStackWithSlot> list = output.list(key, ItemStackWithSlot.CODEC);
        for(int i = 0; i < items.size(); i++)
        {
            if(!items.get(i).isEmpty())
            {
                list.add(new ItemStackWithSlot(i, items.get(i)));
            }
        }
    }

    private static void loadItems(ValueInput input, String key, NonNullList<ItemStack> items)
    {
        for(ItemStackWithSlot item : input.listOrEmpty(key, ItemStackWithSlot.CODEC))
        {
            if(item.isValidInContainer(items.size()))
            {
                items.set(item.slot(), item.stack());
            }
        }
    }

    // The client draws the items cooking on the grill, so it gets everything
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

    @Override
    public boolean stillValid(Player player)
    {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return side == Direction.DOWN ? GRILL_SLOTS : ALL_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        if(!this.getItem(index).isEmpty())
        {
            return false;
        }
        if(index - this.fuel.size() >= 0)
        {
            return this.findRecipe(stack).isPresent();
        }
        return stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        if(direction == Direction.DOWN)
        {
            if(index - this.fuel.size() >= 0)
            {
                index -= this.fuel.size();
                if(this.flipped[index] && this.cookingTimes[index] == this.cookingTotalTimes[index])
                {
                    return this.findRecipe(stack).isEmpty();
                }
            }
        }
        return false;
    }
}
