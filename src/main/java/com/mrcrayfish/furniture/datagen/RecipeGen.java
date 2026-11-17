package com.mrcrayfish.furniture.datagen;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.core.ModItems;
import com.mrcrayfish.furniture.item.crafting.CuttingRecipe;
import com.mrcrayfish.furniture.item.crafting.FreezerSolidifyRecipe;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import java.util.List;
import com.mrcrayfish.furniture.item.crafting.GrillCookingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

/**
 * @author Ocelot
 */
public class RecipeGen extends RecipeProvider
{
    public RecipeGen(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput)
    {
        super(recipeOutput, advancementOutput);
    }

    @Override
    protected void buildRecipes()
    {
        // Dynamically generates all recipes for wooden furniture
        for(GeneratorData.Variant variant : GeneratorData.ALL_VARIANTS)
        {
            this.table(GeneratorData.getResultBlock(GeneratorData.TABLE, variant, false).get(), variant.log().get(), variant.planks().get());
            this.chair(GeneratorData.getResultBlock(GeneratorData.CHAIR, variant, false).get(), variant.log().get(), variant.planks().get());
            this.coffeeTable(GeneratorData.getResultBlock(GeneratorData.COFFEE_TABLE, variant, false).get(), variant.log().get(), variant.planks().get());
            this.cabinet(GeneratorData.getResultBlock(GeneratorData.CABINET, variant, false).get(), variant.log().get(), variant.planks().get());
            this.bedsideCabinet(GeneratorData.getResultBlock(GeneratorData.BEDSIDE_CABINET, variant, false).get(), variant.log().get(), variant.planks().get());
            this.desk(GeneratorData.getResultBlock(GeneratorData.DESK, variant, false).get(), variant.log().get(), variant.planks().get());
            this.deskCabinet(GeneratorData.getResultBlock(GeneratorData.DESK_CABINET, variant, false).get(), variant.log().get(), variant.planks().get());
            this.blinds(GeneratorData.getResultBlock(GeneratorData.BLINDS, variant, false).get(), variant.log().get());
            this.upgradedFence(GeneratorData.getResultBlock(GeneratorData.UPGRADED_FENCE, variant, false).get(), variant.log().get());
            this.upgradedGate(GeneratorData.getResultBlock(GeneratorData.UPGRADED_GATE, variant, false).get(), variant.log().get());
            this.crate(GeneratorData.getResultBlock(GeneratorData.CRATE, variant, false).get(), variant.log().get(), variant.planks().get());
            this.parkBench(GeneratorData.getResultBlock(GeneratorData.PARK_BENCH, variant, false).get(), variant.log().get(), variant.planks().get());
            this.kitchenCounter(GeneratorData.getResultBlock(GeneratorData.KITCHEN_COUNTER, variant, false).get(), variant.log().get(), variant.planks().get());
            this.kitchenDrawer(GeneratorData.getResultBlock(GeneratorData.KITCHEN_DRAWER, variant, false).get(), variant.log().get(), variant.planks().get());
            this.kitchenSink(GeneratorData.getResultBlock(GeneratorData.KITCHEN_SINK_LIGHT, variant, false).get(), Blocks.CONCRETE.pick(DyeColor.WHITE), variant.planks().get());
            this.kitchenSink(GeneratorData.getResultBlock(GeneratorData.KITCHEN_SINK_DARK, variant, false).get(), Blocks.CONCRETE.pick(DyeColor.GRAY), variant.planks().get());
            this.mailBox(GeneratorData.getResultBlock(GeneratorData.MAIL_BOX, variant, false).get(), GeneratorData.getUpgradedFence(variant, false).get());

            if(variant.strippedLog() != null)
            {
                this.table(GeneratorData.getResultBlock(GeneratorData.TABLE, variant, true).get(), variant.strippedLog().get(), variant.log().get());
                this.chair(GeneratorData.getResultBlock(GeneratorData.CHAIR, variant, true).get(), variant.strippedLog().get(), variant.log().get());
                this.coffeeTable(GeneratorData.getResultBlock(GeneratorData.COFFEE_TABLE, variant, true).get(), variant.strippedLog().get(), variant.log().get());
                this.cabinet(GeneratorData.getResultBlock(GeneratorData.CABINET, variant, true).get(), variant.log().get(), variant.strippedLog().get());
                this.bedsideCabinet(GeneratorData.getResultBlock(GeneratorData.BEDSIDE_CABINET, variant, true).get(), variant.strippedLog().get(), variant.log().get());
                this.desk(GeneratorData.getResultBlock(GeneratorData.DESK, variant, true).get(), variant.strippedLog().get(), variant.log().get());
                this.deskCabinet(GeneratorData.getResultBlock(GeneratorData.DESK_CABINET, variant, true).get(), variant.strippedLog().get(), variant.log().get());
                this.blinds(GeneratorData.getResultBlock(GeneratorData.BLINDS, variant, true).get(), variant.strippedLog().get());
                this.upgradedFence(GeneratorData.getResultBlock(GeneratorData.UPGRADED_FENCE, variant, true).get(), variant.strippedLog().get());
                this.upgradedGate(GeneratorData.getResultBlock(GeneratorData.UPGRADED_GATE, variant, true).get(), variant.strippedLog().get());
                this.crate(GeneratorData.getResultBlock(GeneratorData.CRATE, variant, true).get(), variant.strippedLog().get(), variant.log().get());
                this.parkBench(GeneratorData.getResultBlock(GeneratorData.PARK_BENCH, variant, true).get(), variant.log().get(), variant.strippedLog().get());
                this.kitchenCounter(GeneratorData.getResultBlock(GeneratorData.KITCHEN_COUNTER, variant, true).get(), variant.log().get(), variant.strippedLog().get());
                this.kitchenDrawer(GeneratorData.getResultBlock(GeneratorData.KITCHEN_DRAWER, variant, true).get(), variant.log().get(), variant.strippedLog().get());
                this.kitchenSink(GeneratorData.getResultBlock(GeneratorData.KITCHEN_SINK_LIGHT, variant, true).get(), Blocks.CONCRETE.pick(DyeColor.WHITE), variant.strippedLog().get());
                this.kitchenSink(GeneratorData.getResultBlock(GeneratorData.KITCHEN_SINK_DARK, variant, true).get(), Blocks.CONCRETE.pick(DyeColor.GRAY), variant.strippedLog().get());
                this.mailBox(GeneratorData.getResultBlock(GeneratorData.MAIL_BOX, variant, true).get(), GeneratorData.getUpgradedFence(variant, true).get());
            }
        }

        // Desk Cabinet
        this.sofa(ModBlocks.SOFA_WHITE.get(), Blocks.WOOL.pick(DyeColor.WHITE));
        this.sofa(ModBlocks.SOFA_ORANGE.get(), Blocks.WOOL.pick(DyeColor.ORANGE));
        this.sofa(ModBlocks.SOFA_MAGENTA.get(), Blocks.WOOL.pick(DyeColor.MAGENTA));
        this.sofa(ModBlocks.SOFA_LIGHT_BLUE.get(), Blocks.WOOL.pick(DyeColor.LIGHT_BLUE));
        this.sofa(ModBlocks.SOFA_YELLOW.get(), Blocks.WOOL.pick(DyeColor.YELLOW));
        this.sofa(ModBlocks.SOFA_LIME.get(), Blocks.WOOL.pick(DyeColor.LIME));
        this.sofa(ModBlocks.SOFA_PINK.get(), Blocks.WOOL.pick(DyeColor.PINK));
        this.sofa(ModBlocks.SOFA_GRAY.get(), Blocks.WOOL.pick(DyeColor.GRAY));
        this.sofa(ModBlocks.SOFA_LIGHT_GRAY.get(), Blocks.WOOL.pick(DyeColor.LIGHT_GRAY));
        this.sofa(ModBlocks.SOFA_CYAN.get(), Blocks.WOOL.pick(DyeColor.CYAN));
        this.sofa(ModBlocks.SOFA_PURPLE.get(), Blocks.WOOL.pick(DyeColor.PURPLE));
        this.sofa(ModBlocks.SOFA_BLUE.get(), Blocks.WOOL.pick(DyeColor.BLUE));
        this.sofa(ModBlocks.SOFA_BROWN.get(), Blocks.WOOL.pick(DyeColor.BROWN));
        this.sofa(ModBlocks.SOFA_GREEN.get(), Blocks.WOOL.pick(DyeColor.GREEN));
        this.sofa(ModBlocks.SOFA_RED.get(), Blocks.WOOL.pick(DyeColor.RED));
        this.sofa(ModBlocks.SOFA_BLACK.get(), Blocks.WOOL.pick(DyeColor.BLACK));

        // Picket Fence
        this.picketFence(ModBlocks.PICKET_FENCE_WHITE.get(), Blocks.CONCRETE.pick(DyeColor.WHITE), Tags.Items.DYES_WHITE);
        this.picketFence(ModBlocks.PICKET_FENCE_ORANGE.get(), Blocks.CONCRETE.pick(DyeColor.ORANGE), Tags.Items.DYES_ORANGE);
        this.picketFence(ModBlocks.PICKET_FENCE_MAGENTA.get(), Blocks.CONCRETE.pick(DyeColor.MAGENTA), Tags.Items.DYES_MAGENTA);
        this.picketFence(ModBlocks.PICKET_FENCE_LIGHT_BLUE.get(), Blocks.CONCRETE.pick(DyeColor.LIGHT_BLUE), Tags.Items.DYES_LIGHT_BLUE);
        this.picketFence(ModBlocks.PICKET_FENCE_YELLOW.get(), Blocks.CONCRETE.pick(DyeColor.YELLOW), Tags.Items.DYES_YELLOW);
        this.picketFence(ModBlocks.PICKET_FENCE_LIME.get(), Blocks.CONCRETE.pick(DyeColor.LIME), Tags.Items.DYES_LIME);
        this.picketFence(ModBlocks.PICKET_FENCE_PINK.get(), Blocks.CONCRETE.pick(DyeColor.PINK), Tags.Items.DYES_PINK);
        this.picketFence(ModBlocks.PICKET_FENCE_GRAY.get(), Blocks.CONCRETE.pick(DyeColor.GRAY), Tags.Items.DYES_GRAY);
        this.picketFence(ModBlocks.PICKET_FENCE_LIGHT_GRAY.get(), Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY), Tags.Items.DYES_LIGHT_GRAY);
        this.picketFence(ModBlocks.PICKET_FENCE_CYAN.get(), Blocks.CONCRETE.pick(DyeColor.CYAN), Tags.Items.DYES_CYAN);
        this.picketFence(ModBlocks.PICKET_FENCE_PURPLE.get(), Blocks.CONCRETE.pick(DyeColor.PURPLE), Tags.Items.DYES_PURPLE);
        this.picketFence(ModBlocks.PICKET_FENCE_BLUE.get(), Blocks.CONCRETE.pick(DyeColor.BLUE), Tags.Items.DYES_BLUE);
        this.picketFence(ModBlocks.PICKET_FENCE_BROWN.get(), Blocks.CONCRETE.pick(DyeColor.BROWN), Tags.Items.DYES_BROWN);
        this.picketFence(ModBlocks.PICKET_FENCE_GREEN.get(), Blocks.CONCRETE.pick(DyeColor.GREEN), Tags.Items.DYES_GREEN);
        this.picketFence(ModBlocks.PICKET_FENCE_RED.get(), Blocks.CONCRETE.pick(DyeColor.RED), Tags.Items.DYES_RED);
        this.picketFence(ModBlocks.PICKET_FENCE_BLACK.get(), Blocks.CONCRETE.pick(DyeColor.BLACK), Tags.Items.DYES_BLACK);

        // Picket Gate
        this.picketGate(ModBlocks.PICKET_GATE_WHITE.get(), Blocks.CONCRETE.pick(DyeColor.WHITE), Tags.Items.DYES_WHITE);
        this.picketGate(ModBlocks.PICKET_GATE_ORANGE.get(), Blocks.CONCRETE.pick(DyeColor.ORANGE), Tags.Items.DYES_ORANGE);
        this.picketGate(ModBlocks.PICKET_GATE_MAGENTA.get(), Blocks.CONCRETE.pick(DyeColor.MAGENTA), Tags.Items.DYES_MAGENTA);
        this.picketGate(ModBlocks.PICKET_GATE_LIGHT_BLUE.get(), Blocks.CONCRETE.pick(DyeColor.LIGHT_BLUE), Tags.Items.DYES_LIGHT_BLUE);
        this.picketGate(ModBlocks.PICKET_GATE_YELLOW.get(), Blocks.CONCRETE.pick(DyeColor.YELLOW), Tags.Items.DYES_YELLOW);
        this.picketGate(ModBlocks.PICKET_GATE_LIME.get(), Blocks.CONCRETE.pick(DyeColor.LIME), Tags.Items.DYES_LIME);
        this.picketGate(ModBlocks.PICKET_GATE_PINK.get(), Blocks.CONCRETE.pick(DyeColor.PINK), Tags.Items.DYES_PINK);
        this.picketGate(ModBlocks.PICKET_GATE_GRAY.get(), Blocks.CONCRETE.pick(DyeColor.GRAY), Tags.Items.DYES_GRAY);
        this.picketGate(ModBlocks.PICKET_GATE_LIGHT_GRAY.get(), Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY), Tags.Items.DYES_LIGHT_GRAY);
        this.picketGate(ModBlocks.PICKET_GATE_CYAN.get(), Blocks.CONCRETE.pick(DyeColor.CYAN), Tags.Items.DYES_CYAN);
        this.picketGate(ModBlocks.PICKET_GATE_PURPLE.get(), Blocks.CONCRETE.pick(DyeColor.PURPLE), Tags.Items.DYES_PURPLE);
        this.picketGate(ModBlocks.PICKET_GATE_BLUE.get(), Blocks.CONCRETE.pick(DyeColor.BLUE), Tags.Items.DYES_BLUE);
        this.picketGate(ModBlocks.PICKET_GATE_BROWN.get(), Blocks.CONCRETE.pick(DyeColor.BROWN), Tags.Items.DYES_BROWN);
        this.picketGate(ModBlocks.PICKET_GATE_GREEN.get(), Blocks.CONCRETE.pick(DyeColor.GREEN), Tags.Items.DYES_GREEN);
        this.picketGate(ModBlocks.PICKET_GATE_RED.get(), Blocks.CONCRETE.pick(DyeColor.RED), Tags.Items.DYES_RED);
        this.picketGate(ModBlocks.PICKET_GATE_BLACK.get(), Blocks.CONCRETE.pick(DyeColor.BLACK), Tags.Items.DYES_BLACK);

        // Post Box
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.POST_BOX.get())
                .pattern("III")
                .pattern("ISI")
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Blocks.DYED_SHULKER_BOX.pick(DyeColor.BLUE))
                .unlockedBy("has_iron", this.has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_shulker_box", this.has(Blocks.DYED_SHULKER_BOX.pick(DyeColor.BLUE)))
                .save(this.output);

        // Hedge
        this.hedge(ModBlocks.HEDGE_OAK.get(), Blocks.OAK_LEAVES);
        this.hedge(ModBlocks.HEDGE_SPRUCE.get(), Blocks.SPRUCE_LEAVES);
        this.hedge(ModBlocks.HEDGE_BIRCH.get(), Blocks.BIRCH_LEAVES);
        this.hedge(ModBlocks.HEDGE_JUNGLE.get(), Blocks.JUNGLE_LEAVES);
        this.hedge(ModBlocks.HEDGE_ACACIA.get(), Blocks.ACACIA_LEAVES);
        this.hedge(ModBlocks.HEDGE_DARK_OAK.get(), Blocks.DARK_OAK_LEAVES);
        this.hedge(ModBlocks.HEDGE_MANGROVE.get(), Blocks.MANGROVE_LEAVES);
        this.hedge(ModBlocks.HEDGE_AZALEA.get(), Blocks.AZALEA_LEAVES);
        this.hedge(ModBlocks.HEDGE_FLOWERING_AZALEA.get(), Blocks.FLOWERING_AZALEA_LEAVES);

        // Rock Path
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.ROCK_PATH.get(), 16)
                .pattern("SA")
                .pattern("AS")
                .define('S', Blocks.STONE)
                .define('A', Blocks.ANDESITE)
                .unlockedBy("has_stone", this.has(Blocks.STONE))
                .unlockedBy("has_andesite", this.has(Blocks.ANDESITE))
                .save(this.output);

        // Trampoline
        this.trampoline(DyeColor.WHITE, Blocks.WOOL.pick(DyeColor.WHITE));
        this.trampoline(DyeColor.ORANGE, Blocks.WOOL.pick(DyeColor.ORANGE));
        this.trampoline(DyeColor.MAGENTA, Blocks.WOOL.pick(DyeColor.MAGENTA));
        this.trampoline(DyeColor.LIGHT_BLUE, Blocks.WOOL.pick(DyeColor.LIGHT_BLUE));
        this.trampoline(DyeColor.YELLOW, Blocks.WOOL.pick(DyeColor.YELLOW));
        this.trampoline(DyeColor.LIME, Blocks.WOOL.pick(DyeColor.LIME));
        this.trampoline(DyeColor.PINK, Blocks.WOOL.pick(DyeColor.PINK));
        this.trampoline(DyeColor.GRAY, Blocks.WOOL.pick(DyeColor.GRAY));
        this.trampoline(DyeColor.LIGHT_GRAY, Blocks.WOOL.pick(DyeColor.LIGHT_GRAY));
        this.trampoline(DyeColor.CYAN, Blocks.WOOL.pick(DyeColor.CYAN));
        this.trampoline(DyeColor.PURPLE, Blocks.WOOL.pick(DyeColor.PURPLE));
        this.trampoline(DyeColor.BLUE, Blocks.WOOL.pick(DyeColor.BLUE));
        this.trampoline(DyeColor.BROWN, Blocks.WOOL.pick(DyeColor.BROWN));
        this.trampoline(DyeColor.GREEN, Blocks.WOOL.pick(DyeColor.GREEN));
        this.trampoline(DyeColor.RED, Blocks.WOOL.pick(DyeColor.RED));
        this.trampoline(DyeColor.BLACK, Blocks.WOOL.pick(DyeColor.BLACK));

        // Cooler
        this.cooler(ModBlocks.COOLER_WHITE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.WHITE));
        this.cooler(ModBlocks.COOLER_ORANGE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.ORANGE));
        this.cooler(ModBlocks.COOLER_MAGENTA.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.MAGENTA));
        this.cooler(ModBlocks.COOLER_LIGHT_BLUE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.LIGHT_BLUE));
        this.cooler(ModBlocks.COOLER_YELLOW.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.YELLOW));
        this.cooler(ModBlocks.COOLER_LIME.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.LIME));
        this.cooler(ModBlocks.COOLER_PINK.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.PINK));
        this.cooler(ModBlocks.COOLER_GRAY.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.GRAY));
        this.cooler(ModBlocks.COOLER_LIGHT_GRAY.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.LIGHT_GRAY));
        this.cooler(ModBlocks.COOLER_CYAN.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.CYAN));
        this.cooler(ModBlocks.COOLER_PURPLE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.PURPLE));
        this.cooler(ModBlocks.COOLER_BLUE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.BLUE));
        this.cooler(ModBlocks.COOLER_BROWN.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.BROWN));
        this.cooler(ModBlocks.COOLER_GREEN.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.GREEN));
        this.cooler(ModBlocks.COOLER_RED.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.RED));
        this.cooler(ModBlocks.COOLER_BLACK.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.BLACK));

        // Grill
        this.grill(ModBlocks.GRILL_WHITE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.WHITE));
        this.grill(ModBlocks.GRILL_ORANGE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.ORANGE));
        this.grill(ModBlocks.GRILL_MAGENTA.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.MAGENTA));
        this.grill(ModBlocks.GRILL_LIGHT_BLUE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.LIGHT_BLUE));
        this.grill(ModBlocks.GRILL_YELLOW.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.YELLOW));
        this.grill(ModBlocks.GRILL_LIME.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.LIME));
        this.grill(ModBlocks.GRILL_PINK.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.PINK));
        this.grill(ModBlocks.GRILL_GRAY.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.GRAY));
        this.grill(ModBlocks.GRILL_LIGHT_GRAY.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.LIGHT_GRAY));
        this.grill(ModBlocks.GRILL_CYAN.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.CYAN));
        this.grill(ModBlocks.GRILL_PURPLE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.PURPLE));
        this.grill(ModBlocks.GRILL_BLUE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.BLUE));
        this.grill(ModBlocks.GRILL_BROWN.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.BROWN));
        this.grill(ModBlocks.GRILL_GREEN.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.GREEN));
        this.grill(ModBlocks.GRILL_RED.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.RED));
        this.grill(ModBlocks.GRILL_BLACK.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.BLACK));

        // Door Mat
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.DOOR_MAT.get())
                .pattern("WWW")
                .pattern("WWW")
                .define('W', Items.WHEAT)
                .unlockedBy("has_wheat", this.has(Tags.Items.CROPS_WHEAT))
                .save(this.output);

        // Diving Board
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.DIVING_BOARD.get())
                .pattern("CCC")
                .pattern("SSS")
                .pattern("G  ")
                .define('C', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('S', Tags.Items.SLIME_BALLS)
                .define('G', Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY))
                .unlockedBy("has_wheat", this.has(Tags.Items.CROPS_WHEAT))
                .save(this.output);

        // Kitchen Counter
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_WHITE.get(), Tags.Items.DYES_WHITE);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_ORANGE.get(), Tags.Items.DYES_ORANGE);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_MAGENTA.get(), Tags.Items.DYES_MAGENTA);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_LIGHT_BLUE.get(), Tags.Items.DYES_LIGHT_BLUE);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_YELLOW.get(), Tags.Items.DYES_YELLOW);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_LIME.get(), Tags.Items.DYES_LIME);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_PINK.get(), Tags.Items.DYES_PINK);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_GRAY.get(), Tags.Items.DYES_GRAY);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_LIGHT_GRAY.get(), Tags.Items.DYES_LIGHT_GRAY);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_CYAN.get(), Tags.Items.DYES_CYAN);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_PURPLE.get(), Tags.Items.DYES_PURPLE);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_BLUE.get(), Tags.Items.DYES_BLUE);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_BROWN.get(), Tags.Items.DYES_BROWN);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_GREEN.get(), Tags.Items.DYES_GREEN);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_RED.get(), Tags.Items.DYES_RED);
        this.coloredKitchenCounter(ModBlocks.KITCHEN_COUNTER_BLACK.get(), Tags.Items.DYES_BLACK);

        // Kitchen Drawer
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_WHITE.get(), Tags.Items.DYES_WHITE);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_ORANGE.get(), Tags.Items.DYES_ORANGE);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_MAGENTA.get(), Tags.Items.DYES_MAGENTA);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_LIGHT_BLUE.get(), Tags.Items.DYES_LIGHT_BLUE);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_YELLOW.get(), Tags.Items.DYES_YELLOW);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_LIME.get(), Tags.Items.DYES_LIME);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_PINK.get(), Tags.Items.DYES_PINK);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_GRAY.get(), Tags.Items.DYES_GRAY);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_LIGHT_GRAY.get(), Tags.Items.DYES_LIGHT_GRAY);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_CYAN.get(), Tags.Items.DYES_CYAN);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_PURPLE.get(), Tags.Items.DYES_PURPLE);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_BLUE.get(), Tags.Items.DYES_BLUE);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_BROWN.get(), Tags.Items.DYES_BROWN);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_GREEN.get(), Tags.Items.DYES_GREEN);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_RED.get(), Tags.Items.DYES_RED);
        this.coloredKitchenDrawer(ModBlocks.KITCHEN_DRAWER_BLACK.get(), Tags.Items.DYES_BLACK);

        // Kitchen Sink
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_WHITE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.WHITE));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_ORANGE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.ORANGE));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_MAGENTA.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.MAGENTA));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_LIGHT_BLUE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.LIGHT_BLUE));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_YELLOW.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.YELLOW));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_LIME.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.LIME));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_PINK.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.PINK));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_GRAY.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.GRAY));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_LIGHT_GRAY.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.LIGHT_GRAY));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_CYAN.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.CYAN));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_PURPLE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.PURPLE));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_BLUE.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.BLUE));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_BROWN.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.BROWN));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_GREEN.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.GREEN));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_RED.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.RED));
        this.coloredKitchenSink(ModBlocks.KITCHEN_SINK_BLACK.get(), Blocks.DYED_TERRACOTTA.pick(DyeColor.BLACK));

        // Fridges
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.FRIDGE_LIGHT.get())
                .pattern("CIC")
                .pattern("IBI")
                .pattern("CIC")
                .define('C', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Tags.Items.CHESTS_WOODEN)
                .group("fridge")
                .unlockedBy("has_iron", this.has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_chest", this.has(Tags.Items.CHESTS_WOODEN))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.FRIDGE_DARK.get())
                .pattern("CIC")
                .pattern("IBI")
                .pattern("CIC")
                .define('C', Blocks.CONCRETE.pick(DyeColor.GRAY))
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Tags.Items.CHESTS_WOODEN)
                .group("fridge")
                .unlockedBy("has_iron", this.has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_chest", this.has(Tags.Items.CHESTS_WOODEN))
                .save(this.output);

        // Spatula
        this.shaped(RecipeCategory.TOOLS, ModItems.SPATULA.get())
                .pattern("B")
                .pattern("I")
                .pattern("W")
                .define('B', Items.IRON_BARS)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('W', Blocks.WOOL.pick(DyeColor.BLACK))
                .unlockedBy("has_iron", this.has(Tags.Items.INGOTS_IRON))
                .save(this.output);

        // Brought forward from 1.12.2
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.BIRD_BATH.get())
                .pattern("***")
                .pattern(" * ")
                .pattern(" * ")
                .define('*', Blocks.STONE)
                .unlockedBy("has_stone", this.has(Blocks.STONE))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.CANDLE.get())
                .pattern("WWW")
                .pattern("SSS")
                .pattern(" C ")
                .define('W', Blocks.DYED_TERRACOTTA.pick(DyeColor.WHITE))
                .define('S', Blocks.STONE)
                .define('C', Blocks.COBBLESTONE)
                .unlockedBy("has_terracotta", this.has(Blocks.DYED_TERRACOTTA.pick(DyeColor.WHITE)))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.CHIMNEY.get())
                .pattern("CCC")
                .pattern("BBB")
                .pattern("BBB")
                .define('C', Blocks.COBBLESTONE)
                .define('B', Blocks.BRICKS)
                .unlockedBy("has_bricks", this.has(Blocks.BRICKS))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.MANTEL_PIECE.get())
                .pattern("BBB")
                .pattern("B B")
                .pattern("B B")
                .define('B', Blocks.BRICKS)
                .unlockedBy("has_bricks", this.has(Blocks.BRICKS))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.WREATH.get())
                .pattern("LLL")
                .pattern("L L")
                .pattern("LLL")
                .define('L', Blocks.OAK_LEAVES)
                .unlockedBy("has_leaves", this.has(Blocks.OAK_LEAVES))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.FAIRY_LIGHT.get())
                .pattern("SSS")
                .pattern("RGR")
                .define('S', Items.STRING)
                .define('R', Blocks.STAINED_GLASS.pick(DyeColor.RED))
                .define('G', Blocks.STAINED_GLASS.pick(DyeColor.GREEN))
                .unlockedBy("has_string", this.has(Items.STRING))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.TOILET.get())
                .pattern("QB ")
                .pattern("QQQ")
                .pattern(" Q ")
                .define('Q', Blocks.QUARTZ_BLOCK)
                .define('B', Blocks.STONE_BUTTON)
                .unlockedBy("has_quartz", this.has(Blocks.QUARTZ_BLOCK))
                .save(this.output);
        this.barStool(ModBlocks.BAR_STOOL_WHITE.get(), Blocks.WOOL.pick(DyeColor.WHITE));
        this.barStool(ModBlocks.BAR_STOOL_ORANGE.get(), Blocks.WOOL.pick(DyeColor.ORANGE));
        this.barStool(ModBlocks.BAR_STOOL_MAGENTA.get(), Blocks.WOOL.pick(DyeColor.MAGENTA));
        this.barStool(ModBlocks.BAR_STOOL_LIGHT_BLUE.get(), Blocks.WOOL.pick(DyeColor.LIGHT_BLUE));
        this.barStool(ModBlocks.BAR_STOOL_YELLOW.get(), Blocks.WOOL.pick(DyeColor.YELLOW));
        this.barStool(ModBlocks.BAR_STOOL_LIME.get(), Blocks.WOOL.pick(DyeColor.LIME));
        this.barStool(ModBlocks.BAR_STOOL_PINK.get(), Blocks.WOOL.pick(DyeColor.PINK));
        this.barStool(ModBlocks.BAR_STOOL_GRAY.get(), Blocks.WOOL.pick(DyeColor.GRAY));
        this.barStool(ModBlocks.BAR_STOOL_LIGHT_GRAY.get(), Blocks.WOOL.pick(DyeColor.LIGHT_GRAY));
        this.barStool(ModBlocks.BAR_STOOL_CYAN.get(), Blocks.WOOL.pick(DyeColor.CYAN));
        this.barStool(ModBlocks.BAR_STOOL_PURPLE.get(), Blocks.WOOL.pick(DyeColor.PURPLE));
        this.barStool(ModBlocks.BAR_STOOL_BLUE.get(), Blocks.WOOL.pick(DyeColor.BLUE));
        this.barStool(ModBlocks.BAR_STOOL_BROWN.get(), Blocks.WOOL.pick(DyeColor.BROWN));
        this.barStool(ModBlocks.BAR_STOOL_GREEN.get(), Blocks.WOOL.pick(DyeColor.GREEN));
        this.barStool(ModBlocks.BAR_STOOL_RED.get(), Blocks.WOOL.pick(DyeColor.RED));
        this.barStool(ModBlocks.BAR_STOOL_BLACK.get(), Blocks.WOOL.pick(DyeColor.BLACK));
        // Lights (1.12.2)
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.CEILING_LIGHT.get(), 4)
                .pattern("O")
                .pattern("S")
                .pattern("G")
                .define('O', Blocks.CONCRETE.pick(DyeColor.GRAY))
                .define('S', Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY))
                .define('G', Blocks.GLOWSTONE)
                .unlockedBy("has_glowstone", this.has(Blocks.GLOWSTONE))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.MODERN_LIGHT.get(), 4)
                .pattern("CGC")
                .pattern("PPP")
                .define('C', Blocks.CONCRETE.pick(DyeColor.GRAY))
                .define('G', Blocks.GLOWSTONE)
                .define('P', Blocks.GLASS_PANE)
                .unlockedBy("has_glowstone", this.has(Blocks.GLOWSTONE))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.RANGE_HOOD.get())
                .pattern(" I ")
                .pattern(" I ")
                .pattern("ISI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Blocks.SEA_LANTERN)
                .unlockedBy("has_sea_lantern", this.has(Blocks.SEA_LANTERN))
                .save(this.output);
        this.shaped(RecipeCategory.REDSTONE, ModBlocks.LIGHT_SWITCH.get())
                .pattern("CCC")
                .pattern("CLC")
                .pattern("CCC")
                .define('C', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('L', Blocks.LEVER)
                .unlockedBy("has_lever", this.has(Blocks.LEVER))
                .save(this.output);
        // An axe and a log make eight firewood; the axe takes one use of damage
        CuttingRecipe firewood = new CuttingRecipe(new Recipe.CommonInfo(true), new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, ""),
                new ItemStackTemplate(ModItems.FIREWOOD.get(), 8), List.of(this.tag(ItemTags.AXES), this.tag(ItemTags.LOGS_THAT_BURN)));
        this.output.accept(recipeKey(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "firewood")), firewood, null);
        this.lamp(ModBlocks.LAMP_WHITE.get(), Blocks.WOOL.pick(DyeColor.WHITE));
        this.lamp(ModBlocks.LAMP_ORANGE.get(), Blocks.WOOL.pick(DyeColor.ORANGE));
        this.lamp(ModBlocks.LAMP_MAGENTA.get(), Blocks.WOOL.pick(DyeColor.MAGENTA));
        this.lamp(ModBlocks.LAMP_LIGHT_BLUE.get(), Blocks.WOOL.pick(DyeColor.LIGHT_BLUE));
        this.lamp(ModBlocks.LAMP_YELLOW.get(), Blocks.WOOL.pick(DyeColor.YELLOW));
        this.lamp(ModBlocks.LAMP_LIME.get(), Blocks.WOOL.pick(DyeColor.LIME));
        this.lamp(ModBlocks.LAMP_PINK.get(), Blocks.WOOL.pick(DyeColor.PINK));
        this.lamp(ModBlocks.LAMP_GRAY.get(), Blocks.WOOL.pick(DyeColor.GRAY));
        this.lamp(ModBlocks.LAMP_LIGHT_GRAY.get(), Blocks.WOOL.pick(DyeColor.LIGHT_GRAY));
        this.lamp(ModBlocks.LAMP_CYAN.get(), Blocks.WOOL.pick(DyeColor.CYAN));
        this.lamp(ModBlocks.LAMP_PURPLE.get(), Blocks.WOOL.pick(DyeColor.PURPLE));
        this.lamp(ModBlocks.LAMP_BLUE.get(), Blocks.WOOL.pick(DyeColor.BLUE));
        this.lamp(ModBlocks.LAMP_BROWN.get(), Blocks.WOOL.pick(DyeColor.BROWN));
        this.lamp(ModBlocks.LAMP_GREEN.get(), Blocks.WOOL.pick(DyeColor.GREEN));
        this.lamp(ModBlocks.LAMP_RED.get(), Blocks.WOOL.pick(DyeColor.RED));
        this.lamp(ModBlocks.LAMP_BLACK.get(), Blocks.WOOL.pick(DyeColor.BLACK));

        // Bathroom (1.12.2)
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.BASIN.get())
                .pattern("BIB")
                .pattern("QQQ")
                .pattern(" Q ")
                .define('B', Blocks.STONE_BUTTON)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('Q', Blocks.QUARTZ_BLOCK)
                .unlockedBy("has_quartz", this.has(Blocks.QUARTZ_BLOCK))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.TAP.get())
                .pattern(" I ")
                .pattern("SSS")
                .pattern("  S")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Blocks.STONE)
                .unlockedBy("has_iron", this.has(Tags.Items.INGOTS_IRON))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.SHOWER.get())
                .pattern("QGQ")
                .pattern("QGQ")
                .pattern("QGQ")
                .define('Q', Blocks.QUARTZ_BLOCK)
                .define('G', Blocks.GLASS)
                .unlockedBy("has_quartz", this.has(Blocks.QUARTZ_BLOCK))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.SHOWER_HEAD.get())
                .pattern("II ")
                .pattern(" I ")
                .pattern("SSS")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Blocks.STONE)
                .unlockedBy("has_iron", this.has(Tags.Items.INGOTS_IRON))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.BATH.get())
                .pattern("B  ")
                .pattern("Q Q")
                .pattern("QQQ")
                .define('B', Blocks.STONE_BUTTON)
                .define('Q', Blocks.QUARTZ_BLOCK)
                .unlockedBy("has_quartz", this.has(Blocks.QUARTZ_BLOCK))
                .save(this.output);

        // Modern furniture (1.12.2)
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.MODERN_CHAIR.get())
                .pattern("C  ")
                .pattern("CCC")
                .pattern("GGG")
                .define('C', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('G', Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY))
                .unlockedBy("has_concrete", this.has(Blocks.CONCRETE.pick(DyeColor.WHITE)))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.MODERN_TABLE.get())
                .pattern("CCC")
                .pattern("G G")
                .pattern("G G")
                .define('C', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('G', Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY))
                .unlockedBy("has_concrete", this.has(Blocks.CONCRETE.pick(DyeColor.WHITE)))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.MODERN_OUTDOOR_TABLE.get())
                .pattern("CGC")
                .pattern("L L")
                .pattern("L L")
                .define('C', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('G', Blocks.GLASS)
                .define('L', Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY))
                .unlockedBy("has_concrete", this.has(Blocks.CONCRETE.pick(DyeColor.WHITE)))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.MODERN_COFFEE_TABLE.get())
                .pattern("TTT")
                .pattern("L L")
                .define('T', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('L', Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY))
                .unlockedBy("has_concrete", this.has(Blocks.CONCRETE.pick(DyeColor.WHITE)))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.MODERN_STAIR.get(), 4)
                .pattern("C  ")
                .pattern("IC ")
                .pattern(" IC")
                .define('C', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_concrete", this.has(Blocks.CONCRETE.pick(DyeColor.WHITE)))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, ModBlocks.MODERN_WINDOW.get(), 8)
                .pattern("CCC")
                .pattern("CGC")
                .pattern("CCC")
                .define('C', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('G', Blocks.GLASS_PANE)
                .unlockedBy("has_concrete", this.has(Blocks.CONCRETE.pick(DyeColor.WHITE)))
                .save(this.output);
        this.shaped(RecipeCategory.REDSTONE, ModBlocks.MODERN_SLIDING_DOOR.get())
                .pattern("CC")
                .pattern("GG")
                .pattern("CC")
                .define('C', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('G', Blocks.GLASS_PANE)
                .unlockedBy("has_concrete", this.has(Blocks.CONCRETE.pick(DyeColor.WHITE)))
                .save(this.output);
        this.modernCouch(ModBlocks.MODERN_COUCH_WHITE.get(), ModBlocks.SOFA_WHITE.get());
        this.modernBed(ModBlocks.MODERN_BED_WHITE.get(), Blocks.BED.pick(DyeColor.WHITE));
        this.modernCouch(ModBlocks.MODERN_COUCH_ORANGE.get(), ModBlocks.SOFA_ORANGE.get());
        this.modernBed(ModBlocks.MODERN_BED_ORANGE.get(), Blocks.BED.pick(DyeColor.ORANGE));
        this.modernCouch(ModBlocks.MODERN_COUCH_MAGENTA.get(), ModBlocks.SOFA_MAGENTA.get());
        this.modernBed(ModBlocks.MODERN_BED_MAGENTA.get(), Blocks.BED.pick(DyeColor.MAGENTA));
        this.modernCouch(ModBlocks.MODERN_COUCH_LIGHT_BLUE.get(), ModBlocks.SOFA_LIGHT_BLUE.get());
        this.modernBed(ModBlocks.MODERN_BED_LIGHT_BLUE.get(), Blocks.BED.pick(DyeColor.LIGHT_BLUE));
        this.modernCouch(ModBlocks.MODERN_COUCH_YELLOW.get(), ModBlocks.SOFA_YELLOW.get());
        this.modernBed(ModBlocks.MODERN_BED_YELLOW.get(), Blocks.BED.pick(DyeColor.YELLOW));
        this.modernCouch(ModBlocks.MODERN_COUCH_LIME.get(), ModBlocks.SOFA_LIME.get());
        this.modernBed(ModBlocks.MODERN_BED_LIME.get(), Blocks.BED.pick(DyeColor.LIME));
        this.modernCouch(ModBlocks.MODERN_COUCH_PINK.get(), ModBlocks.SOFA_PINK.get());
        this.modernBed(ModBlocks.MODERN_BED_PINK.get(), Blocks.BED.pick(DyeColor.PINK));
        this.modernCouch(ModBlocks.MODERN_COUCH_GRAY.get(), ModBlocks.SOFA_GRAY.get());
        this.modernBed(ModBlocks.MODERN_BED_GRAY.get(), Blocks.BED.pick(DyeColor.GRAY));
        this.modernCouch(ModBlocks.MODERN_COUCH_LIGHT_GRAY.get(), ModBlocks.SOFA_LIGHT_GRAY.get());
        this.modernBed(ModBlocks.MODERN_BED_LIGHT_GRAY.get(), Blocks.BED.pick(DyeColor.LIGHT_GRAY));
        this.modernCouch(ModBlocks.MODERN_COUCH_CYAN.get(), ModBlocks.SOFA_CYAN.get());
        this.modernBed(ModBlocks.MODERN_BED_CYAN.get(), Blocks.BED.pick(DyeColor.CYAN));
        this.modernCouch(ModBlocks.MODERN_COUCH_PURPLE.get(), ModBlocks.SOFA_PURPLE.get());
        this.modernBed(ModBlocks.MODERN_BED_PURPLE.get(), Blocks.BED.pick(DyeColor.PURPLE));
        this.modernCouch(ModBlocks.MODERN_COUCH_BLUE.get(), ModBlocks.SOFA_BLUE.get());
        this.modernBed(ModBlocks.MODERN_BED_BLUE.get(), Blocks.BED.pick(DyeColor.BLUE));
        this.modernCouch(ModBlocks.MODERN_COUCH_BROWN.get(), ModBlocks.SOFA_BROWN.get());
        this.modernBed(ModBlocks.MODERN_BED_BROWN.get(), Blocks.BED.pick(DyeColor.BROWN));
        this.modernCouch(ModBlocks.MODERN_COUCH_GREEN.get(), ModBlocks.SOFA_GREEN.get());
        this.modernBed(ModBlocks.MODERN_BED_GREEN.get(), Blocks.BED.pick(DyeColor.GREEN));
        this.modernCouch(ModBlocks.MODERN_COUCH_RED.get(), ModBlocks.SOFA_RED.get());
        this.modernBed(ModBlocks.MODERN_BED_RED.get(), Blocks.BED.pick(DyeColor.RED));
        this.modernCouch(ModBlocks.MODERN_COUCH_BLACK.get(), ModBlocks.SOFA_BLACK.get());
        this.modernBed(ModBlocks.MODERN_BED_BLACK.get(), Blocks.BED.pick(DyeColor.BLACK));

        // Cooking Recipes
        this.cookingRecipesForMethod("grill_cooking", GrillCookingRecipe::new, 600);

        // Freezing Recipes
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.WATER_BUCKET), RecipeCategory.MISC, CookingBookCategory.MISC, Blocks.ICE, 1, 1000, FreezerSolidifyRecipe::new).unlockedBy("has_water", this.has(Items.WATER_BUCKET)).save(this.output, "ice_from_freezing");
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.ICE), RecipeCategory.MISC, CookingBookCategory.MISC, Blocks.PACKED_ICE, 1, 2000, FreezerSolidifyRecipe::new).unlockedBy("has_ice", this.has(Items.ICE)).save(this.output, "packed_ice_from_freezing");
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.PACKED_ICE), RecipeCategory.MISC, CookingBookCategory.MISC, Blocks.BLUE_ICE, 1, 4000, FreezerSolidifyRecipe::new).unlockedBy("has_packed_ice", this.has(Items.PACKED_ICE)).save(this.output, "blue_ice_from_freezing");
    }

    // 1.12.2 made the modern couch from its classic couch; the sofa takes that part here
    private void modernCouch(ItemLike couch, ItemLike sofa)
    {
        this.shaped(RecipeCategory.DECORATIONS, couch)
                .pattern("OCO")
                .pattern(" S ")
                .define('O', Blocks.OAK_PLANKS)
                .define('C', sofa)
                .define('S', Blocks.SPRUCE_PLANKS)
                .group("modern_couch")
                .unlockedBy("has_sofa", this.has(sofa))
                .save(this.output);
    }

    private void modernBed(ItemLike modernBed, ItemLike bed)
    {
        this.shaped(RecipeCategory.DECORATIONS, modernBed)
                .pattern(" O ")
                .pattern("OCO")
                .pattern(" O ")
                .define('O', Blocks.CONCRETE.pick(DyeColor.GRAY))
                .define('C', bed)
                .group("modern_bed")
                .unlockedBy("has_bed", this.has(bed))
                .save(this.output);
    }

    private void lamp(ItemLike lamp, ItemLike wool)
    {
        this.shaped(RecipeCategory.DECORATIONS, lamp, 2)
                .pattern("WWW")
                .pattern("WGW")
                .pattern(" O ")
                .define('W', wool)
                .define('G', Blocks.GLOWSTONE)
                .define('O', Blocks.OBSIDIAN)
                .group("lamp")
                .unlockedBy("has_glowstone", this.has(Blocks.GLOWSTONE))
                .save(this.output);
    }

    private void barStool(ItemLike stool, ItemLike wool)
    {
        this.shaped(RecipeCategory.DECORATIONS, stool)
                .pattern("WWW")
                .pattern("CCC")
                .pattern("Q Q")
                .define('W', wool)
                .define('C', Blocks.DYED_TERRACOTTA.pick(DyeColor.CYAN))
                .define('Q', Blocks.QUARTZ_BLOCK)
                .group("bar_stool")
                .unlockedBy("has_wool", this.has(wool))
                .unlockedBy("has_quartz", this.has(Blocks.QUARTZ_BLOCK))
                .save(this.output);
    }

    private void table(ItemLike table, ItemLike log, ItemLike planks)
    {
        this.shaped(RecipeCategory.DECORATIONS, table, 4)
                .pattern("LLL")
                .pattern(" P ")
                .pattern(" P ")
                .define('L', log)
                .define('P', planks)
                .group("table")
                .unlockedBy("has_log", this.has(log))
                .unlockedBy("has_planks", this.has(planks))
                .save(this.output);
    }

    private void chair(ItemLike chair, ItemLike log, ItemLike planks)
    {
        this.shaped(RecipeCategory.DECORATIONS, chair, 4)
                .pattern("L  ")
                .pattern("LLL")
                .pattern("P P")
                .define('L', log)
                .define('P', planks)
                .group("chair")
                .unlockedBy("has_log", this.has(log))
                .unlockedBy("has_planks", this.has(planks))
                .save(this.output);
    }

    private void coffeeTable(ItemLike table, ItemLike log, ItemLike planks)
    {
        this.shaped(RecipeCategory.DECORATIONS, table, 4)
                .pattern("LLL")
                .pattern("P P")
                .define('L', log)
                .define('P', planks)
                .group("coffee_table")
                .unlockedBy("has_log", this.has(log))
                .unlockedBy("has_planks", this.has(planks))
                .save(this.output);
    }

    private void cabinet(ItemLike cabinet, ItemLike log, ItemLike planks)
    {
        this.shaped(RecipeCategory.DECORATIONS, cabinet, 2)
                .pattern("PPL")
                .pattern("P L")
                .pattern("PPL")
                .define('L', log)
                .define('P', planks)
                .group("cabinet")
                .unlockedBy("has_log", this.has(log))
                .unlockedBy("has_planks", this.has(planks))
                .save(this.output);
    }

    private void bedsideCabinet(ItemLike cabinet, ItemLike log, ItemLike planks)
    {
        this.shaped(RecipeCategory.DECORATIONS, cabinet, 2)
                .pattern("LLL")
                .pattern("P P")
                .pattern("PPP")
                .define('L', log)
                .define('P', planks)
                .group("bedside_cabinet")
                .unlockedBy("has_log", this.has(log))
                .unlockedBy("has_planks", this.has(planks))
                .save(this.output);
    }

    private void desk(ItemLike desk, ItemLike log, ItemLike planks)
    {
        this.shaped(RecipeCategory.DECORATIONS, desk, 2)
                .pattern("LLL")
                .pattern("P P")
                .pattern("P P")
                .define('L', log)
                .define('P', planks)
                .group("desk")
                .unlockedBy("has_log", this.has(log))
                .unlockedBy("has_planks", this.has(planks))
                .save(this.output);
    }

    private void deskCabinet(ItemLike desk, ItemLike log, ItemLike planks)
    {
        this.shaped(RecipeCategory.DECORATIONS, desk, 2)
                .pattern("LLL")
                .pattern("PPP")
                .pattern("P P")
                .define('L', log)
                .define('P', planks)
                .group("desk_cabinet")
                .unlockedBy("has_log", this.has(log))
                .unlockedBy("has_planks", this.has(planks))
                .save(this.output);
    }

    private void sofa(ItemLike sofa, ItemLike wool)
    {
        this.shaped(RecipeCategory.DECORATIONS, sofa, 2)
                .pattern("W  ")
                .pattern("WWW")
                .pattern("LLL")
                .define('W', wool)
                .define('L', ItemTags.LOGS)
                .group("sofa")
                .unlockedBy("has_wool", this.has(wool))
                .save(this.output);
    }

    private void blinds(ItemLike blinds, ItemLike log)
    {
        this.shaped(RecipeCategory.DECORATIONS, blinds, 2)
                .pattern("LLL")
                .pattern("SSS")
                .pattern("SSS")
                .define('L', log)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("blinds")
                .unlockedBy("has_log", this.has(log))
                .save(this.output);
    }

    private void upgradedFence(ItemLike fence, ItemLike log)
    {
        this.shaped(RecipeCategory.DECORATIONS, fence, 12)
                .pattern("LSL")
                .pattern("LSL")
                .define('L', log)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("upgraded_fence")
                .unlockedBy("has_log", this.has(log))
                .save(this.output);
    }

    private void upgradedGate(ItemLike gate, ItemLike log)
    {
        this.shaped(RecipeCategory.DECORATIONS, gate, 2)
                .pattern("LGL")
                .define('L', log)
                .define('G', Tags.Items.FENCE_GATES_WOODEN)
                .group("upgraded_gate")
                .unlockedBy("has_log", this.has(log))
                .unlockedBy("has_gate", this.has(Tags.Items.FENCE_GATES_WOODEN))
                .save(this.output);
    }

    private void picketFence(ItemLike fence, ItemLike concrete, TagKey<Item> dye)
    {
        this.shaped(RecipeCategory.DECORATIONS, fence, 12)
                .pattern("CSC")
                .pattern("CSC")
                .define('C', concrete)
                .define('S', Tags.Items.RODS_WOODEN)
                .group("picket_fence")
                .unlockedBy("has_concrete", this.has(concrete))
                .save(this.output);
        if (fence == ModBlocks.PICKET_FENCE_WHITE.get())
            return;
        Identifier registryName = BuiltInRegistries.ITEM.getKey(fence.asItem());
        this.shaped(RecipeCategory.DECORATIONS, fence, 8)
                .pattern("FFF")
                .pattern("FDF")
                .pattern("FFF")
                .define('F', ModBlocks.PICKET_FENCE_WHITE.get())
                .define('D', dye)
                .group("picket_fence")
                .unlockedBy("has_fence", this.has(ModBlocks.PICKET_FENCE_WHITE.get()))
                .unlockedBy("has_dye", this.has(dye))
                .save(this.output, recipeKey(registryName.withPrefix("dye_")));
    }

    private void picketGate(ItemLike gate, ItemLike concrete, TagKey<Item> dye)
    {
        this.shaped(RecipeCategory.DECORATIONS, gate, 2)
                .pattern("CGC")
                .define('C', concrete)
                .define('G', Tags.Items.FENCE_GATES_WOODEN)
                .group("picket_fence")
                .unlockedBy("has_concrete", this.has(concrete))
                .unlockedBy("has_gate", this.has(Tags.Items.FENCE_GATES_WOODEN))
                .save(this.output);
        if (gate == ModBlocks.PICKET_GATE_WHITE.get())
            return;
        Identifier registryName = BuiltInRegistries.ITEM.getKey(gate.asItem());
        this.shaped(RecipeCategory.DECORATIONS, gate, 8)
                .pattern("GGG")
                .pattern("GDG")
                .pattern("GGG")
                .define('G', ModBlocks.PICKET_GATE_WHITE.get())
                .define('D', dye)
                .group("picket_fence")
                .unlockedBy("has_gate", this.has(ModBlocks.PICKET_GATE_WHITE.get()))
                .unlockedBy("has_dye", this.has(dye))
                .save(this.output, recipeKey(registryName.withPrefix("dye_")));
    }

    private void crate(ItemLike crate, ItemLike log, ItemLike planks)
    {
        this.shaped(RecipeCategory.DECORATIONS, crate, 2)
                .pattern("LPL")
                .pattern("P P")
                .pattern("LPL")
                .define('L', log)
                .define('P', planks)
                .group("crate")
                .unlockedBy("has_log", this.has(log))
                .unlockedBy("has_planks", this.has(planks))
                .save(this.output);
    }

    private void parkBench(ItemLike bench, ItemLike log, ItemLike planks)
    {
        this.shaped(RecipeCategory.DECORATIONS, bench, 4)
                .pattern("PPP")
                .pattern("PPP")
                .pattern("L L")
                .define('L', log)
                .define('P', planks)
                .group("park_bench")
                .unlockedBy("has_log", this.has(log))
                .unlockedBy("has_planks", this.has(planks))
                .save(this.output);
    }

    private void mailBox(ItemLike mailbox, ItemLike fence)
    {
        this.shaped(RecipeCategory.DECORATIONS, mailbox)
                .pattern("C")
                .pattern("F")
                .pattern("F")
                .define('C', Tags.Items.CHESTS_WOODEN)
                .define('F', fence)
                .group("mail_box")
                .unlockedBy("has_postbox", this.has(ModBlocks.POST_BOX.get())) // Mailbox depends on post box so if you have a postbox unlock all mailboxes
                .unlockedBy("has_chest", this.has(Tags.Items.CHESTS_WOODEN))
                .save(this.output);
    }

    private void hedge(ItemLike hedge, ItemLike leaves)
    {
        this.shaped(RecipeCategory.DECORATIONS, hedge, 12)
                .pattern("LLL")
                .pattern("LLL")
                .define('L', leaves)
                .group("hedge")
                .unlockedBy("has_leaves", this.has(leaves))
                .save(this.output);
    }

    private void trampoline(DyeColor color, ItemLike wool)
    {
        // The trampoline item carries its colour as the base colour component
        DataComponentPatch components = DataComponentPatch.builder().set(DataComponents.BASE_COLOR, color).build();
        ItemStackTemplate trampoline = new ItemStackTemplate(ModBlocks.TRAMPOLINE.get().asItem(), components);
        this.shaped(RecipeCategory.DECORATIONS, trampoline)
                .pattern("WSW")
                .pattern("I I")
                .pattern("III")
                .define('W', wool)
                .define('S', Tags.Items.SLIME_BALLS)
                .define('I', Tags.Items.INGOTS_IRON)
                .group("trampoline")
                .unlockedBy("has_wool", this.has(wool))
                .unlockedBy("has_iron", this.has(Tags.Items.INGOTS_IRON))
                .save(this.output, recipeKey(Identifier.fromNamespaceAndPath(Reference.MOD_ID, color.getName() + "_trampoline")));
    }

    private void cooler(ItemLike cooler, ItemLike terracotta)
    {
        this.shaped(RecipeCategory.DECORATIONS, cooler, 2)
                .pattern("TTT")
                .pattern("WCW")
                .pattern("TTT")
                .define('T', terracotta)
                .define('W', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('C', Tags.Items.CHESTS_WOODEN)
                .group("trampoline")
                .unlockedBy("has_terracotta", this.has(terracotta))
                .unlockedBy("has_chest", this.has(Tags.Items.CHESTS_WOODEN))
                .save(this.output);
    }

    private void grill(ItemLike grill, ItemLike terracotta)
    {
        this.shaped(RecipeCategory.DECORATIONS, grill)
                .pattern("TBT")
                .pattern("I I")
                .pattern("I I")
                .define('T', terracotta)
                .define('B', Items.IRON_BARS)
                .define('I', Tags.Items.INGOTS_IRON)
                .group("grill")
                .unlockedBy("has_terracotta", this.has(terracotta))
                .unlockedBy("has_iron", this.has(Tags.Items.INGOTS_IRON))
                .save(this.output);
    }

    private void kitchenCounter(ItemLike counter, ItemLike log, ItemLike planks)
    {
        this.shaped(RecipeCategory.DECORATIONS, counter, 8)
                .pattern("LLL")
                .pattern("PPP")
                .pattern("PPP")
                .define('L', log)
                .define('P', planks)
                .group("kitchen_counter")
                .unlockedBy("has_log", this.has(log))
                .unlockedBy("has_planks", this.has(planks))
                .save(this.output);
    }

    private void coloredKitchenCounter(ItemLike counter, TagKey<Item> dye)
    {
        this.shaped(RecipeCategory.DECORATIONS, counter, 8)
                .pattern("SDS")
                .pattern("CCC")
                .pattern("CCC")
                .define('S', Blocks.TERRACOTTA)
                .define('D', dye)
                .define('C', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .group("kitchen_counter")
                .unlockedBy("has_stone", this.has(Blocks.TERRACOTTA))
                .unlockedBy("has_concrete", this.has(Blocks.CONCRETE.pick(DyeColor.WHITE)))
                .save(this.output);
    }

    private void kitchenDrawer(ItemLike counter, ItemLike log, ItemLike planks)
    {
        this.shaped(RecipeCategory.DECORATIONS, counter, 4)
                .pattern("LLL")
                .pattern("PCP")
                .pattern("PPP")
                .define('L', log)
                .define('P', planks)
                .define('C', Tags.Items.CHESTS_WOODEN)
                .group("kitchen_drawer")
                .unlockedBy("has_log", this.has(log))
                .unlockedBy("has_planks", this.has(planks))
                .unlockedBy("has_chest", this.has(Tags.Items.CHESTS_WOODEN))
                .save(this.output);
    }

    private void coloredKitchenDrawer(ItemLike counter, TagKey<Item> dye)
    {
        this.shaped(RecipeCategory.DECORATIONS, counter, 4)
                .pattern("SDS")
                .pattern("CBC")
                .pattern("CCC")
                .define('S', Blocks.TERRACOTTA)
                .define('D', dye)
                .define('C', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('B', Tags.Items.CHESTS_WOODEN)
                .group("kitchen_drawer")
                .unlockedBy("has_terracotta", this.has(Blocks.TERRACOTTA))
                .unlockedBy("has_concrete", this.has(Blocks.CONCRETE.pick(DyeColor.WHITE)))
                .unlockedBy("has_chest", this.has(Tags.Items.CHESTS_WOODEN))
                .save(this.output);
    }

    private void kitchenSink(ItemLike sink, ItemLike top, ItemLike bottom)
    {
        this.shaped(RecipeCategory.DECORATIONS, sink, 2)
                .pattern("CIC")
                .pattern("PBP")
                .pattern("PPP")
                .define('C', top)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', bottom)
                .define('B', Items.BUCKET)
                .group("kitchen_sink")
                .unlockedBy("has_top", this.has(top))
                .unlockedBy("has_bottom", this.has(bottom))
                .unlockedBy("has_bucket", this.has(Items.BUCKET))
                .save(this.output);
    }

    private void coloredKitchenSink(ItemLike sink, ItemLike top)
    {
        this.shaped(RecipeCategory.DECORATIONS, sink, 2)
                .pattern("CIC")
                .pattern("PBP")
                .pattern("PPP")
                .define('C', top)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', Blocks.CONCRETE.pick(DyeColor.WHITE))
                .define('B', Items.BUCKET)
                .group("kitchen_sink")
                .unlockedBy("has_top", this.has(top))
                .unlockedBy("has_concrete", this.has(Blocks.CONCRETE.pick(DyeColor.WHITE)))
                .unlockedBy("has_bucket", this.has(Items.BUCKET))
                .save(this.output);
    }

    private <T extends AbstractCookingRecipe> void cookingRecipesForMethod(String method, AbstractCookingRecipe.Factory<T> factory, int cookingTime)
    {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.BEEF), RecipeCategory.FOOD, CookingBookCategory.FOOD, Items.COOKED_BEEF, 0.35F, cookingTime, factory).unlockedBy("has_beef", this.has(Items.BEEF)).save(this.output, "cooked_beef_from_" + method);
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.CHICKEN), RecipeCategory.FOOD, CookingBookCategory.FOOD, Items.COOKED_CHICKEN, 0.35F, cookingTime, factory).unlockedBy("has_chicken", this.has(Items.CHICKEN)).save(this.output, "cooked_chicken_from_" + method);
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.COD), RecipeCategory.FOOD, CookingBookCategory.FOOD, Items.COOKED_COD, 0.35F, cookingTime, factory).unlockedBy("has_cod", this.has(Items.COD)).save(this.output, "cooked_cod_from_" + method);
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Blocks.KELP), RecipeCategory.FOOD, CookingBookCategory.FOOD, Items.DRIED_KELP, 0.1F, cookingTime, factory).unlockedBy("has_kelp", this.has(Blocks.KELP)).save(this.output, "dried_kelp_from_" + method);
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.SALMON), RecipeCategory.FOOD, CookingBookCategory.FOOD, Items.COOKED_SALMON, 0.35F, cookingTime, factory).unlockedBy("has_salmon", this.has(Items.SALMON)).save(this.output, "cooked_salmon_from_" + method);
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.MUTTON), RecipeCategory.FOOD, CookingBookCategory.FOOD, Items.COOKED_MUTTON, 0.35F, cookingTime, factory).unlockedBy("has_mutton", this.has(Items.MUTTON)).save(this.output, "cooked_mutton_from_" + method);
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.PORKCHOP), RecipeCategory.FOOD, CookingBookCategory.FOOD, Items.COOKED_PORKCHOP, 0.35F, cookingTime, factory).unlockedBy("has_porkchop", this.has(Items.PORKCHOP)).save(this.output, "cooked_porkchop_from_" + method);
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.POTATO), RecipeCategory.FOOD, CookingBookCategory.FOOD, Items.BAKED_POTATO, 0.35F, cookingTime, factory).unlockedBy("has_potato", this.has(Items.POTATO)).save(this.output, "baked_potato_from_" + method);
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.RABBIT), RecipeCategory.FOOD, CookingBookCategory.FOOD, Items.COOKED_RABBIT, 0.35F, cookingTime, factory).unlockedBy("has_rabbit", this.has(Items.RABBIT)).save(this.output, "cooked_rabbit_from_" + method);
    }

    private static ResourceKey<Recipe<?>> recipeKey(Identifier id)
    {
        return ResourceKey.create(Registries.RECIPE, id);
    }
}