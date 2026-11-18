package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.network.message.S2CMessageOpenScreenSettings;
import com.mrcrayfish.furniture.tileentity.TVBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;

/**
 * A television (1.12.2). Using it opens its settings (up to three channels, each a GIF from the web); a TV remote turns it on and off
 * and changes channel. Its screen's size and place, in pixels, are kept here for the renderer.
 *
 * Author: MrCrayfish
 */
public class TVBlock extends ApplianceBlock
{
    private final int screenWidth;
    private final int screenHeight;
    private final double screenY;
    private final double screenZ;

    /**
     * @param screenY how high the bottom of the screen is
     * @param screenZ how far the screen is from the middle of the block, towards the front when negative
     */
    public TVBlock(Properties properties, VoxelShape shape, int screenWidth, int screenHeight, double screenY, double screenZ)
    {
        super(properties, shape);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenY = screenY;
        this.screenZ = screenZ;
    }

    public int getScreenWidth()
    {
        return this.screenWidth;
    }

    public int getScreenHeight()
    {
        return this.screenHeight;
    }

    public double getScreenY(BlockState state)
    {
        return this.screenY;
    }

    public double getScreenZ(BlockState state)
    {
        return this.screenZ;
    }

    @Override
    protected boolean opensMenu()
    {
        return false;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof TVBlockEntity)
        {
            PacketDistributor.sendToPlayer(serverPlayer, new S2CMessageOpenScreenSettings(pos));
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new TVBlockEntity(pos, state);
    }
}
