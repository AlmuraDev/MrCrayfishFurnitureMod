package com.mrcrayfish.furniture.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.function.Consumer;

/**
 * Author: MrCrayfish
 */
public class BlockEntityUtil
{
    /**
     * Sends a block entity's update packet ({@link BlockEntity#getUpdatePacket()}) to the clients tracking it, and marks it changed so
     * the change is saved. Does nothing on the client.
     *
     * @param blockEntity the block entity to update
     */
    public static void sendUpdatePacket(BlockEntity blockEntity)
    {
        Level level = blockEntity.getLevel();
        if(level != null && !level.isClientSide())
        {
            blockEntity.setChanged();
            BlockState state = blockEntity.getBlockState();
            level.sendBlockUpdated(blockEntity.getBlockPos(), state, state, Block.UPDATE_CLIENTS);
        }
    }

    /**
     * Writes only what the client needs into an update tag, for {@link BlockEntity#getUpdateTag(HolderLookup.Provider)}.
     */
    public static CompoundTag createUpdateTag(HolderLookup.Provider registries, Consumer<ValueOutput> writer)
    {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        writer.accept(output);
        return output.buildResult();
    }
}
