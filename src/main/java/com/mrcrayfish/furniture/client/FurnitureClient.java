package com.mrcrayfish.furniture.client;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.client.color.TrampolineColorSource;
import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.client.gui.screen.DoorMatScreen;
import com.mrcrayfish.furniture.network.message.S2CMessageFlipGrill;
import com.mrcrayfish.furniture.network.message.S2CMessageOpenDoorMat;
import com.mrcrayfish.furniture.tileentity.DoorMatBlockEntity;
import com.mrcrayfish.furniture.tileentity.GrillBlockEntity;
import com.mrcrayfish.furniture.tileentity.TrampolineBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.neoforged.api.distmarker.Dist;
import com.mrcrayfish.furniture.client.gui.screen.ScreenSettingsScreen;
import com.mrcrayfish.furniture.network.message.S2CMessageOpenScreenSettings;
import com.mrcrayfish.furniture.tileentity.ScreenBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.common.Mod;
import com.mrcrayfish.furniture.util.RecipeLookup;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Arrays;
import java.util.List;

/**
 * The client side of the mod: screens, renderers, colours and the client's payload handlers. Only the client loads this class, so
 * nothing here can reach the dedicated server.
 */
@Mod(value = Reference.MOD_ID, dist = Dist.CLIENT)
public class FurnitureClient
{
    private static final int WHITE = -1;

    public FurnitureClient(IEventBus modBus, ModContainer container)
    {
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        modBus.addListener(FurnitureClient::onRegisterBlockTints);
        modBus.addListener(FurnitureClient::onRegisterItemTints);
        modBus.addListener(FurnitureClient::onRegisterPayloadHandlers);
        NeoForge.EVENT_BUS.addListener(RecipesReceivedEvent.class, event -> RecipeLookup.setClientRecipes(event.getRecipeMap()));
        ScreenSetup.register(modBus);
        RenderSetup.register(modBus);
    }

    private static void onRegisterPayloadHandlers(RegisterClientPayloadHandlersEvent event)
    {
        event.register(S2CMessageFlipGrill.TYPE, (message, context) -> {
            Level level = Minecraft.getInstance().level;
            if(level != null && level.getBlockEntity(message.getPos()) instanceof GrillBlockEntity grill)
            {
                grill.setFlipping(message.getPosition());
            }
        });
        event.register(S2CMessageOpenScreenSettings.TYPE, (message, context) -> {
            Level level = Minecraft.getInstance().level;
            if(level != null && level.getBlockEntity(message.pos()) instanceof ScreenBlockEntity screen)
            {
                Minecraft.getInstance().gui.setScreen(new ScreenSettingsScreen(screen));
            }
        });
        event.register(S2CMessageOpenDoorMat.TYPE, (message, context) -> {
            Level level = Minecraft.getInstance().level;
            if(level != null && level.getBlockEntity(message.pos()) instanceof DoorMatBlockEntity doorMat && !doorMat.hasMessage())
            {
                Minecraft.getInstance().gui.setScreen(new DoorMatScreen(doorMat));
            }
        });
    }

    private static void onRegisterItemTints(RegisterColorHandlersEvent.ItemTintSources event)
    {
        event.register(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "trampoline_color"), TrampolineColorSource.MAP_CODEC);
    }

    /**
     * The tint of each layer (tint index) of the furniture models. Item models carry their own tints (assets/cfm/items).
     */
    private static void onRegisterBlockTints(RegisterColorHandlersEvent.BlockTintSources event)
    {
        // Layer 1 is the painted wood of the picket fences, gates and post box
        register(event, secondLayer(0xCCCCCC),
                ModBlocks.PICKET_FENCE_WHITE, ModBlocks.PICKET_FENCE_ORANGE, ModBlocks.PICKET_FENCE_MAGENTA, ModBlocks.PICKET_FENCE_LIGHT_BLUE,
                ModBlocks.PICKET_FENCE_YELLOW, ModBlocks.PICKET_FENCE_LIME, ModBlocks.PICKET_FENCE_PINK, ModBlocks.PICKET_FENCE_GRAY,
                ModBlocks.PICKET_FENCE_LIGHT_GRAY, ModBlocks.PICKET_FENCE_CYAN, ModBlocks.PICKET_FENCE_PURPLE, ModBlocks.PICKET_FENCE_BLUE,
                ModBlocks.PICKET_FENCE_BROWN, ModBlocks.PICKET_FENCE_GREEN, ModBlocks.PICKET_FENCE_RED, ModBlocks.PICKET_FENCE_BLACK,
                ModBlocks.PICKET_GATE_WHITE, ModBlocks.PICKET_GATE_ORANGE, ModBlocks.PICKET_GATE_MAGENTA, ModBlocks.PICKET_GATE_LIGHT_BLUE,
                ModBlocks.PICKET_GATE_YELLOW, ModBlocks.PICKET_GATE_LIME, ModBlocks.PICKET_GATE_PINK, ModBlocks.PICKET_GATE_GRAY,
                ModBlocks.PICKET_GATE_LIGHT_GRAY, ModBlocks.PICKET_GATE_CYAN, ModBlocks.PICKET_GATE_PURPLE, ModBlocks.PICKET_GATE_BLUE,
                ModBlocks.PICKET_GATE_BROWN, ModBlocks.PICKET_GATE_GREEN, ModBlocks.PICKET_GATE_RED, ModBlocks.PICKET_GATE_BLACK,
                ModBlocks.POST_BOX);

        // Layer 1 darkens the stripped log faces
        register(event, secondLayer(0xBBBBBB),
                ModBlocks.CRATE_STRIPPED_OAK, ModBlocks.CRATE_STRIPPED_SPRUCE, ModBlocks.CRATE_STRIPPED_BIRCH, ModBlocks.CRATE_STRIPPED_JUNGLE,
                ModBlocks.CRATE_STRIPPED_ACACIA, ModBlocks.CRATE_STRIPPED_DARK_OAK, ModBlocks.CRATE_STRIPPED_CRIMSON, ModBlocks.CRATE_STRIPPED_WARPED,
                ModBlocks.CRATE_STRIPPED_MANGROVE,
                ModBlocks.KITCHEN_COUNTER_STRIPPED_OAK, ModBlocks.KITCHEN_COUNTER_STRIPPED_SPRUCE, ModBlocks.KITCHEN_COUNTER_STRIPPED_BIRCH,
                ModBlocks.KITCHEN_COUNTER_STRIPPED_JUNGLE, ModBlocks.KITCHEN_COUNTER_STRIPPED_ACACIA, ModBlocks.KITCHEN_COUNTER_STRIPPED_DARK_OAK,
                ModBlocks.KITCHEN_COUNTER_STRIPPED_CRIMSON, ModBlocks.KITCHEN_COUNTER_STRIPPED_WARPED, ModBlocks.KITCHEN_COUNTER_STRIPPED_MANGROVE,
                ModBlocks.KITCHEN_DRAWER_STRIPPED_OAK, ModBlocks.KITCHEN_DRAWER_STRIPPED_SPRUCE, ModBlocks.KITCHEN_DRAWER_STRIPPED_BIRCH,
                ModBlocks.KITCHEN_DRAWER_STRIPPED_JUNGLE, ModBlocks.KITCHEN_DRAWER_STRIPPED_ACACIA, ModBlocks.KITCHEN_DRAWER_STRIPPED_DARK_OAK,
                ModBlocks.KITCHEN_DRAWER_STRIPPED_CRIMSON, ModBlocks.KITCHEN_DRAWER_STRIPPED_WARPED, ModBlocks.KITCHEN_DRAWER_STRIPPED_MANGROVE,
                ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_OAK, ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_SPRUCE, ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_BIRCH,
                ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_JUNGLE, ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_ACACIA, ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_DARK_OAK,
                ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_CRIMSON, ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_WARPED, ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_MANGROVE,
                ModBlocks.KITCHEN_SINK_DARK_STRIPPED_OAK, ModBlocks.KITCHEN_SINK_DARK_STRIPPED_SPRUCE, ModBlocks.KITCHEN_SINK_DARK_STRIPPED_BIRCH,
                ModBlocks.KITCHEN_SINK_DARK_STRIPPED_JUNGLE, ModBlocks.KITCHEN_SINK_DARK_STRIPPED_ACACIA, ModBlocks.KITCHEN_SINK_DARK_STRIPPED_DARK_OAK,
                ModBlocks.KITCHEN_SINK_DARK_STRIPPED_CRIMSON, ModBlocks.KITCHEN_SINK_DARK_STRIPPED_WARPED, ModBlocks.KITCHEN_SINK_DARK_STRIPPED_MANGROVE);

        register(event, secondLayer(0x999999),
                ModBlocks.PARK_BENCH_STRIPPED_OAK, ModBlocks.PARK_BENCH_STRIPPED_SPRUCE, ModBlocks.PARK_BENCH_STRIPPED_BIRCH,
                ModBlocks.PARK_BENCH_STRIPPED_JUNGLE, ModBlocks.PARK_BENCH_STRIPPED_ACACIA, ModBlocks.PARK_BENCH_STRIPPED_DARK_OAK,
                ModBlocks.PARK_BENCH_STRIPPED_CRIMSON, ModBlocks.PARK_BENCH_STRIPPED_WARPED, ModBlocks.PARK_BENCH_STRIPPED_MANGROVE);

        register(event, secondLayer(0xCCCCCC), ModBlocks.FRIDGE_LIGHT, ModBlocks.FREEZER_LIGHT, ModBlocks.FRIDGE_DARK, ModBlocks.FREEZER_DARK);

        // Hedges are tinted like their leaves
        register(event, List.of(BlockTintSources.constant(FoliageColor.FOLIAGE_EVERGREEN)), ModBlocks.HEDGE_SPRUCE);
        register(event, List.of(BlockTintSources.constant(FoliageColor.FOLIAGE_BIRCH)), ModBlocks.HEDGE_BIRCH);
        register(event, List.of(BlockTintSources.constant(FoliageColor.FOLIAGE_MANGROVE)), ModBlocks.HEDGE_MANGROVE);
        register(event, List.of(BlockTintSources.foliage()), ModBlocks.HEDGE_OAK, ModBlocks.HEDGE_JUNGLE, ModBlocks.HEDGE_ACACIA, ModBlocks.HEDGE_DARK_OAK);

        // The wreath's leaves are pine green, as in 1.12.2
        register(event, List.of(BlockTintSources.constant(FoliageColor.FOLIAGE_EVERGREEN)), ModBlocks.WREATH);

        // The Christmas tree's leaves (tint layer 4) are the same green
        BlockTintSource white = BlockTintSources.constant(WHITE);
        register(event, List.of(white, white, white, white, BlockTintSources.constant(FoliageColor.FOLIAGE_EVERGREEN)), ModBlocks.CHRISTMAS_TREE);

        // The trampoline's colour is kept by its block entity; every layer takes it
        BlockTintSource trampoline = new BlockTintSource()
        {
            @Override
            public int color(BlockState state)
            {
                return WHITE;
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
            {
                if(level.getBlockEntity(pos) instanceof TrampolineBlockEntity trampoline)
                {
                    return ARGB.opaque(trampoline.getColour().getMapColor().col);
                }
                return WHITE;
            }
        };
        register(event, List.of(trampoline, trampoline), ModBlocks.TRAMPOLINE);
    }

    private static List<BlockTintSource> secondLayer(int rgb)
    {
        return List.of(BlockTintSources.constant(WHITE), BlockTintSources.constant(ARGB.opaque(rgb)));
    }

    @SafeVarargs
    private static void register(RegisterColorHandlersEvent.BlockTintSources event, List<BlockTintSource> tints, DeferredBlock<Block>... blocks)
    {
        event.register(tints, Arrays.stream(blocks).map(DeferredBlock::get).toArray(Block[]::new));
    }
}
