package com.mrcrayfish.furniture.datagen;

import com.mrcrayfish.furniture.block.BathBlock;
import com.mrcrayfish.furniture.block.ChristmasTreeBlock;
import com.mrcrayfish.furniture.block.GrandChairBlock;
import com.mrcrayfish.furniture.block.CoffeeTableBlock;
import com.mrcrayfish.furniture.block.ShowerBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import com.mrcrayfish.furniture.block.FirePitBlock;
import com.mrcrayfish.furniture.block.ModernBedBlock;
import com.mrcrayfish.furniture.block.ModernSlidingDoorBlock;
import com.mrcrayfish.furniture.core.ModDataComponents;
import com.mrcrayfish.furniture.core.ModItems;
import net.minecraft.world.level.storage.loot.entries.UniformContainerBase;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import com.mrcrayfish.furniture.core.ModBlocks;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Ocelot
 */
public class LootTableGen extends LootTableProvider
{
    public LootTableGen()
    {
        super(Set.of(), List.of(new SubProviderEntry(BlockProvider::new, LootContextParamSets.BLOCK)));
    }

    private static class BlockProvider extends BlockLootSubProvider
    {
        protected BlockProvider(LootTableSubProvider.Context output)
        {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), output);
        }

        @Override
        protected void generate()
        {
            this.dropSelf(ModBlocks.SOFA_WHITE.get());
            this.dropSelf(ModBlocks.SOFA_ORANGE.get());
            this.dropSelf(ModBlocks.SOFA_MAGENTA.get());
            this.dropSelf(ModBlocks.SOFA_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.SOFA_YELLOW.get());
            this.dropSelf(ModBlocks.SOFA_LIME.get());
            this.dropSelf(ModBlocks.SOFA_PINK.get());
            this.dropSelf(ModBlocks.SOFA_GRAY.get());
            this.dropSelf(ModBlocks.SOFA_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.SOFA_CYAN.get());
            this.dropSelf(ModBlocks.SOFA_PURPLE.get());
            this.dropSelf(ModBlocks.SOFA_BLUE.get());
            this.dropSelf(ModBlocks.SOFA_BROWN.get());
            this.dropSelf(ModBlocks.SOFA_GREEN.get());
            this.dropSelf(ModBlocks.SOFA_RED.get());
            this.dropSelf(ModBlocks.SOFA_BLACK.get());
            this.dropSelf(ModBlocks.SOFA_RAINBOW.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_WHITE.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_ORANGE.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_MAGENTA.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_YELLOW.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_LIME.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_PINK.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_GRAY.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_CYAN.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_PURPLE.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_BLUE.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_BROWN.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_GREEN.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_RED.get());
            this.dropSelf(ModBlocks.PICKET_FENCE_BLACK.get());
            this.dropSelf(ModBlocks.PICKET_GATE_WHITE.get());
            this.dropSelf(ModBlocks.PICKET_GATE_ORANGE.get());
            this.dropSelf(ModBlocks.PICKET_GATE_MAGENTA.get());
            this.dropSelf(ModBlocks.PICKET_GATE_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.PICKET_GATE_YELLOW.get());
            this.dropSelf(ModBlocks.PICKET_GATE_LIME.get());
            this.dropSelf(ModBlocks.PICKET_GATE_PINK.get());
            this.dropSelf(ModBlocks.PICKET_GATE_GRAY.get());
            this.dropSelf(ModBlocks.PICKET_GATE_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.PICKET_GATE_CYAN.get());
            this.dropSelf(ModBlocks.PICKET_GATE_PURPLE.get());
            this.dropSelf(ModBlocks.PICKET_GATE_BLUE.get());
            this.dropSelf(ModBlocks.PICKET_GATE_BROWN.get());
            this.dropSelf(ModBlocks.PICKET_GATE_GREEN.get());
            this.dropSelf(ModBlocks.PICKET_GATE_RED.get());
            this.dropSelf(ModBlocks.PICKET_GATE_BLACK.get());
            this.dropSelf(ModBlocks.POST_BOX.get());
            this.dropSelf(ModBlocks.HEDGE_OAK.get());
            this.dropSelf(ModBlocks.HEDGE_SPRUCE.get());
            this.dropSelf(ModBlocks.HEDGE_BIRCH.get());
            this.dropSelf(ModBlocks.HEDGE_JUNGLE.get());
            this.dropSelf(ModBlocks.HEDGE_ACACIA.get());
            this.dropSelf(ModBlocks.HEDGE_DARK_OAK.get());
            this.dropSelf(ModBlocks.HEDGE_MANGROVE.get());
            this.dropSelf(ModBlocks.HEDGE_AZALEA.get());
            this.dropSelf(ModBlocks.HEDGE_FLOWERING_AZALEA.get());
            this.dropSelf(ModBlocks.ROCK_PATH.get());
            this.dropSelf(ModBlocks.BIRD_BATH.get());
            this.dropSelf(ModBlocks.CANDLE.get());
            this.dropSelf(ModBlocks.CHIMNEY.get());
            this.dropSelf(ModBlocks.MANTEL_PIECE.get());
            this.dropSelf(ModBlocks.WREATH.get());
            this.dropSelf(ModBlocks.FAIRY_LIGHT.get());
            this.dropSelf(ModBlocks.TOILET.get());
            this.dropSelf(ModBlocks.CEILING_LIGHT.get());
            this.dropSelf(ModBlocks.MODERN_LIGHT.get());
            this.dropSelf(ModBlocks.RANGE_HOOD.get());
            this.dropSelf(ModBlocks.LAMP_WHITE.get());
            this.dropSelf(ModBlocks.LAMP_ORANGE.get());
            this.dropSelf(ModBlocks.LAMP_MAGENTA.get());
            this.dropSelf(ModBlocks.LAMP_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.LAMP_YELLOW.get());
            this.dropSelf(ModBlocks.LAMP_LIME.get());
            this.dropSelf(ModBlocks.LAMP_PINK.get());
            this.dropSelf(ModBlocks.LAMP_GRAY.get());
            this.dropSelf(ModBlocks.LAMP_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.LAMP_CYAN.get());
            this.dropSelf(ModBlocks.LAMP_PURPLE.get());
            this.dropSelf(ModBlocks.LAMP_BLUE.get());
            this.dropSelf(ModBlocks.LAMP_BROWN.get());
            this.dropSelf(ModBlocks.LAMP_GREEN.get());
            this.dropSelf(ModBlocks.LAMP_RED.get());
            this.dropSelf(ModBlocks.LAMP_BLACK.get());
            this.registerLightSwitch(ModBlocks.LIGHT_SWITCH.get());
            this.dropSelf(ModBlocks.BASIN.get());
            this.dropSelf(ModBlocks.MODERN_CHAIR.get());
            this.dropSelf(ModBlocks.MODERN_TABLE.get());
            this.dropSelf(ModBlocks.MODERN_OUTDOOR_TABLE.get());
            this.dropSelf(ModBlocks.MODERN_COFFEE_TABLE.get());
            this.dropSelf(ModBlocks.MODERN_STAIR.get());
            this.dropSelf(ModBlocks.MODERN_WINDOW.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_WHITE.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_ORANGE.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_MAGENTA.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_YELLOW.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_LIME.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_PINK.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_GRAY.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_CYAN.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_PURPLE.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_BLUE.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_BROWN.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_GREEN.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_RED.get());
            this.dropSelf(ModBlocks.MODERN_COUCH_BLACK.get());
            this.add(ModBlocks.MODERN_BED_WHITE.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_ORANGE.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_MAGENTA.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_LIGHT_BLUE.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_YELLOW.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_LIME.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_PINK.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_GRAY.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_LIGHT_GRAY.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_CYAN.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_PURPLE.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_BLUE.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_BROWN.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_GREEN.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_RED.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_BED_BLACK.get(), block -> this.createSinglePropConditionTable(block, ModernBedBlock.PART, BedPart.HEAD));
            this.add(ModBlocks.MODERN_SLIDING_DOOR.get(), block -> this.createSinglePropConditionTable(block, ModernSlidingDoorBlock.HALF, DoubleBlockHalf.LOWER));
            this.dropSelf(ModBlocks.TAP.get());
            this.dropSelf(ModBlocks.SHOWER_HEAD.get());
            this.dropSelf(ModBlocks.OVEN.get());
            this.dropSelf(ModBlocks.MICROWAVE.get());
            this.dropSelf(ModBlocks.TOASTER.get());
            this.dropSelf(ModBlocks.BLENDER.get());
            this.dropSelf(ModBlocks.DISHWASHER.get());
            this.dropSelf(ModBlocks.WASHING_MACHINE.get());
            this.dropSelf(ModBlocks.BENCH.get());
            this.dropSelf(ModBlocks.OUTDOOR_TABLE.get());
            this.dropSelf(ModBlocks.CASTLE_NETTING.get());
            this.dropSelf(ModBlocks.STONE_PATH.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_WHITE.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_ORANGE.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_MAGENTA.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_YELLOW.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_LIME.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_PINK.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_GRAY.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_CYAN.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_PURPLE.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_BLUE.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_BROWN.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_GREEN.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_RED.get());
            this.dropSelf(ModBlocks.INFLATABLE_CASTLE_BLACK.get());
            this.dropSelf(ModBlocks.BIN.get());
            this.dropSelf(ModBlocks.MIRROR.get());
            this.dropSelf(ModBlocks.CURTAINS_WHITE.get());
            this.dropSelf(ModBlocks.CURTAINS_ORANGE.get());
            this.dropSelf(ModBlocks.CURTAINS_MAGENTA.get());
            this.dropSelf(ModBlocks.CURTAINS_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.CURTAINS_YELLOW.get());
            this.dropSelf(ModBlocks.CURTAINS_LIME.get());
            this.dropSelf(ModBlocks.CURTAINS_PINK.get());
            this.dropSelf(ModBlocks.CURTAINS_GRAY.get());
            this.dropSelf(ModBlocks.CURTAINS_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.CURTAINS_CYAN.get());
            this.dropSelf(ModBlocks.CURTAINS_PURPLE.get());
            this.dropSelf(ModBlocks.CURTAINS_BLUE.get());
            this.dropSelf(ModBlocks.CURTAINS_BROWN.get());
            this.dropSelf(ModBlocks.CURTAINS_GREEN.get());
            this.dropSelf(ModBlocks.CURTAINS_RED.get());
            this.dropSelf(ModBlocks.CURTAINS_BLACK.get());
            this.add(ModBlocks.GRAND_CHAIR.get(), block -> this.createSinglePropConditionTable(block, GrandChairBlock.HALF, DoubleBlockHalf.LOWER));
            this.dropSelf(ModBlocks.KITCHEN_CABINET_WHITE.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_ORANGE.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_MAGENTA.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_YELLOW.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_LIME.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_PINK.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_GRAY.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_CYAN.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_PURPLE.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_BLUE.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_BROWN.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_GREEN.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_RED.get());
            this.dropSelf(ModBlocks.KITCHEN_CABINET_BLACK.get());
            this.dropSelf(ModBlocks.WALL_CABINET.get());
            this.dropSelf(ModBlocks.CHOPPING_BOARD.get());
            this.dropSelf(ModBlocks.PLATE.get());
            this.dropSelf(ModBlocks.COOKIE_JAR.get());
            // Only the bottom of the tree drops it; a placed present is only wrapping, its contents spill out
            this.add(ModBlocks.CHRISTMAS_TREE.get(), block -> this.createSinglePropConditionTable(block, ChristmasTreeBlock.HALF, DoubleBlockHalf.LOWER));
            this.add(ModBlocks.PRESENT_WHITE.get(), noDrop());
            this.add(ModBlocks.PRESENT_ORANGE.get(), noDrop());
            this.add(ModBlocks.PRESENT_MAGENTA.get(), noDrop());
            this.add(ModBlocks.PRESENT_LIGHT_BLUE.get(), noDrop());
            this.add(ModBlocks.PRESENT_YELLOW.get(), noDrop());
            this.add(ModBlocks.PRESENT_LIME.get(), noDrop());
            this.add(ModBlocks.PRESENT_PINK.get(), noDrop());
            this.add(ModBlocks.PRESENT_GRAY.get(), noDrop());
            this.add(ModBlocks.PRESENT_LIGHT_GRAY.get(), noDrop());
            this.add(ModBlocks.PRESENT_CYAN.get(), noDrop());
            this.add(ModBlocks.PRESENT_PURPLE.get(), noDrop());
            this.add(ModBlocks.PRESENT_BLUE.get(), noDrop());
            this.add(ModBlocks.PRESENT_BROWN.get(), noDrop());
            this.add(ModBlocks.PRESENT_GREEN.get(), noDrop());
            this.add(ModBlocks.PRESENT_RED.get(), noDrop());
            this.add(ModBlocks.PRESENT_BLACK.get(), noDrop());
            this.dropSelf(ModBlocks.TV.get());
            this.dropSelf(ModBlocks.MODERN_TV.get());
            this.dropSelf(ModBlocks.TV_STAND.get());
            this.dropSelf(ModBlocks.STEREO.get());
            this.dropSelf(ModBlocks.COMPUTER.get());
            this.dropSelf(ModBlocks.PRINTER.get());
            this.dropSelf(ModBlocks.FIRE_ALARM.get());
            this.dropSelf(ModBlocks.ELECTRIC_FENCE.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_WHITE.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_ORANGE.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_MAGENTA.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_YELLOW.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_LIME.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_PINK.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_GRAY.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_CYAN.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_PURPLE.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_BLUE.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_BROWN.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_GREEN.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_RED.get());
            this.dropSelf(ModBlocks.DIGITAL_CLOCK_BLACK.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_WHITE.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_ORANGE.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_MAGENTA.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_YELLOW.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_LIME.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_PINK.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_GRAY.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_CYAN.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_PURPLE.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_BLUE.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_BROWN.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_GREEN.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_RED.get());
            this.dropSelf(ModBlocks.PHOTO_FRAME_BLACK.get());
            this.dropSelf(ModBlocks.DOOR_BELL_OAK.get());
            this.dropSelf(ModBlocks.DOOR_BELL_SPRUCE.get());
            this.dropSelf(ModBlocks.DOOR_BELL_BIRCH.get());
            this.dropSelf(ModBlocks.DOOR_BELL_JUNGLE.get());
            this.dropSelf(ModBlocks.DOOR_BELL_ACACIA.get());
            this.dropSelf(ModBlocks.DOOR_BELL_DARK_OAK.get());
            this.dropSelf(ModBlocks.DOOR_BELL_CRIMSON.get());
            this.dropSelf(ModBlocks.DOOR_BELL_WARPED.get());
            this.dropSelf(ModBlocks.DOOR_BELL_MANGROVE.get());
            this.dropOther(ModBlocks.CUP.get(), ModItems.CUP.get());
            this.add(ModBlocks.SHOWER.get(), block -> this.createSinglePropConditionTable(block, ShowerBlock.HALF, DoubleBlockHalf.LOWER));
            this.add(ModBlocks.BATH.get(), block -> this.createSinglePropConditionTable(block, BathBlock.PART, BedPart.HEAD));
            this.registerFirePit(ModBlocks.FIRE_PIT.get());
            this.dropSelf(ModBlocks.BAR_STOOL_WHITE.get());
            this.dropSelf(ModBlocks.BAR_STOOL_ORANGE.get());
            this.dropSelf(ModBlocks.BAR_STOOL_MAGENTA.get());
            this.dropSelf(ModBlocks.BAR_STOOL_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.BAR_STOOL_YELLOW.get());
            this.dropSelf(ModBlocks.BAR_STOOL_LIME.get());
            this.dropSelf(ModBlocks.BAR_STOOL_PINK.get());
            this.dropSelf(ModBlocks.BAR_STOOL_GRAY.get());
            this.dropSelf(ModBlocks.BAR_STOOL_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.BAR_STOOL_CYAN.get());
            this.dropSelf(ModBlocks.BAR_STOOL_PURPLE.get());
            this.dropSelf(ModBlocks.BAR_STOOL_BLUE.get());
            this.dropSelf(ModBlocks.BAR_STOOL_BROWN.get());
            this.dropSelf(ModBlocks.BAR_STOOL_GREEN.get());
            this.dropSelf(ModBlocks.BAR_STOOL_RED.get());
            this.dropSelf(ModBlocks.BAR_STOOL_BLACK.get());
            this.registerTrampoline(ModBlocks.TRAMPOLINE.get());
            this.dropSelf(ModBlocks.COOLER_WHITE.get());
            this.dropSelf(ModBlocks.COOLER_ORANGE.get());
            this.dropSelf(ModBlocks.COOLER_MAGENTA.get());
            this.dropSelf(ModBlocks.COOLER_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.COOLER_YELLOW.get());
            this.dropSelf(ModBlocks.COOLER_LIME.get());
            this.dropSelf(ModBlocks.COOLER_PINK.get());
            this.dropSelf(ModBlocks.COOLER_GRAY.get());
            this.dropSelf(ModBlocks.COOLER_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.COOLER_CYAN.get());
            this.dropSelf(ModBlocks.COOLER_PURPLE.get());
            this.dropSelf(ModBlocks.COOLER_BLUE.get());
            this.dropSelf(ModBlocks.COOLER_BROWN.get());
            this.dropSelf(ModBlocks.COOLER_GREEN.get());
            this.dropSelf(ModBlocks.COOLER_RED.get());
            this.dropSelf(ModBlocks.COOLER_BLACK.get());
            this.dropSelf(ModBlocks.GRILL_WHITE.get());
            this.dropSelf(ModBlocks.GRILL_ORANGE.get());
            this.dropSelf(ModBlocks.GRILL_MAGENTA.get());
            this.dropSelf(ModBlocks.GRILL_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.GRILL_YELLOW.get());
            this.dropSelf(ModBlocks.GRILL_LIME.get());
            this.dropSelf(ModBlocks.GRILL_PINK.get());
            this.dropSelf(ModBlocks.GRILL_GRAY.get());
            this.dropSelf(ModBlocks.GRILL_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.GRILL_CYAN.get());
            this.dropSelf(ModBlocks.GRILL_PURPLE.get());
            this.dropSelf(ModBlocks.GRILL_BLUE.get());
            this.dropSelf(ModBlocks.GRILL_BROWN.get());
            this.dropSelf(ModBlocks.GRILL_GREEN.get());
            this.dropSelf(ModBlocks.GRILL_RED.get());
            this.dropSelf(ModBlocks.GRILL_BLACK.get());
            this.dropSelf(ModBlocks.DOOR_MAT.get());
            this.dropSelf(ModBlocks.DIVING_BOARD.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_WHITE.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_ORANGE.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_MAGENTA.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_YELLOW.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_LIME.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_PINK.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_GRAY.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_CYAN.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_PURPLE.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_BLUE.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_BROWN.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_GREEN.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_RED.get());
            this.dropSelf(ModBlocks.KITCHEN_COUNTER_BLACK.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_WHITE.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_ORANGE.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_MAGENTA.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_YELLOW.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_LIME.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_PINK.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_GRAY.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_CYAN.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_PURPLE.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_BLUE.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_BROWN.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_GREEN.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_RED.get());
            this.dropSelf(ModBlocks.KITCHEN_DRAWER_BLACK.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_WHITE.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_ORANGE.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_MAGENTA.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_LIGHT_BLUE.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_YELLOW.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_LIME.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_PINK.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_GRAY.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_LIGHT_GRAY.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_CYAN.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_PURPLE.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_BLUE.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_BROWN.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_GREEN.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_RED.get());
            this.dropSelf(ModBlocks.KITCHEN_SINK_BLACK.get());
            this.dropSelf(ModBlocks.FRIDGE_LIGHT.get());
            this.dropSelf(ModBlocks.FRIDGE_DARK.get());
            this.dropOther(ModBlocks.FREEZER_LIGHT.get(), ModBlocks.FRIDGE_LIGHT.get());
            this.dropOther(ModBlocks.FREEZER_DARK.get(), ModBlocks.FRIDGE_DARK.get());

            // Dynamically registers drops for wooden furniture
            for(GeneratorData.FurnitureType type : GeneratorData.ALL_TYPES)
            {
                for(GeneratorData.Variant variant : GeneratorData.ALL_VARIANTS)
                {
                    Consumer<Block> register = type == GeneratorData.COFFEE_TABLE ? this::registerCoffeeTable : this::dropSelf;
                    register.accept(GeneratorData.getResultBlock(type, variant, false).get());
                    if(variant.strippedLog() != null)
                    {
                        register.accept(GeneratorData.getResultBlock(type, variant, true).get());
                    }
                }
            }
        }

        public void registerCoffeeTable(Block block)
        {
            this.add(block, coffeeTable -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(this.applyExplosionCondition(coffeeTable, LootItem.lootTableItem(coffeeTable).apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(2)).when(MatchBlock.blockMatches(this.blocks, coffeeTable, StatePropertiesPredicate.Builder.properties().hasProperty(CoffeeTableBlock.TALL, true))))))));
        }

        public void registerLightSwitch(Block block)
        {
            // Keeps the switch's linked lights on the dropped item
            this.add(block, lightSwitch -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(this.applyExplosionCondition(lightSwitch, LootItem.lootTableItem(lightSwitch).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(ModDataComponents.LINKED_LIGHTS.get()))))));
        }

        public void registerFirePit(Block block)
        {
            // A fire pit gives back its firewood: one log for each stage it was built up to
            UniformContainerBase.Builder<?> firewood = LootItem.lootTableItem(ModItems.FIREWOOD.get());
            for(int stage = 1; stage <= FirePitBlock.MAX_STAGE; stage++)
            {
                firewood.apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(stage + 1)).when(MatchBlock.blockMatches(this.blocks, block, StatePropertiesPredicate.Builder.properties().hasProperty(FirePitBlock.STAGE, stage))));
            }
            this.add(block, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(firewood).when(ExplosionCondition.survivesExplosion())));
        }

        public void registerTrampoline(Block block)
        {
            // Keeps the custom name and the colour (base colour component) of the trampoline on the dropped item
            this.add(block, trampoline -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(trampoline).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME).include(DataComponents.BASE_COLOR)))));
        }

        @Override
        protected Iterable<Block> getKnownBlocks()
        {
            return ModBlocks.REGISTER.getEntries().stream().<Block>map(DeferredHolder::get).toList();
        }
    }
}
