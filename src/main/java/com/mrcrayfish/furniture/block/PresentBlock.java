package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.tileentity.PresentBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

/**
 * A wrapped present (1.12.2, one block per wrapping colour). Breaking it opens it: what is inside spills out, with a cheer and a note
 * of who it's from. The wrapping itself is gone, so it drops nothing else.
 *
 * Author: MrCrayfish
 */
public class PresentBlock extends FurnitureBlock implements EntityBlock
{
    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 5.6, 12);

    public PresentBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        if(!level.isClientSide() && level.getBlockEntity(pos) instanceof PresentBlockEntity present)
        {
            level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.75F, 1.0F);
            String from = present.getFrom().isEmpty() ? "?" : present.getFrom();
            player.sendSystemMessage(Component.translatable("message.cfm.present_opened", Component.literal(from).withStyle(ChatFormatting.RED)));
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new PresentBlockEntity(pos, state);
    }
}
