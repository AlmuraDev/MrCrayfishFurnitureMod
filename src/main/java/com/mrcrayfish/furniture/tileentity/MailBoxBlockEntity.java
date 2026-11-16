package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.FurnitureConfig;
import com.mrcrayfish.furniture.common.mail.Mail;
import com.mrcrayfish.furniture.common.mail.PostOffice;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.inventory.container.MailBoxMenu;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MailBoxBlockEntity extends BasicLootBlockEntity
{
    private UUID id;
    private String name = "Mail Box";
    private String ownerName;
    private UUID ownerId;

    protected MailBoxBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public MailBoxBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.MAIL_BOX.get(), pos, state);
    }

    public void setId(UUID id)
    {
        if(this.id == null)
        {
            this.id = id;
        }
    }

    @Nullable
    public UUID getId()
    {
        return this.id;
    }

    public void setMailBoxName(String name)
    {
        this.name = name;
        this.setChanged();
    }

    public String getMailBoxName()
    {
        return name;
    }

    public void setOwner(ServerPlayer entity)
    {
        this.ownerId = entity.getUUID();
        this.ownerName = entity.getName().getString();
    }

    @Nullable
    public UUID getOwnerId()
    {
        return this.ownerId;
    }

    @Nullable
    public String getOwnerName()
    {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    public void updateOwnerName(ServerPlayer player)
    {
        if(player.getUUID().equals(this.ownerId))
        {
            if(!player.getName().getString().equals(this.ownerName))
            {
                this.ownerName = player.getName().getString();
            }
        }
    }

    public void updateIdAndAttemptClaim(ServerPlayer player)
    {
        if(this.id == null)
        {
            this.id = UUID.randomUUID();
        }
        if(this.ownerId == null)
        {
            this.setOwner(player);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MailBoxBlockEntity blockEntity)
    {
        MinecraftServer server = level.getServer();
        if(server == null || blockEntity.ownerId == null || blockEntity.id == null)
            return;

        // Attempt to register the mail box if it was somehow removed
        if(!PostOffice.isRegistered(blockEntity.ownerId, blockEntity.id))
        {
            ServerPlayer player = server.getPlayerList().getPlayer(blockEntity.ownerId);
            if(player != null)
            {
                PostOffice.registerMailBox(player, blockEntity.id, "Mail Box", blockEntity.worldPosition);
            }
        }
        else if(!blockEntity.isFull() && server.getTickCount() % FurnitureConfig.COMMON.pullMailInterval.get() == 0)
        {
            Supplier<Mail> supplier = PostOffice.getMailForPlayerMailBox(blockEntity.ownerId, blockEntity.id);
            while(!blockEntity.isFull())
            {
                Mail mail = supplier.get();
                if(mail == null) break;
                blockEntity.addItem(mail.getStack());
            }
        }
    }

    // Drops the mail still queued for this mail box and takes it out of the post office (server only)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if(this.level != null && !this.level.isClientSide() && this.id != null && this.ownerId != null)
        {
            Supplier<Mail> supplier = PostOffice.getMailForPlayerMailBox(this.ownerId, this.id);
            while(true)
            {
                Mail mail = supplier.get();
                if(mail == null) break;
                Containers.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), mail.getStack());
            }
            PostOffice.unregisterMailBox(this.ownerId, this.id);
        }
    }

    @Override
    public int getContainerSize()
    {
        return 9;
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.cfm.mail_box", this.ownerName, this.name);
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return new MailBoxMenu(windowId, playerInventory, this);
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

    // The client only needs the mail box's name and owner, not the items (menus sync those)
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
        input.read("MailBoxUUID", UUIDUtil.CODEC).ifPresent(id -> this.id = id);
        input.getString("MailBoxName").ifPresent(name -> this.name = name);
        input.getString("OwnerName").ifPresent(ownerName -> this.ownerName = ownerName);
        input.read("OwnerUUID", UUIDUtil.CODEC).ifPresent(ownerId -> this.ownerId = ownerId);
    }

    private void writeData(ValueOutput output)
    {
        output.storeNullable("MailBoxUUID", UUIDUtil.CODEC, this.id);
        if(this.name != null)
        {
            output.putString("MailBoxName", this.name);
        }
        if(this.ownerName != null && this.ownerId != null)
        {
            output.putString("OwnerName", this.ownerName);
            output.store("OwnerUUID", UUIDUtil.CODEC, this.ownerId);
        }
    }
}
