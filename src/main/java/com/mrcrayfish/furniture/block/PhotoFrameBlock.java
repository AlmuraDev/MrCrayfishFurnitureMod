package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.network.message.S2CMessageOpenScreenSettings;
import com.mrcrayfish.furniture.tileentity.PhotoFrameBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;

/**
 * A photo frame (1.12.2, one block per frame colour) that shows a picture from the web. Using it opens its settings.
 *
 * Author: MrCrayfish
 */
public class PhotoFrameBlock extends WallFurnitureBlock implements EntityBlock
{
    public PhotoFrameBlock(Properties properties)
    {
        super(properties, Block.box(15, 0, 0, 16, 16, 16));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof PhotoFrameBlockEntity)
        {
            PacketDistributor.sendToPlayer(serverPlayer, new S2CMessageOpenScreenSettings(pos));
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new PhotoFrameBlockEntity(pos, state);
    }
}
