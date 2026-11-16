package com.mrcrayfish.furniture.common;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.tileentity.KitchenSinkBlockEntity;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;

import java.util.List;
import java.util.function.Supplier;

/**
 * Lets hoppers, pipes and other mods reach the furniture's inventories and the sink's water.
 */
public class CapabilitySetup
{
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event)
    {
        List<Supplier<? extends BlockEntityType<? extends WorldlyContainer>>> containers = List.of(
                ModBlockEntities.CABINET, ModBlockEntities.BEDSIDE_CABINET, ModBlockEntities.DESK_CABINET, ModBlockEntities.CRATE,
                ModBlockEntities.MAIL_BOX, ModBlockEntities.COOLER, ModBlockEntities.KITCHEN_DRAWER, ModBlockEntities.FRIDGE,
                ModBlockEntities.FREEZER, ModBlockEntities.GRILL);
        containers.forEach(type -> registerContainer(event, type.get()));
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, ModBlockEntities.KITCHEN_SINK.get(), (sink, side) -> sink.getFluidHandler());
    }

    private static <T extends BlockEntity & WorldlyContainer> void registerContainer(RegisterCapabilitiesEvent event, BlockEntityType<? extends WorldlyContainer> type)
    {
        @SuppressWarnings("unchecked")
        BlockEntityType<T> containerType = (BlockEntityType<T>) type;
        event.registerBlockEntity(Capabilities.Item.BLOCK, containerType, WorldlyContainerWrapper::new);
    }
}
