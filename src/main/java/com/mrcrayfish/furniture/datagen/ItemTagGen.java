package com.mrcrayfish.furniture.datagen;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.common.ModTags;
import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.core.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.concurrent.CompletableFuture;

/**
 * @author Ocelot
 */
public class ItemTagGen extends BlockTagCopyingItemTagProvider
{
    public ItemTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> tagLookup)
    {
        super(output, lookupProvider, tagLookup, Reference.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        this.tag(ModTags.Items.GENERAL)
                // Sofa
                .add(item(ModBlocks.SOFA_WHITE))
                .add(item(ModBlocks.SOFA_ORANGE))
                .add(item(ModBlocks.SOFA_MAGENTA))
                .add(item(ModBlocks.SOFA_LIGHT_BLUE))
                .add(item(ModBlocks.SOFA_YELLOW))
                .add(item(ModBlocks.SOFA_LIME))
                .add(item(ModBlocks.SOFA_PINK))
                .add(item(ModBlocks.SOFA_GRAY))
                .add(item(ModBlocks.SOFA_LIGHT_GRAY))
                .add(item(ModBlocks.SOFA_CYAN))
                .add(item(ModBlocks.SOFA_PURPLE))
                .add(item(ModBlocks.SOFA_BLUE))
                .add(item(ModBlocks.SOFA_BROWN))
                .add(item(ModBlocks.SOFA_GREEN))
                .add(item(ModBlocks.SOFA_RED))
                .add(item(ModBlocks.SOFA_BLACK));

        // Brought forward from 1.12.2
        this.tag(ModTags.Items.GENERAL)
                .add(item(ModBlocks.CANDLE))
                .add(item(ModBlocks.CHIMNEY))
                .add(item(ModBlocks.MANTEL_PIECE))
                .add(item(ModBlocks.WREATH))
                .add(item(ModBlocks.FAIRY_LIGHT))
                .add(item(ModBlocks.TOILET));
        this.tag(ModTags.Items.KITCHEN)
                .add(item(ModBlocks.BAR_STOOL_WHITE))
                .add(item(ModBlocks.BAR_STOOL_ORANGE))
                .add(item(ModBlocks.BAR_STOOL_MAGENTA))
                .add(item(ModBlocks.BAR_STOOL_LIGHT_BLUE))
                .add(item(ModBlocks.BAR_STOOL_YELLOW))
                .add(item(ModBlocks.BAR_STOOL_LIME))
                .add(item(ModBlocks.BAR_STOOL_PINK))
                .add(item(ModBlocks.BAR_STOOL_GRAY))
                .add(item(ModBlocks.BAR_STOOL_LIGHT_GRAY))
                .add(item(ModBlocks.BAR_STOOL_CYAN))
                .add(item(ModBlocks.BAR_STOOL_PURPLE))
                .add(item(ModBlocks.BAR_STOOL_BLUE))
                .add(item(ModBlocks.BAR_STOOL_BROWN))
                .add(item(ModBlocks.BAR_STOOL_GREEN))
                .add(item(ModBlocks.BAR_STOOL_RED))
                .add(item(ModBlocks.BAR_STOOL_BLACK));
        this.tag(ModTags.Items.OUTDOORS)
                .add(item(ModBlocks.BIRD_BATH))
                .add(ResourceKey.create(Registries.ITEM, ModItems.FIREWOOD.getId()));
        this.tag(ModTags.Items.GENERAL)
                .add(item(ModBlocks.CEILING_LIGHT))
                .add(item(ModBlocks.MODERN_LIGHT))
                .add(item(ModBlocks.LIGHT_SWITCH))
                .add(item(ModBlocks.LAMP_WHITE))
                .add(item(ModBlocks.LAMP_ORANGE))
                .add(item(ModBlocks.LAMP_MAGENTA))
                .add(item(ModBlocks.LAMP_LIGHT_BLUE))
                .add(item(ModBlocks.LAMP_YELLOW))
                .add(item(ModBlocks.LAMP_LIME))
                .add(item(ModBlocks.LAMP_PINK))
                .add(item(ModBlocks.LAMP_GRAY))
                .add(item(ModBlocks.LAMP_LIGHT_GRAY))
                .add(item(ModBlocks.LAMP_CYAN))
                .add(item(ModBlocks.LAMP_PURPLE))
                .add(item(ModBlocks.LAMP_BLUE))
                .add(item(ModBlocks.LAMP_BROWN))
                .add(item(ModBlocks.LAMP_GREEN))
                .add(item(ModBlocks.LAMP_RED))
                .add(item(ModBlocks.LAMP_BLACK));
        this.tag(ModTags.Items.KITCHEN)
                .add(item(ModBlocks.RANGE_HOOD));
        this.tag(ModTags.Items.GENERAL)
                .add(item(ModBlocks.BASIN))
                .add(item(ModBlocks.SHOWER))
                .add(item(ModBlocks.SHOWER_HEAD))
                .add(item(ModBlocks.BATH));
        this.tag(ModTags.Items.OUTDOORS)
                .add(item(ModBlocks.TAP))
                .add(item(ModBlocks.MODERN_OUTDOOR_TABLE));
        this.tag(ModTags.Items.GENERAL)
                .add(item(ModBlocks.MODERN_CHAIR))
                .add(item(ModBlocks.MODERN_TABLE))
                .add(item(ModBlocks.MODERN_COFFEE_TABLE))
                .add(item(ModBlocks.MODERN_STAIR))
                .add(item(ModBlocks.MODERN_WINDOW))
                .add(item(ModBlocks.MODERN_SLIDING_DOOR))
                .add(item(ModBlocks.MODERN_COUCH_WHITE))
                .add(item(ModBlocks.MODERN_COUCH_ORANGE))
                .add(item(ModBlocks.MODERN_COUCH_MAGENTA))
                .add(item(ModBlocks.MODERN_COUCH_LIGHT_BLUE))
                .add(item(ModBlocks.MODERN_COUCH_YELLOW))
                .add(item(ModBlocks.MODERN_COUCH_LIME))
                .add(item(ModBlocks.MODERN_COUCH_PINK))
                .add(item(ModBlocks.MODERN_COUCH_GRAY))
                .add(item(ModBlocks.MODERN_COUCH_LIGHT_GRAY))
                .add(item(ModBlocks.MODERN_COUCH_CYAN))
                .add(item(ModBlocks.MODERN_COUCH_PURPLE))
                .add(item(ModBlocks.MODERN_COUCH_BLUE))
                .add(item(ModBlocks.MODERN_COUCH_BROWN))
                .add(item(ModBlocks.MODERN_COUCH_GREEN))
                .add(item(ModBlocks.MODERN_COUCH_RED))
                .add(item(ModBlocks.MODERN_COUCH_BLACK));
        this.tag(ModTags.Items.BEDROOM)
                .add(item(ModBlocks.MODERN_BED_WHITE))
                .add(item(ModBlocks.MODERN_BED_ORANGE))
                .add(item(ModBlocks.MODERN_BED_MAGENTA))
                .add(item(ModBlocks.MODERN_BED_LIGHT_BLUE))
                .add(item(ModBlocks.MODERN_BED_YELLOW))
                .add(item(ModBlocks.MODERN_BED_LIME))
                .add(item(ModBlocks.MODERN_BED_PINK))
                .add(item(ModBlocks.MODERN_BED_GRAY))
                .add(item(ModBlocks.MODERN_BED_LIGHT_GRAY))
                .add(item(ModBlocks.MODERN_BED_CYAN))
                .add(item(ModBlocks.MODERN_BED_PURPLE))
                .add(item(ModBlocks.MODERN_BED_BLUE))
                .add(item(ModBlocks.MODERN_BED_BROWN))
                .add(item(ModBlocks.MODERN_BED_GREEN))
                .add(item(ModBlocks.MODERN_BED_RED))
                .add(item(ModBlocks.MODERN_BED_BLACK));

        this.tag(ModTags.Items.KITCHEN)
                // Kitchen Counters
                .add(item(ModBlocks.KITCHEN_COUNTER_WHITE))
                .add(item(ModBlocks.KITCHEN_COUNTER_ORANGE))
                .add(item(ModBlocks.KITCHEN_COUNTER_MAGENTA))
                .add(item(ModBlocks.KITCHEN_COUNTER_LIGHT_BLUE))
                .add(item(ModBlocks.KITCHEN_COUNTER_YELLOW))
                .add(item(ModBlocks.KITCHEN_COUNTER_LIME))
                .add(item(ModBlocks.KITCHEN_COUNTER_PINK))
                .add(item(ModBlocks.KITCHEN_COUNTER_GRAY))
                .add(item(ModBlocks.KITCHEN_COUNTER_LIGHT_GRAY))
                .add(item(ModBlocks.KITCHEN_COUNTER_CYAN))
                .add(item(ModBlocks.KITCHEN_COUNTER_PURPLE))
                .add(item(ModBlocks.KITCHEN_COUNTER_BLUE))
                .add(item(ModBlocks.KITCHEN_COUNTER_BROWN))
                .add(item(ModBlocks.KITCHEN_COUNTER_GREEN))
                .add(item(ModBlocks.KITCHEN_COUNTER_RED))
                .add(item(ModBlocks.KITCHEN_COUNTER_BLACK))
                // Kitchen Drawers
                .add(item(ModBlocks.KITCHEN_DRAWER_WHITE))
                .add(item(ModBlocks.KITCHEN_DRAWER_ORANGE))
                .add(item(ModBlocks.KITCHEN_DRAWER_MAGENTA))
                .add(item(ModBlocks.KITCHEN_DRAWER_LIGHT_BLUE))
                .add(item(ModBlocks.KITCHEN_DRAWER_YELLOW))
                .add(item(ModBlocks.KITCHEN_DRAWER_LIME))
                .add(item(ModBlocks.KITCHEN_DRAWER_PINK))
                .add(item(ModBlocks.KITCHEN_DRAWER_GRAY))
                .add(item(ModBlocks.KITCHEN_DRAWER_LIGHT_GRAY))
                .add(item(ModBlocks.KITCHEN_DRAWER_CYAN))
                .add(item(ModBlocks.KITCHEN_DRAWER_PURPLE))
                .add(item(ModBlocks.KITCHEN_DRAWER_BLUE))
                .add(item(ModBlocks.KITCHEN_DRAWER_BROWN))
                .add(item(ModBlocks.KITCHEN_DRAWER_GREEN))
                .add(item(ModBlocks.KITCHEN_DRAWER_RED))
                .add(item(ModBlocks.KITCHEN_DRAWER_BLACK))
                // Spatula
                .add(ModItems.SPATULA.key())
                // Kitchen Sinks
                .add(item(ModBlocks.KITCHEN_SINK_WHITE))
                .add(item(ModBlocks.KITCHEN_SINK_ORANGE))
                .add(item(ModBlocks.KITCHEN_SINK_MAGENTA))
                .add(item(ModBlocks.KITCHEN_SINK_LIGHT_BLUE))
                .add(item(ModBlocks.KITCHEN_SINK_YELLOW))
                .add(item(ModBlocks.KITCHEN_SINK_LIME))
                .add(item(ModBlocks.KITCHEN_SINK_PINK))
                .add(item(ModBlocks.KITCHEN_SINK_GRAY))
                .add(item(ModBlocks.KITCHEN_SINK_LIGHT_GRAY))
                .add(item(ModBlocks.KITCHEN_SINK_CYAN))
                .add(item(ModBlocks.KITCHEN_SINK_PURPLE))
                .add(item(ModBlocks.KITCHEN_SINK_BLUE))
                .add(item(ModBlocks.KITCHEN_SINK_BROWN))
                .add(item(ModBlocks.KITCHEN_SINK_GREEN))
                .add(item(ModBlocks.KITCHEN_SINK_RED))
                .add(item(ModBlocks.KITCHEN_SINK_BLACK))
                //Fridge
                .add(item(ModBlocks.FRIDGE_LIGHT))
                .add(item(ModBlocks.FRIDGE_DARK));

        this.tag(ModTags.Items.OUTDOORS)
                // Fences and Fence Gates
                .addTag(ModTags.Items.UPGRADED_FENCE_GATES)
                .addTag(ModTags.Items.UPGRADED_FENCES)
                // Post Box
                .add(item(ModBlocks.POST_BOX))
                // Hedge
                .addTag(ModTags.Items.HEDGES)
                // Rock Path
                .add(item(ModBlocks.ROCK_PATH))
                // Trampolines
                .add(item(ModBlocks.TRAMPOLINE))
                // Coolers
                .add(item(ModBlocks.COOLER_WHITE))
                .add(item(ModBlocks.COOLER_ORANGE))
                .add(item(ModBlocks.COOLER_MAGENTA))
                .add(item(ModBlocks.COOLER_LIGHT_BLUE))
                .add(item(ModBlocks.COOLER_YELLOW))
                .add(item(ModBlocks.COOLER_LIME))
                .add(item(ModBlocks.COOLER_PINK))
                .add(item(ModBlocks.COOLER_GRAY))
                .add(item(ModBlocks.COOLER_LIGHT_GRAY))
                .add(item(ModBlocks.COOLER_CYAN))
                .add(item(ModBlocks.COOLER_PURPLE))
                .add(item(ModBlocks.COOLER_BLUE))
                .add(item(ModBlocks.COOLER_BROWN))
                .add(item(ModBlocks.COOLER_GREEN))
                .add(item(ModBlocks.COOLER_RED))
                .add(item(ModBlocks.COOLER_BLACK))
                // Grill
                .add(item(ModBlocks.GRILL_WHITE))
                .add(item(ModBlocks.GRILL_ORANGE))
                .add(item(ModBlocks.GRILL_MAGENTA))
                .add(item(ModBlocks.GRILL_LIGHT_BLUE))
                .add(item(ModBlocks.GRILL_YELLOW))
                .add(item(ModBlocks.GRILL_LIME))
                .add(item(ModBlocks.GRILL_PINK))
                .add(item(ModBlocks.GRILL_GRAY))
                .add(item(ModBlocks.GRILL_LIGHT_GRAY))
                .add(item(ModBlocks.GRILL_CYAN))
                .add(item(ModBlocks.GRILL_PURPLE))
                .add(item(ModBlocks.GRILL_BLUE))
                .add(item(ModBlocks.GRILL_BROWN))
                .add(item(ModBlocks.GRILL_GREEN))
                .add(item(ModBlocks.GRILL_RED))
                .add(item(ModBlocks.GRILL_BLACK))
                // Spatula
                .add(ModItems.SPATULA.key())
                .add(item(ModBlocks.DOOR_MAT));

        this.tag(ModTags.Items.STORAGE)
                // Coolers
                .add(item(ModBlocks.COOLER_WHITE))
                .add(item(ModBlocks.COOLER_ORANGE))
                .add(item(ModBlocks.COOLER_MAGENTA))
                .add(item(ModBlocks.COOLER_LIGHT_BLUE))
                .add(item(ModBlocks.COOLER_YELLOW))
                .add(item(ModBlocks.COOLER_LIME))
                .add(item(ModBlocks.COOLER_PINK))
                .add(item(ModBlocks.COOLER_GRAY))
                .add(item(ModBlocks.COOLER_LIGHT_GRAY))
                .add(item(ModBlocks.COOLER_CYAN))
                .add(item(ModBlocks.COOLER_PURPLE))
                .add(item(ModBlocks.COOLER_BLUE))
                .add(item(ModBlocks.COOLER_BROWN))
                .add(item(ModBlocks.COOLER_GREEN))
                .add(item(ModBlocks.COOLER_RED))
                .add(item(ModBlocks.COOLER_BLACK))
                // Post Box
                .add(item(ModBlocks.POST_BOX))
                // Coolers
                .add(item(ModBlocks.COOLER_WHITE))
                .add(item(ModBlocks.COOLER_ORANGE))
                .add(item(ModBlocks.COOLER_MAGENTA))
                .add(item(ModBlocks.COOLER_LIGHT_BLUE))
                .add(item(ModBlocks.COOLER_YELLOW))
                .add(item(ModBlocks.COOLER_LIME))
                .add(item(ModBlocks.COOLER_PINK))
                .add(item(ModBlocks.COOLER_GRAY))
                .add(item(ModBlocks.COOLER_LIGHT_GRAY))
                .add(item(ModBlocks.COOLER_CYAN))
                .add(item(ModBlocks.COOLER_PURPLE))
                .add(item(ModBlocks.COOLER_BLUE))
                .add(item(ModBlocks.COOLER_BROWN))
                .add(item(ModBlocks.COOLER_GREEN))
                .add(item(ModBlocks.COOLER_RED))
                .add(item(ModBlocks.COOLER_BLACK))
                // Kitchen Drawers
                .add(item(ModBlocks.KITCHEN_DRAWER_WHITE))
                .add(item(ModBlocks.KITCHEN_DRAWER_ORANGE))
                .add(item(ModBlocks.KITCHEN_DRAWER_MAGENTA))
                .add(item(ModBlocks.KITCHEN_DRAWER_LIGHT_BLUE))
                .add(item(ModBlocks.KITCHEN_DRAWER_YELLOW))
                .add(item(ModBlocks.KITCHEN_DRAWER_LIME))
                .add(item(ModBlocks.KITCHEN_DRAWER_PINK))
                .add(item(ModBlocks.KITCHEN_DRAWER_GRAY))
                .add(item(ModBlocks.KITCHEN_DRAWER_LIGHT_GRAY))
                .add(item(ModBlocks.KITCHEN_DRAWER_CYAN))
                .add(item(ModBlocks.KITCHEN_DRAWER_PURPLE))
                .add(item(ModBlocks.KITCHEN_DRAWER_BLUE))
                .add(item(ModBlocks.KITCHEN_DRAWER_BROWN))
                .add(item(ModBlocks.KITCHEN_DRAWER_GREEN))
                .add(item(ModBlocks.KITCHEN_DRAWER_RED))
                .add(item(ModBlocks.KITCHEN_DRAWER_BLACK));

        this.tag(ModTags.Items.TRAMPOLINE).add(item(ModBlocks.TRAMPOLINE));
        this.tag(ModTags.Items.ITEMS).add(ModItems.SPATULA.key());
        this.tag(ModTags.Items.KITCHEN)
                .add(item(ModBlocks.OVEN))
                .add(item(ModBlocks.MICROWAVE))
                .add(item(ModBlocks.TOASTER))
                .add(item(ModBlocks.BLENDER))
                .add(item(ModBlocks.DISHWASHER));
        this.tag(ModTags.Items.GENERAL)
                .add(item(ModBlocks.WASHING_MACHINE));
        this.tag(ModTags.Items.GENERAL)
                .add(item(ModBlocks.TV))
                .add(item(ModBlocks.MODERN_TV))
                .add(item(ModBlocks.TV_STAND))
                .add(item(ModBlocks.STEREO))
                .add(item(ModBlocks.COMPUTER))
                .add(item(ModBlocks.PRINTER))
                .add(item(ModBlocks.FIRE_ALARM))
                .add(item(ModBlocks.DIGITAL_CLOCK_WHITE))
                .add(item(ModBlocks.DIGITAL_CLOCK_ORANGE))
                .add(item(ModBlocks.DIGITAL_CLOCK_MAGENTA))
                .add(item(ModBlocks.DIGITAL_CLOCK_LIGHT_BLUE))
                .add(item(ModBlocks.DIGITAL_CLOCK_YELLOW))
                .add(item(ModBlocks.DIGITAL_CLOCK_LIME))
                .add(item(ModBlocks.DIGITAL_CLOCK_PINK))
                .add(item(ModBlocks.DIGITAL_CLOCK_GRAY))
                .add(item(ModBlocks.DIGITAL_CLOCK_LIGHT_GRAY))
                .add(item(ModBlocks.DIGITAL_CLOCK_CYAN))
                .add(item(ModBlocks.DIGITAL_CLOCK_PURPLE))
                .add(item(ModBlocks.DIGITAL_CLOCK_BLUE))
                .add(item(ModBlocks.DIGITAL_CLOCK_BROWN))
                .add(item(ModBlocks.DIGITAL_CLOCK_GREEN))
                .add(item(ModBlocks.DIGITAL_CLOCK_RED))
                .add(item(ModBlocks.DIGITAL_CLOCK_BLACK))
                .add(item(ModBlocks.PHOTO_FRAME_WHITE))
                .add(item(ModBlocks.PHOTO_FRAME_ORANGE))
                .add(item(ModBlocks.PHOTO_FRAME_MAGENTA))
                .add(item(ModBlocks.PHOTO_FRAME_LIGHT_BLUE))
                .add(item(ModBlocks.PHOTO_FRAME_YELLOW))
                .add(item(ModBlocks.PHOTO_FRAME_LIME))
                .add(item(ModBlocks.PHOTO_FRAME_PINK))
                .add(item(ModBlocks.PHOTO_FRAME_GRAY))
                .add(item(ModBlocks.PHOTO_FRAME_LIGHT_GRAY))
                .add(item(ModBlocks.PHOTO_FRAME_CYAN))
                .add(item(ModBlocks.PHOTO_FRAME_PURPLE))
                .add(item(ModBlocks.PHOTO_FRAME_BLUE))
                .add(item(ModBlocks.PHOTO_FRAME_BROWN))
                .add(item(ModBlocks.PHOTO_FRAME_GREEN))
                .add(item(ModBlocks.PHOTO_FRAME_RED))
                .add(item(ModBlocks.PHOTO_FRAME_BLACK))
                .add(item(ModBlocks.DOOR_BELL_OAK))
                .add(item(ModBlocks.DOOR_BELL_SPRUCE))
                .add(item(ModBlocks.DOOR_BELL_BIRCH))
                .add(item(ModBlocks.DOOR_BELL_JUNGLE))
                .add(item(ModBlocks.DOOR_BELL_ACACIA))
                .add(item(ModBlocks.DOOR_BELL_DARK_OAK))
                .add(item(ModBlocks.DOOR_BELL_CRIMSON))
                .add(item(ModBlocks.DOOR_BELL_WARPED))
                .add(item(ModBlocks.DOOR_BELL_MANGROVE));
        this.tag(ModTags.Items.OUTDOORS)
                .add(item(ModBlocks.ELECTRIC_FENCE));
        this.tag(ModTags.Items.KITCHEN)
                .add(item(ModBlocks.KITCHEN_CABINET_WHITE))
                .add(item(ModBlocks.KITCHEN_CABINET_ORANGE))
                .add(item(ModBlocks.KITCHEN_CABINET_MAGENTA))
                .add(item(ModBlocks.KITCHEN_CABINET_LIGHT_BLUE))
                .add(item(ModBlocks.KITCHEN_CABINET_YELLOW))
                .add(item(ModBlocks.KITCHEN_CABINET_LIME))
                .add(item(ModBlocks.KITCHEN_CABINET_PINK))
                .add(item(ModBlocks.KITCHEN_CABINET_GRAY))
                .add(item(ModBlocks.KITCHEN_CABINET_LIGHT_GRAY))
                .add(item(ModBlocks.KITCHEN_CABINET_CYAN))
                .add(item(ModBlocks.KITCHEN_CABINET_PURPLE))
                .add(item(ModBlocks.KITCHEN_CABINET_BLUE))
                .add(item(ModBlocks.KITCHEN_CABINET_BROWN))
                .add(item(ModBlocks.KITCHEN_CABINET_GREEN))
                .add(item(ModBlocks.KITCHEN_CABINET_RED))
                .add(item(ModBlocks.KITCHEN_CABINET_BLACK))
                .add(item(ModBlocks.CHOPPING_BOARD))
                .add(item(ModBlocks.PLATE))
                .add(item(ModBlocks.COOKIE_JAR));
        this.tag(ModTags.Items.GENERAL)
                .add(item(ModBlocks.WALL_CABINET));
        this.tag(ModTags.Items.GENERAL)
                .add(item(ModBlocks.BIN))
                .add(item(ModBlocks.MIRROR))
                .add(item(ModBlocks.GRAND_CHAIR))
                .add(item(ModBlocks.CURTAINS_WHITE))
                .add(item(ModBlocks.CURTAINS_ORANGE))
                .add(item(ModBlocks.CURTAINS_MAGENTA))
                .add(item(ModBlocks.CURTAINS_LIGHT_BLUE))
                .add(item(ModBlocks.CURTAINS_YELLOW))
                .add(item(ModBlocks.CURTAINS_LIME))
                .add(item(ModBlocks.CURTAINS_PINK))
                .add(item(ModBlocks.CURTAINS_GRAY))
                .add(item(ModBlocks.CURTAINS_LIGHT_GRAY))
                .add(item(ModBlocks.CURTAINS_CYAN))
                .add(item(ModBlocks.CURTAINS_PURPLE))
                .add(item(ModBlocks.CURTAINS_BLUE))
                .add(item(ModBlocks.CURTAINS_BROWN))
                .add(item(ModBlocks.CURTAINS_GREEN))
                .add(item(ModBlocks.CURTAINS_RED))
                .add(item(ModBlocks.CURTAINS_BLACK));
        this.tag(ModTags.Items.ITEMS)
                .add(ModItems.KNIFE.key());
        this.tag(ModTags.Items.GENERAL)
                .add(item(ModBlocks.CHRISTMAS_TREE))
                .add(item(ModBlocks.PRESENT_WHITE))
                .add(item(ModBlocks.PRESENT_ORANGE))
                .add(item(ModBlocks.PRESENT_MAGENTA))
                .add(item(ModBlocks.PRESENT_LIGHT_BLUE))
                .add(item(ModBlocks.PRESENT_YELLOW))
                .add(item(ModBlocks.PRESENT_LIME))
                .add(item(ModBlocks.PRESENT_PINK))
                .add(item(ModBlocks.PRESENT_GRAY))
                .add(item(ModBlocks.PRESENT_LIGHT_GRAY))
                .add(item(ModBlocks.PRESENT_CYAN))
                .add(item(ModBlocks.PRESENT_PURPLE))
                .add(item(ModBlocks.PRESENT_BLUE))
                .add(item(ModBlocks.PRESENT_BROWN))
                .add(item(ModBlocks.PRESENT_GREEN))
                .add(item(ModBlocks.PRESENT_RED))
                .add(item(ModBlocks.PRESENT_BLACK));
        this.tag(ModTags.Items.ITEMS)
                .add(ModItems.TV_REMOTE.key())
                .add(ModItems.INK_CARTRIDGE.key());
        this.tag(ModTags.Items.PRINTABLE)
                .add(Items.WRITTEN_BOOK.builtInRegistryHolder().key(), Items.ENCHANTED_BOOK.builtInRegistryHolder().key());
        this.tag(ModTags.Items.ITEMS)
                .add(ModItems.BREAD_SLICE.key())
                .add(ModItems.TOAST.key())
                .add(ModItems.CUP.key())
                .add(ModItems.SOAP.key())
                .add(ModItems.SOAPY_WATER.key())
                .add(ModItems.SUPER_SOAPY_WATER.key());
        // 1.12.2 listed these in its recipe API; tags let packs add more
        this.tag(ModTags.Items.DISHWASHER_CLEANABLE)
                .addTag(ItemTags.PICKAXES)
                .addTag(ItemTags.SHOVELS)
                .addTag(ItemTags.SWORDS)
                .addTag(ItemTags.AXES)
                .addTag(ItemTags.HOES)
                .addTag(ItemTags.SPEARS)
                .add(Items.BOW.builtInRegistryHolder().key(), Items.CROSSBOW.builtInRegistryHolder().key(), Items.FISHING_ROD.builtInRegistryHolder().key(),
                        Items.FLINT_AND_STEEL.builtInRegistryHolder().key(), Items.SHEARS.builtInRegistryHolder().key(), Items.SHIELD.builtInRegistryHolder().key(),
                        Items.TRIDENT.builtInRegistryHolder().key(), Items.MACE.builtInRegistryHolder().key(), Items.BRUSH.builtInRegistryHolder().key(),
                        Items.CARROT_ON_A_STICK.builtInRegistryHolder().key(), Items.WARPED_FUNGUS_ON_A_STICK.builtInRegistryHolder().key(), ModItems.SPATULA.key());
        this.tag(ModTags.Items.WASHING_MACHINE_CLEANABLE)
                .addTag(ItemTags.HEAD_ARMOR)
                .addTag(ItemTags.CHEST_ARMOR)
                .addTag(ItemTags.LEG_ARMOR)
                .addTag(ItemTags.FOOT_ARMOR)
                .add(Items.ELYTRA.builtInRegistryHolder().key());

        this.copy(ModTags.Blocks.UPGRADED_FENCE_GATES, ModTags.Items.UPGRADED_FENCE_GATES);
        this.copy(ModTags.Blocks.PICKET_FENCE_GATES, ModTags.Items.PICKET_FENCE_GATES);
        this.copy(ModTags.Blocks.UPGRADED_FENCES, ModTags.Items.UPGRADED_FENCES);
        this.copy(ModTags.Blocks.PICKET_FENCES, ModTags.Items.PICKET_FENCES);
        this.copy(ModTags.Blocks.HEDGES, ModTags.Items.HEDGES);

        TagBuilder bedroomTag = this.getOrCreateRawBuilder(ModTags.Items.BEDROOM);
        for(GeneratorData.Variant variant : GeneratorData.ALL_VARIANTS)
        {
            bedroomTag.addElement(GeneratorData.getResultBlock(GeneratorData.BEDSIDE_CABINET, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                bedroomTag.addElement(GeneratorData.getResultBlock(GeneratorData.BEDSIDE_CABINET, variant, true).getId());
            }

            bedroomTag.addElement(GeneratorData.getResultBlock(GeneratorData.DESK, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                bedroomTag.addElement(GeneratorData.getResultBlock(GeneratorData.DESK, variant, true).getId());
            }

            bedroomTag.addElement(GeneratorData.getResultBlock(GeneratorData.DESK_CABINET, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                bedroomTag.addElement(GeneratorData.getResultBlock(GeneratorData.DESK_CABINET, variant, true).getId());
            }

            bedroomTag.addElement(GeneratorData.getResultBlock(GeneratorData.BLINDS, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                bedroomTag.addElement(GeneratorData.getResultBlock(GeneratorData.BLINDS, variant, true).getId());
            }
        }

        TagBuilder generalTag = this.getOrCreateRawBuilder(ModTags.Items.GENERAL);
        for(GeneratorData.Variant variant : GeneratorData.ALL_VARIANTS)
        {
            generalTag.addElement(GeneratorData.getResultBlock(GeneratorData.TABLE, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                generalTag.addElement(GeneratorData.getResultBlock(GeneratorData.TABLE, variant, true).getId());
            }

            generalTag.addElement(GeneratorData.getResultBlock(GeneratorData.CHAIR, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                generalTag.addElement(GeneratorData.getResultBlock(GeneratorData.CHAIR, variant, true).getId());
            }

            generalTag.addElement(GeneratorData.getResultBlock(GeneratorData.COFFEE_TABLE, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                generalTag.addElement(GeneratorData.getResultBlock(GeneratorData.COFFEE_TABLE, variant, true).getId());
            }
        }

        TagBuilder kitchenTag = this.getOrCreateRawBuilder(ModTags.Items.KITCHEN);
        for(GeneratorData.Variant variant : GeneratorData.ALL_VARIANTS)
        {
            kitchenTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_COUNTER, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                kitchenTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_COUNTER, variant, true).getId());
            }

            kitchenTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_DRAWER, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                kitchenTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_DRAWER, variant, true).getId());
            }

            kitchenTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_SINK_LIGHT, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                kitchenTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_SINK_LIGHT, variant, true).getId());
            }

            kitchenTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_SINK_DARK, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                kitchenTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_SINK_DARK, variant, true).getId());
            }
        }

        TagBuilder outdoorTag = this.getOrCreateRawBuilder(ModTags.Items.OUTDOORS);
        for(GeneratorData.Variant variant : GeneratorData.ALL_VARIANTS)
        {
            outdoorTag.addElement(GeneratorData.getResultBlock(GeneratorData.PARK_BENCH, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                outdoorTag.addElement(GeneratorData.getResultBlock(GeneratorData.PARK_BENCH, variant, true).getId());
            }

            outdoorTag.addElement(GeneratorData.getResultBlock(GeneratorData.MAIL_BOX, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                outdoorTag.addElement(GeneratorData.getResultBlock(GeneratorData.MAIL_BOX, variant, true).getId());
            }

            outdoorTag.addElement(GeneratorData.getResultBlock(GeneratorData.MAIL_BOX, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                outdoorTag.addElement(GeneratorData.getResultBlock(GeneratorData.MAIL_BOX, variant, true).getId());
            }
        }

        TagBuilder storageTag = this.getOrCreateRawBuilder(ModTags.Items.STORAGE);
        for(GeneratorData.Variant variant : GeneratorData.ALL_VARIANTS)
        {
            storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.CABINET, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.CABINET, variant, true).getId());
            }

            storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.BEDSIDE_CABINET, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.BEDSIDE_CABINET, variant, true).getId());
            }

            storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.DESK_CABINET, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.DESK_CABINET, variant, true).getId());
            }

            storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.CRATE, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.CRATE, variant, true).getId());
            }

            storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.MAIL_BOX, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.MAIL_BOX, variant, true).getId());
            }

            storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_DRAWER, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_DRAWER, variant, true).getId());
            }

            storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_SINK_LIGHT, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_SINK_LIGHT, variant, true).getId());
            }

            storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_SINK_DARK, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                storageTag.addElement(GeneratorData.getResultBlock(GeneratorData.KITCHEN_SINK_DARK, variant, true).getId());
            }
        }
    }

    private static ResourceKey<Item> item(DeferredBlock<?> block)
    {
        return ResourceKey.create(Registries.ITEM, block.getId());
    }
}
