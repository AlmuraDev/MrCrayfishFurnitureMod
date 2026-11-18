package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.tileentity.CookieJarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

/**
 * A cookie jar (1.12.2) holding up to six cookies: use it with a cookie to put one in, or with an empty hand to take one out. Breaking
 * it spills them. Comparators read how many are in it.
 *
 * Author: MrCrayfish
 */
public class CookieJarBlock extends FurnitureBlock implements EntityBlock
{
    public static final int MAX_COOKIES = 6;
    public static final IntegerProperty COOKIES = IntegerProperty.create("cookies", 0, MAX_COOKIES);
    private static final VoxelShape SHAPE = Block.box(4.5, 0, 4.5, 11.5, 10, 11.5);

    public CookieJarBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(COOKIES, 0));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        int cookies = state.getValue(COOKIES);
        if(stack.is(Items.COOKIE) && cookies < MAX_COOKIES)
        {
            if(!level.isClientSide())
            {
                level.setBlock(pos, state.setValue(COOKIES, cookies + 1), Block.UPDATE_ALL);
                stack.consume(1, player);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        int cookies = state.getValue(COOKIES);
        if(cookies <= 0)
            return InteractionResult.PASS;

        if(!level.isClientSide())
        {
            level.setBlock(pos, state.setValue(COOKIES, cookies - 1), Block.UPDATE_ALL);
            Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, new ItemStack(Items.COOKIE));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction)
    {
        return state.getValue(COOKIES);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new CookieJarBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(COOKIES);
    }
}
