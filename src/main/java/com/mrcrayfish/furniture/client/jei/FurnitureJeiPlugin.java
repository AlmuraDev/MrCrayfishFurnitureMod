package com.mrcrayfish.furniture.client.jei;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.core.ModBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;

/**
 * Tells JEI that items sharing one id but differing in colour are separate entries.
 * JEI only loads this class when it is installed.
 */
@JeiPlugin
public class FurnitureJeiPlugin implements IModPlugin
{
    private static final Identifier UID = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "jei_plugin");

    @Override
    public Identifier getPluginUid()
    {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration)
    {
        // One trampoline item, sixteen colours held in the base colour component
        registration.registerFromDataComponentTypes(ModBlocks.TRAMPOLINE.get().asItem(), DataComponents.BASE_COLOR);
    }
}
