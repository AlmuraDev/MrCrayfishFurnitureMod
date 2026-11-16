package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.block.CabinetBlock;
import com.mrcrayfish.furniture.block.CrateBlock;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.inventory.container.CrateMenu;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class CrateBlockEntity extends BasicLootBlockEntity
{
    @Nullable
    private UUID ownerUuid;
    private boolean locked;

    protected CrateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public CrateBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.CRATE.get(), pos, state);
    }

    @Override
    public int getContainerSize()
    {
        return 27;
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.crate");
    }

    @Override
    @Nullable
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        if(this.locked && !playerInventory.player.getUUID().equals(this.ownerUuid))
        {
            playerInventory.player.sendOverlayMessage(Component.translatable("container.isLocked", this.getDisplayName()));
            playerInventory.player.level().playSound(null, this.worldPosition, SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return null;
        }
        return new CrateMenu(windowId, playerInventory, this, this.locked);
    }

    @Override
    public boolean isMatchingContainerMenu(AbstractContainerMenu menu)
    {
        return menu instanceof CrateMenu crateMenu && crateMenu.getBlockEntity() == this;
    }

    @Nullable
    public UUID getOwner()
    {
        return this.ownerUuid;
    }

    public void setOwner(UUID uuid)
    {
        this.ownerUuid = uuid;
        this.setChanged();
    }

    public boolean isLocked()
    {
        return this.locked;
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;
        this.setChanged();
        BlockEntityUtil.sendUpdatePacket(this);
    }

    @Override
    public void onOpen(Level level, BlockPos pos, BlockState state)
    {
        this.playLidSound(state, ModSounds.BLOCK_CABINET_OPEN.get());
        this.setLidState(state, true);
    }

    @Override
    public void onClose(Level level, BlockPos pos, BlockState state)
    {
        this.playLidSound(state, ModSounds.BLOCK_CABINET_CLOSE.get());
        this.setLidState(state, false);
    }

    public void removeUnauthorisedPlayers()
    {
        if(this.locked && this.level != null)
        {
            for(Player player : this.level.getEntitiesOfClass(Player.class, new AABB(this.worldPosition).inflate(5.0)))
            {
                if(player.containerMenu instanceof CrateMenu crateMenu && crateMenu.getBlockEntity() == this && !player.getUUID().equals(this.ownerUuid))
                {
                    player.closeContainer();
                }
            }
        }
    }

    private void playLidSound(BlockState state, SoundEvent event)
    {
        Vec3i directionVec = state.getValue(CabinetBlock.DIRECTION).getUnitVec3i();
        double x = this.worldPosition.getX() + 0.5D + directionVec.getX() / 2.0D;
        double y = this.worldPosition.getY() + 0.5D + directionVec.getY() / 2.0D;
        double z = this.worldPosition.getZ() + 0.5D + directionVec.getZ() / 2.0D;
        Level level = this.getLevel();
        if(level != null)
        {
            level.playSound(null, x, y, z, event, SoundSource.BLOCKS, 0.75F, level.getRandom().nextFloat() * 0.1F + 0.7F);
        }
    }

    private void setLidState(BlockState state, boolean open)
    {
        Level level = this.getLevel();
        if(level != null)
        {
            level.setBlock(this.getBlockPos(), state.setValue(CrateBlock.OPEN, open), 3);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.readData(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        this.writeData(output);
    }

    // The client only needs the lock, not the items (menus sync those)
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        return BlockEntityUtil.createUpdateTag(registries, this::writeData);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ValueInput input)
    {
        this.readData(input);
    }

    @Override
    public void handleUpdateTag(ValueInput input)
    {
        this.readData(input);
    }

    private void readData(ValueInput input)
    {
        input.read("OwnerUUID", UUIDUtil.CODEC).ifPresent(uuid -> this.ownerUuid = uuid);
        this.locked = input.getBooleanOr("Locked", this.locked);
    }

    private void writeData(ValueOutput output)
    {
        output.storeNullable("OwnerUUID", UUIDUtil.CODEC, this.ownerUuid);
        output.putBoolean("Locked", this.locked);
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction)
    {
        return !this.locked;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction)
    {
        return !this.locked;
    }
}
