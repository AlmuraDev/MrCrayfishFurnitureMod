package com.mrcrayfish.furniture.client;

import com.mrcrayfish.furniture.client.event.CreativeScreenEvents;
import com.mrcrayfish.furniture.client.gui.screen.inventory.CrateScreen;
import com.mrcrayfish.furniture.client.gui.screen.inventory.BinScreen;
import com.mrcrayfish.furniture.client.gui.screen.inventory.ComputerScreen;
import com.mrcrayfish.furniture.client.gui.screen.inventory.FreezerScreen;
import com.mrcrayfish.furniture.client.gui.screen.inventory.PresentScreen;
import com.mrcrayfish.furniture.client.gui.screen.inventory.PrinterScreen;
import com.mrcrayfish.furniture.client.gui.screen.inventory.MicrowaveScreen;
import com.mrcrayfish.furniture.client.gui.screen.inventory.OvenScreen;
import com.mrcrayfish.furniture.client.gui.screen.inventory.WasherScreen;
import com.mrcrayfish.furniture.inventory.container.DishwasherMenu;
import com.mrcrayfish.furniture.inventory.container.WashingMachineMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import com.mrcrayfish.furniture.client.gui.screen.inventory.MailBoxScreen;
import com.mrcrayfish.furniture.client.gui.screen.inventory.PostBoxScreen;
import com.mrcrayfish.furniture.core.ModContainers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Registers the furniture screens and the creative tab's filters (client only).
 */
public class ScreenSetup
{
    public static void register(IEventBus modBus)
    {
        modBus.addListener(ScreenSetup::onRegisterMenuScreens);
        NeoForge.EVENT_BUS.register(new CreativeScreenEvents());
    }

    private static void onRegisterMenuScreens(RegisterMenuScreensEvent event)
    {
        event.register(ModContainers.CRATE.get(), CrateScreen::new);
        event.register(ModContainers.POST_BOX.get(), PostBoxScreen::new);
        event.register(ModContainers.MAIL_BOX.get(), MailBoxScreen::new);
        event.register(ModContainers.FREEZER.get(), FreezerScreen::new);
        event.register(ModContainers.OVEN.get(), OvenScreen::new);
        event.register(ModContainers.MICROWAVE.get(), MicrowaveScreen::new);
        event.register(ModContainers.COMPUTER.get(), ComputerScreen::new);
        event.register(ModContainers.PRINTER.get(), PrinterScreen::new);
        event.register(ModContainers.PRESENT.get(), PresentScreen::new);
        event.register(ModContainers.BIN.get(), BinScreen::new);
        event.register(ModContainers.DISHWASHER.get(), (DishwasherMenu menu, Inventory inventory, Component title) -> new WasherScreen<>(menu, inventory, title, "dishwasher", WasherScreen.Layout.DISHWASHER));
        event.register(ModContainers.WASHING_MACHINE.get(), (WashingMachineMenu menu, Inventory inventory, Component title) -> new WasherScreen<>(menu, inventory, title, "washing_machine", WasherScreen.Layout.WASHING_MACHINE));
    }
}
