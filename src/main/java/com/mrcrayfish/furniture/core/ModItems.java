package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.item.DrinkItem;
import com.mrcrayfish.furniture.item.FirewoodItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumables;
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

    // Places and builds up a fire pit (the 1.12.2 log item)
    public static final DeferredItem<FirewoodItem> FIREWOOD = REGISTER.registerItem("firewood", props -> new FirewoodItem(ModBlocks.FIRE_PIT.get(), props), () -> new Item.Properties().stacksTo(16));

    // Appliances (1.12.2)
    public static final DeferredItem<Item> BREAD_SLICE = REGISTER.registerItem("bread_slice", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.6F).build()));
    public static final DeferredItem<Item> TOAST = REGISTER.registerItem("toast", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6F).build()));
    // An empty cup places a cup on the ground; the blender fills it
    public static final DeferredItem<BlockItem> CUP = REGISTER.registerItem("cup", props -> new BlockItem(ModBlocks.CUP.get(), props), Item.Properties::new);
    // A blended drink. The blender's recipe gives it its name, colour and food; drinking it leaves the cup, and it can be set down too
    public static final DeferredItem<DrinkItem> DRINK = REGISTER.registerItem("drink", props -> new DrinkItem(ModBlocks.CUP.get(), props), () -> new Item.Properties().stacksTo(1)
            .food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.5F).build(), Consumables.DEFAULT_DRINK).usingConvertsTo(CUP.get()));
    public static final DeferredItem<Item> SOAP = REGISTER.registerItem("soap", Item::new, Item.Properties::new);
    // Runs the dishwasher and washing machine, which use it up (the bucket comes back when it is crafted from a water bucket); the super
    // kind cleans more than twice as fast
    public static final DeferredItem<Item> SOAPY_WATER = REGISTER.registerItem("soapy_water", Item::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> SUPER_SOAPY_WATER = REGISTER.registerItem("super_soapy_water", Item::new, () -> new Item.Properties().stacksTo(1));
}
