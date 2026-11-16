package com.mrcrayfish.furniture.datagen;

import com.mrcrayfish.furniture.Reference;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;

/**
 * Registers the data generators (./gradlew runData).
 */
public class DataSetup
{
    public static void register(IEventBus modBus)
    {
        modBus.addListener(GatherDataRegistryEntriesEvent.class, DataSetup::onGatherRegistryEntries);
        modBus.addListener(GatherDataEvent.Client.class, DataSetup::onGatherData);
        modBus.addListener(GatherDataEvent.Server.class, DataSetup::onGatherData);
    }

    /**
     * Recipes, their unlock advancements and loot tables are datapack registry entries in 26.3, so they are
     * gathered here instead of in GatherDataEvent. The grill and freezer recipes keep their ids in the
     * minecraft namespace (e.g. minecraft:cooked_beef_from_grill_cooking), so that namespace is generated too.
     */
    private static void onGatherRegistryEntries(GatherDataRegistryEntriesEvent event)
    {
        event.gatherFor(Reference.MOD_ID, "minecraft")
                .recipe(RecipeGen::new)
                .add(Registries.LOOT_TABLE, new LootTableGen());
    }

    /**
     * The data run uses clientData(), which fires the client event. The server event is handled as well so a
     * serverData run generates the same tags.
     */
    private static void onGatherData(GatherDataEvent event)
    {
        event.createBlockAndItemTags(BlockTagGen::new, ItemTagGen::new);
    }
}
