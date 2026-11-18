package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.jspecify.annotations.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The nine slots in a wall cabinet. Hoppers only put potions in, as in 1.12.2 (it is a medicine cabinet).
 *
 * Author: MrCrayfish
 */
public class WallCabinetBlockEntity extends BasicLootBlockEntity
{
    public WallCabinetBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.WALL_CABINET.get(), pos, state);
    }

    @Override
    public int getContainerSize()
    {
        return 9;
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.wall_cabinet");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return new DispenserMenu(windowId, playerInventory, this);
    }

    @Override
    public boolean isMatchingContainerMenu(AbstractContainerMenu menu)
    {
        return menu instanceof DispenserMenu dispenserMenu && dispenserMenu.slots.getFirst().container == this;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        return stack.getItem() instanceof PotionItem;
    }

    @Override
    public void onOpen(Level level, BlockPos pos, BlockState state)
    {
        level.playSound(null, pos, ModSounds.BLOCK_CABINET_OPEN.get(), SoundSource.BLOCKS, 0.75F, 0.9F);
    }

    @Override
    public void onClose(Level level, BlockPos pos, BlockState state)
    {
        level.playSound(null, pos, ModSounds.BLOCK_CABINET_CLOSE.get(), SoundSource.BLOCKS, 0.75F, 0.8F);
    }
}
