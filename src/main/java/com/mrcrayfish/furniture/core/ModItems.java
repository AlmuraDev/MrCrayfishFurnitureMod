package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Author: MrCrayfish
 */
public class ModItems
{
    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(Reference.MOD_ID);

    public static final DeferredItem<Item> SPATULA = REGISTER.registerItem("spatula", Item::new, () -> new Item.Properties().sword(ToolMaterial.IRON, 3, -1.4F));
}
