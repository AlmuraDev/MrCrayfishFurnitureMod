package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.item.DrinkItem;
import com.mrcrayfish.furniture.item.FirewoodItem;
import com.mrcrayfish.furniture.item.TVRemoteItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
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
    // Electronics (1.12.2)
    public static final DeferredItem<TVRemoteItem> TV_REMOTE = REGISTER.registerItem("tv_remote", TVRemoteItem::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> INK_CARTRIDGE = REGISTER.registerItem("ink_cartridge", Item::new, Item.Properties::new);
    // Chops food on the chopping board; it fights like a stone sword (1.12.2)
    public static final DeferredItem<Item> KNIFE = REGISTER.registerItem("knife", Item::new, () -> new Item.Properties().sword(ToolMaterial.STONE, 2, -2.4F).durability(100));
    // Grill and fridge food (1.12.2): sausages and kebabs for the grill, and flesh, which the freezer makes from rotten flesh
    public static final DeferredItem<Item> SAUSAGE = REGISTER.registerItem("sausage", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.6F).build()));
    public static final DeferredItem<Item> COOKED_SAUSAGE = REGISTER.registerItem("cooked_sausage", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6F).build()));
    public static final DeferredItem<Item> KEBAB = REGISTER.registerItem("kebab", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.6F).build(),
            Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F)).build()));
    public static final DeferredItem<Item> COOKED_KEBAB = REGISTER.registerItem("cooked_kebab", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6F).build()));
    public static final DeferredItem<Item> FLESH = REGISTER.registerItem("flesh", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1F).build()));
    public static final DeferredItem<Item> COOKED_FLESH = REGISTER.registerItem("cooked_flesh", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6F).build()));
    // Freezer fuel (1.12.2)
    public static final DeferredItem<Item> COOL_PACK = REGISTER.registerItem("cool_pack", Item::new, Item.Properties::new);
    // In creative, breaks other players' mail boxes (1.12.2)
    public static final DeferredItem<Item> HAMMER = REGISTER.registerItem("hammer", Item::new, () -> new Item.Properties().stacksTo(1));
    // Lets an operator pry open a locked crate (1.12.2 opened sealed crates with it)
    public static final DeferredItem<Item> CROWBAR = REGISTER.registerItem("crowbar", Item::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> SOAP = REGISTER.registerItem("soap", Item::new, Item.Properties::new);
    // Runs the dishwasher and washing machine, which use it up (the bucket comes back when it is crafted from a water bucket); the super
    // kind cleans more than twice as fast
    public static final DeferredItem<Item> SOAPY_WATER = REGISTER.registerItem("soapy_water", Item::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> SUPER_SOAPY_WATER = REGISTER.registerItem("super_soapy_water", Item::new, () -> new Item.Properties().stacksTo(1));
}
