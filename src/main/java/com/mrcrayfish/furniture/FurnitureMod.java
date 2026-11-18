package com.mrcrayfish.furniture;

import com.mrcrayfish.furniture.common.CapabilitySetup;
import com.mrcrayfish.furniture.common.CommonHandler;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.core.ModContainers;
import com.mrcrayfish.furniture.core.ModCreativeTabs;
import com.mrcrayfish.furniture.core.ModDataComponents;
import com.mrcrayfish.furniture.core.ModEntities;
import com.mrcrayfish.furniture.core.ModItems;
import com.mrcrayfish.furniture.core.ModRecipeSerializers;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.datagen.DataSetup;
import com.mrcrayfish.furniture.network.PacketHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The mod on both sides. Client-only setup lives in {@link com.mrcrayfish.furniture.client.FurnitureClient}, which only the client loads.
 */
@Mod(Reference.MOD_ID)
public class FurnitureMod
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

    public FurnitureMod(IEventBus modBus, ModContainer container)
    {
        ModBlocks.REGISTER.register(modBus);
        ModItems.REGISTER.register(modBus);
        ModDataComponents.REGISTER.register(modBus);
        ModEntities.REGISTER.register(modBus);
        ModBlockEntities.REGISTER.register(modBus);
        ModContainers.REGISTER.register(modBus);
        ModSounds.REGISTER.register(modBus);
        ModRecipeTypes.REGISTER.register(modBus);
        ModRecipeSerializers.REGISTER.register(modBus);
        ModCreativeTabs.REGISTER.register(modBus);
        container.registerConfig(ModConfig.Type.LOCAL, FurnitureConfig.commonSpec);
        modBus.addListener(PacketHandler::register);
        modBus.addListener(this::onCommonSetup);
        modBus.addListener(CapabilitySetup::onRegisterCapabilities);
        DataSetup.register(modBus);
        // The freezer, oven and microwave screens check their ingredients on the client too, using a toaster or chopping board does, and the computer lists
        // what MineBay sells, so clients get those recipes
        NeoForge.EVENT_BUS.addListener(OnDatapackSyncEvent.class, event -> event.sendRecipes(ModRecipeTypes.FREEZER_SOLIDIFY.get(),
                ModRecipeTypes.OVEN_BAKING.get(), ModRecipeTypes.MICROWAVE_HEATING.get(), ModRecipeTypes.TOASTING.get(), ModRecipeTypes.MINEBAY.get(), ModRecipeTypes.CHOPPING.get()));
    }

    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(CommonHandler::setup);
    }
}
