package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.stream.IntStream;

/**
 * The dishwasher and washing machine (1.12.2): a bucket of soapy water runs it for 5000 ticks, and while it runs it repairs a point of
 * damage on each item in it every 50 ticks (every 20 with super soapy water). Start and Stop are buttons on its screen. It stops by
 * itself when everything in it is repaired, or when the soap runs out and there is no more soapy water.
 *
 * Author: MrCrayfish
 */
public abstract class WasherBlockEntity extends ApplianceContainerBlockEntity
{
    public static final int BUTTON_START_STOP = 0;
    public static final int SOAP_TIME = 5000;

    private final int cleanSlots;
    private final int[] slotsTop;
    private final int[] slotsBottom;
    private final int[] slotsSide;

    private boolean washing;
    private boolean superMode;
    private int progress;
    private int timeRemaining;

    private final ContainerData data = new ContainerData()
    {
        @Override
        public int get(int index)
        {
            return switch(index)
            {
                case 0 -> WasherBlockEntity.this.progress;
                case 1 -> WasherBlockEntity.this.timeRemaining;
                case 2 -> WasherBlockEntity.this.washing ? 1 : 0;
                case 3 -> WasherBlockEntity.this.superMode ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value)
        {
            switch(index)
            {
                case 0 -> WasherBlockEntity.this.progress = value;
                case 1 -> WasherBlockEntity.this.timeRemaining = value;
                case 2 -> WasherBlockEntity.this.washing = value != 0;
                case 3 -> WasherBlockEntity.this.superMode = value != 0;
            }
        }

        @Override
        public int getCount()
        {
            return 4;
        }
    };

    /**
     * @param cleanSlots how many items it holds; the soapy water slot comes after them
     */
    protected WasherBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int cleanSlots)
    {
        super(type, pos, state);
        this.cleanSlots = cleanSlots;
        // The super constructor sized the inventory before cleanSlots was set
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.slotsTop = IntStream.range(0, cleanSlots).toArray();
        this.slotsBottom = IntStream.rangeClosed(0, cleanSlots).toArray();
        this.slotsSide = new int[]{cleanSlots};
    }

    protected abstract TagKey<Item> cleanable();

    protected abstract SoundEvent washingSound();

    /**
     * The slot an item goes in (tools by kind in the dishwasher, armour by where it is worn in the washing machine), or -1 for any.
     */
    protected abstract int slotFor(ItemStack stack);

    // The client counts along so the washing machine drum turns
    public static void clientTick(Level level, BlockPos pos, BlockState state, WasherBlockEntity washer)
    {
        if(washer.washing)
        {
            washer.progress++;
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, WasherBlockEntity washer)
    {
        if(!washer.washing)
            return;

        if(!washer.canWash())
        {
            washer.stop();
            return;
        }

        if(washer.progress % (washer.superMode ? 20 : 50) == 0)
        {
            for(int i = 0; i < washer.cleanSlots; i++)
            {
                ItemStack stack = washer.items.get(i);
                if(stack.isDamaged())
                {
                    stack.setDamageValue(stack.getDamageValue() - 1);
                }
            }
            washer.setChanged();
        }

        if(--washer.timeRemaining <= 0 && !washer.useSoap())
        {
            washer.timeRemaining = 0;
            washer.stop();
            return;
        }

        if(washer.progress % 20 == 0)
        {
            level.playSound(null, pos, washer.washingSound(), SoundSource.BLOCKS, 0.75F, 1.0F);
        }
        washer.progress++;
    }

    public void start()
    {
        if(this.washing || !this.canWash())
            return;

        if(this.timeRemaining <= 0 && !this.useSoap())
            return;

        this.washing = true;
        this.progress = 0;
        this.onRunningChanged();
    }

    public void stop()
    {
        if(!this.washing)
            return;

        this.washing = false;
        this.progress = 0;
        this.onRunningChanged();
    }

    // Takes the soapy water for another 5000 ticks, leaving the bucket
    private boolean useSoap()
    {
        ItemStack soap = this.items.get(this.cleanSlots);
        if(!isSoap(soap))
            return false;

        this.superMode = soap.is(ModItems.SUPER_SOAPY_WATER.get());
        this.timeRemaining = SOAP_TIME;
        ItemStackTemplate remainder = soap.getCraftingRemainder();
        this.items.set(this.cleanSlots, remainder != null ? remainder.create() : ItemStack.EMPTY);
        this.setChanged();
        return true;
    }

    // Something in it to repair, and nothing it can't wash
    private boolean canWash()
    {
        boolean damaged = false;
        for(int i = 0; i < this.cleanSlots; i++)
        {
            ItemStack stack = this.items.get(i);
            if(stack.isEmpty())
                continue;
            if(!stack.is(this.cleanable()))
                return false;
            damaged |= stack.isDamaged();
        }
        return damaged && (this.timeRemaining > 0 || isSoap(this.items.get(this.cleanSlots)));
    }

    public static boolean isSoap(ItemStack stack)
    {
        return stack.is(ModItems.SOAPY_WATER.get()) || stack.is(ModItems.SUPER_SOAPY_WATER.get());
    }

    @Override
    public boolean isRunning()
    {
        return this.washing;
    }

    public int getProgress()
    {
        return this.progress;
    }

    public ContainerData getWasherData()
    {
        return this.data;
    }

    @Override
    public int getContainerSize()
    {
        return this.cleanSlots + 1;
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack)
    {
        if(index == this.cleanSlots)
            return isSoap(stack);
        if(!stack.is(this.cleanable()))
            return false;
        int slot = this.slotFor(stack);
        return slot == -1 || slot == index;
    }

    @Override
    public int[] getSlotsForFace(Direction direction)
    {
        return switch(direction)
        {
            case UP -> this.slotsTop;
            case DOWN -> this.slotsBottom;
            default -> this.slotsSide;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        return this.canPlaceItem(index, stack);
    }

    // Hoppers take out what is repaired, and the empty buckets
    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return direction == Direction.DOWN && !isSoap(stack) && !stack.isDamaged();
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.washing = input.getBooleanOr("Washing", false);
        this.superMode = input.getBooleanOr("SuperMode", false);
        this.progress = input.getIntOr("Progress", 0);
        this.timeRemaining = input.getIntOr("Remaining", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putBoolean("Washing", this.washing);
        output.putBoolean("SuperMode", this.superMode);
        output.putInt("Progress", this.progress);
        output.putInt("Remaining", this.timeRemaining);
    }
}
