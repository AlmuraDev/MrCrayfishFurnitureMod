package com.mrcrayfish.furniture.datagen;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.common.ModTags;
import com.mrcrayfish.furniture.core.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

/**
 * @author Ocelot
 */
public class BlockTagGen extends BlockTagsProvider
{
    public BlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, lookupProvider, Reference.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        // Picket fences connect to other picket fences
        this.tag(ModTags.Blocks.PICKET_FENCES)
                .add(ModBlocks.PICKET_FENCE_WHITE.key())
                .add(ModBlocks.PICKET_FENCE_ORANGE.key())
                .add(ModBlocks.PICKET_FENCE_MAGENTA.key())
                .add(ModBlocks.PICKET_FENCE_LIGHT_BLUE.key())
                .add(ModBlocks.PICKET_FENCE_YELLOW.key())
                .add(ModBlocks.PICKET_FENCE_LIME.key())
                .add(ModBlocks.PICKET_FENCE_PINK.key())
                .add(ModBlocks.PICKET_FENCE_GRAY.key())
                .add(ModBlocks.PICKET_FENCE_LIGHT_GRAY.key())
                .add(ModBlocks.PICKET_FENCE_CYAN.key())
                .add(ModBlocks.PICKET_FENCE_PURPLE.key())
                .add(ModBlocks.PICKET_FENCE_BLUE.key())
                .add(ModBlocks.PICKET_FENCE_BROWN.key())
                .add(ModBlocks.PICKET_FENCE_GREEN.key())
                .add(ModBlocks.PICKET_FENCE_RED.key())
                .add(ModBlocks.PICKET_FENCE_BLACK.key());

        // Picket fence gates connect to other picket fence gates
        this.tag(ModTags.Blocks.PICKET_FENCE_GATES)
                .add(ModBlocks.PICKET_GATE_WHITE.key())
                .add(ModBlocks.PICKET_GATE_ORANGE.key())
                .add(ModBlocks.PICKET_GATE_MAGENTA.key())
                .add(ModBlocks.PICKET_GATE_LIGHT_BLUE.key())
                .add(ModBlocks.PICKET_GATE_YELLOW.key())
                .add(ModBlocks.PICKET_GATE_LIME.key())
                .add(ModBlocks.PICKET_GATE_PINK.key())
                .add(ModBlocks.PICKET_GATE_GRAY.key())
                .add(ModBlocks.PICKET_GATE_LIGHT_GRAY.key())
                .add(ModBlocks.PICKET_GATE_CYAN.key())
                .add(ModBlocks.PICKET_GATE_PURPLE.key())
                .add(ModBlocks.PICKET_GATE_BLUE.key())
                .add(ModBlocks.PICKET_GATE_BROWN.key())
                .add(ModBlocks.PICKET_GATE_GREEN.key())
                .add(ModBlocks.PICKET_GATE_RED.key())
                .add(ModBlocks.PICKET_GATE_BLACK.key());

        this.tag(ModTags.Blocks.HEDGES)
                .add(ModBlocks.HEDGE_OAK.key())
                .add(ModBlocks.HEDGE_SPRUCE.key())
                .add(ModBlocks.HEDGE_BIRCH.key())
                .add(ModBlocks.HEDGE_JUNGLE.key())
                .add(ModBlocks.HEDGE_ACACIA.key())
                .add(ModBlocks.HEDGE_DARK_OAK.key())
                .add(ModBlocks.HEDGE_MANGROVE.key())
                .add(ModBlocks.HEDGE_AZALEA.key())
                .add(ModBlocks.HEDGE_FLOWERING_AZALEA.key());

        // Dynamically registers upgraded fences to the upgraded fences tag
        TagBuilder upgradedFences = this.getOrCreateRawBuilder(ModTags.Blocks.UPGRADED_FENCES);
        for(GeneratorData.Variant variant : GeneratorData.ALL_VARIANTS)
        {
            upgradedFences.addElement(GeneratorData.getResultBlock(GeneratorData.UPGRADED_FENCE, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                upgradedFences.addElement(GeneratorData.getResultBlock(GeneratorData.UPGRADED_FENCE, variant, true).getId());
            }
        }

        // Dynamically registers upgraded gates to the upgraded gates tag
        TagBuilder upgradedGates = this.getOrCreateRawBuilder(ModTags.Blocks.UPGRADED_FENCE_GATES);
        for(GeneratorData.Variant variant : GeneratorData.ALL_VARIANTS)
        {
            upgradedGates.addElement(GeneratorData.getResultBlock(GeneratorData.UPGRADED_GATE, variant, false).getId());
            if(variant.strippedLog() != null)
            {
                upgradedGates.addElement(GeneratorData.getResultBlock(GeneratorData.UPGRADED_GATE, variant, true).getId());
            }
        }

        // Adds upgraded fences and gates to normal fence and gate tags
        this.tag(BlockTags.FENCES).addTag(ModTags.Blocks.PICKET_FENCES);
        this.tag(BlockTags.FENCES).addTag(ModTags.Blocks.UPGRADED_FENCES);
        // Not a wooden fence, so it joins up with picket and nether brick fences, as in 1.12.2
        this.tag(BlockTags.FENCES).add(ModBlocks.ELECTRIC_FENCE.key());
        this.tag(BlockTags.FENCE_GATES).addTag(ModTags.Blocks.PICKET_FENCE_GATES);
        this.tag(BlockTags.FENCE_GATES).addTag(ModTags.Blocks.UPGRADED_FENCE_GATES);

        // Dynamically gets all wooden blocks and marks them as mineable with an axe
        TagAppender<Block> mineableWithAxe = this.tag(BlockTags.MINEABLE_WITH_AXE);
        ModBlocks.REGISTER.getEntries().stream()
                .filter(s -> s.get().defaultBlockState().getSoundType() == SoundType.WOOD)
                .forEach(holder -> mineableWithAxe.add(holder.key()));

        // Dynamically gets all stone blocks and marks them as mineable with an pickaxe
        TagAppender<Block> mineableWithPickaxe = this.tag(BlockTags.MINEABLE_WITH_PICKAXE);
        ModBlocks.REGISTER.getEntries().stream()
                .filter(s -> s.get().defaultBlockState().getSoundType() == SoundType.STONE || s.get().defaultBlockState().getSoundType() == SoundType.METAL)
                .forEach(holder -> mineableWithPickaxe.add(holder.key()));
    }
}
