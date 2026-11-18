package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.common.ModTags;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModItems;
import com.mrcrayfish.furniture.inventory.container.PrinterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

/**
 * A printer (1.12.2): with ink it copies the book on top into the tray below. An ink cartridge lasts 5000 ticks and an ink sac 1000; a
 * copy takes 1000 ticks, or 10000 for an enchanted book. Written books are copied the way crafting copies them (a copy of a copy can't be
 * copied again).
 *
 * Author: MrCrayfish
 */
public class PrinterBlockEntity extends ApplianceContainerBlockEntity
{
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_INK = 1;
    public static final int SLOT_OUTPUT = 2;
    private static final int[] SLOTS_TOP = new int[]{SLOT_INPUT};
    private static final int[] SLOTS_BOTTOM = new int[]{SLOT_OUTPUT};
    private static final int[] SLOTS_SIDE = new int[]{SLOT_INK};

    private int inkTime;
    private int inkTimeTotal;
    private int printTime;
    private int printTimeTotal;

    private final ContainerData data = new ContainerData()
    {
        @Override
        public int get(int index)
        {
            return switch(index)
            {
                case 0 -> PrinterBlockEntity.this.inkTime;
                case 1 -> PrinterBlockEntity.this.inkTimeTotal;
                case 2 -> PrinterBlockEntity.this.printTime;
                case 3 -> PrinterBlockEntity.this.printTimeTotal;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value)
        {
            switch(index)
            {
                case 0 -> PrinterBlockEntity.this.inkTime = value;
                case 1 -> PrinterBlockEntity.this.inkTimeTotal = value;
                case 2 -> PrinterBlockEntity.this.printTime = value;
                case 3 -> PrinterBlockEntity.this.printTimeTotal = value;
            }
        }

        @Override
        public int getCount()
        {
            return 4;
        }
    };

    public PrinterBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.PRINTER.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, PrinterBlockEntity printer)
    {
        boolean wasRunning = printer.isRunning();
        if(printer.inkTime > 0)
        {
            printer.inkTime--;
        }

        ItemStack copy = printer.makeCopy();
        if(copy.isEmpty())
        {
            printer.printTime = 0;
        }
        else
        {
            if(printer.inkTime <= 0)
            {
                printer.useInk();
            }

            if(printer.inkTime > 0)
            {
                printer.printTimeTotal = getPrintTime(printer.items.get(SLOT_INPUT));
                if(++printer.printTime >= printer.printTimeTotal)
                {
                    printer.printTime = 0;
                    printer.items.set(SLOT_OUTPUT, copy);
                    printer.setChanged();
                }
            }
        }

        if(wasRunning != printer.isRunning())
        {
            printer.onRunningChanged();
        }
    }

    private void useInk()
    {
        ItemStack ink = this.items.get(SLOT_INK);
        int time = getInkTime(ink);
        if(time <= 0)
            return;

        this.inkTime = this.inkTimeTotal = time;
        ItemStackTemplate remainder = ink.getCraftingRemainder();
        ink.shrink(1);
        if(ink.isEmpty() && remainder != null)
        {
            this.items.set(SLOT_INK, remainder.create());
        }
        this.setChanged();
    }

    /**
     * The copy of what is on top, or empty when it can't print it (nothing there, the tray isn't empty, or it can't be copied).
     */
    private ItemStack makeCopy()
    {
        ItemStack input = this.items.get(SLOT_INPUT);
        if(input.isEmpty() || !input.is(ModTags.Items.PRINTABLE) || !this.items.get(SLOT_OUTPUT).isEmpty())
            return ItemStack.EMPTY;

        WrittenBookContent content = input.get(DataComponents.WRITTEN_BOOK_CONTENT);
        if(content != null)
        {
            if(content.generation() >= 2)
                return ItemStack.EMPTY;
            ItemStack copy = input.copyWithCount(1);
            copy.set(DataComponents.WRITTEN_BOOK_CONTENT, content.craftCopy());
            return copy;
        }
        return input.copyWithCount(1);
    }

    public static int getPrintTime(ItemStack stack)
    {
        return stack.is(Items.ENCHANTED_BOOK) ? 10000 : 1000;
    }

    public static int getInkTime(ItemStack stack)
    {
        if(stack.is(ModItems.INK_CARTRIDGE.get()))
            return 5000;
        if(stack.is(Items.INK_SAC))
            return 1000;
        return 0;
    }

    @Override
    public boolean isRunning()
    {
        return this.inkTime > 0 && this.printTime > 0;
    }

    public ContainerData getPrinterData()
    {
        return this.data;
    }

    @Override
    public int getContainerSize()
    {
        return 3;
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.printer");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return new PrinterMenu(windowId, playerInventory, this);
    }

    @Override
    public boolean isMatchingContainerMenu(AbstractContainerMenu menu)
    {
        return menu instanceof PrinterMenu printerMenu && printerMenu.getBlockEntity() == this;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack)
    {
        return switch(index)
        {
            case SLOT_INPUT -> stack.is(ModTags.Items.PRINTABLE);
            case SLOT_INK -> getInkTime(stack) > 0;
            default -> false;
        };
    }

    @Override
    public int[] getSlotsForFace(Direction direction)
    {
        return switch(direction)
        {
            case UP -> SLOTS_TOP;
            case DOWN -> SLOTS_BOTTOM;
            default -> SLOTS_SIDE;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return index == SLOT_OUTPUT;
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.inkTime = input.getIntOr("InkTime", 0);
        this.inkTimeTotal = input.getIntOr("InkTimeTotal", 0);
        this.printTime = input.getIntOr("PrintTime", 0);
        this.printTimeTotal = input.getIntOr("PrintTimeTotal", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putInt("InkTime", this.inkTime);
        output.putInt("InkTimeTotal", this.inkTimeTotal);
        output.putInt("PrintTime", this.printTime);
        output.putInt("PrintTimeTotal", this.printTimeTotal);
    }
}
