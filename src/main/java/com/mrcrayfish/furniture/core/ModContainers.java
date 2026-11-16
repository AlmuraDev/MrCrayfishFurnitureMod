package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.common.mail.MailBoxEntry;
import com.mrcrayfish.furniture.inventory.container.CrateMenu;
import com.mrcrayfish.furniture.inventory.container.FreezerMenu;
import com.mrcrayfish.furniture.inventory.container.MailBoxMenu;
import com.mrcrayfish.furniture.inventory.container.PostBoxMenu;
import com.mrcrayfish.furniture.tileentity.CrateBlockEntity;
import com.mrcrayfish.furniture.tileentity.FreezerBlockEntity;
import com.mrcrayfish.furniture.tileentity.MailBoxBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Author: MrCrayfish
 */
public class ModContainers
{
    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, Reference.MOD_ID);

    // The server writes the block's position (see the blocks' useWithoutItem); the client finds its copy of the block entity there
    public static final DeferredHolder<MenuType<?>, MenuType<CrateMenu>> CRATE = register("crate", (windowId, playerInventory, data) -> {
        CrateBlockEntity crateBlockEntity = (CrateBlockEntity) playerInventory.player.level().getBlockEntity(data.readBlockPos());
        return new CrateMenu(windowId, playerInventory, crateBlockEntity, crateBlockEntity.isLocked());
    });

    // The server writes the mail boxes the player can send to (see PostBoxBlock)
    public static final DeferredHolder<MenuType<?>, MenuType<PostBoxMenu>> POST_BOX = register("post_box", (windowId, playerInventory, data) -> {
        return new PostBoxMenu(windowId, playerInventory, MailBoxEntry.LIST_STREAM_CODEC.decode(data));
    });

    public static final DeferredHolder<MenuType<?>, MenuType<MailBoxMenu>> MAIL_BOX = register("mail_box", (windowId, playerInventory, data) -> {
        MailBoxBlockEntity mailBoxBlockEntity = (MailBoxBlockEntity) playerInventory.player.level().getBlockEntity(data.readBlockPos());
        return new MailBoxMenu(windowId, playerInventory, mailBoxBlockEntity);
    });

    public static final DeferredHolder<MenuType<?>, MenuType<FreezerMenu>> FREEZER = register("freezer", (windowId, playerInventory, data) -> {
        FreezerBlockEntity freezerBlockEntity = (FreezerBlockEntity) playerInventory.player.level().getBlockEntity(data.readBlockPos());
        return new FreezerMenu(windowId, playerInventory, freezerBlockEntity);
    });

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String key, IContainerFactory<T> factory)
    {
        return REGISTER.register(key, () -> new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS));
    }
}
