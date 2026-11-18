package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.tileentity.StereoBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/**
 * A stereo (1.12.2). Using it plays the next music disc's song, without needing the disc; sneaking and using it turns it off.
 * Comparators read which song it plays.
 *
 * Author: MrCrayfish
 */
public class StereoBlock extends ApplianceBlock
{
    public StereoBlock(Properties properties)
    {
        super(properties, Block.box(5.1, 0, 0.5, 10.5, 13, 15.5));
    }

    @Override
    protected boolean opensMenu()
    {
        return false;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!level.isClientSide() && level.getBlockEntity(pos) instanceof StereoBlockEntity stereo)
        {
            if(player.isSecondaryUseActive())
            {
                player.sendSystemMessage(Component.translatable(stereo.stop() ? "message.cfm.stereo_off" : "message.cfm.stereo_already_off"));
            }
            else
            {
                stereo.playNext();
            }
            level.updateNeighbourForOutputSignal(pos, this);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction)
    {
        return level.getBlockEntity(pos) instanceof StereoBlockEntity stereo ? stereo.getSignal() : 0;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new StereoBlockEntity(pos, state);
    }
}
