package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.tileentity.ChristmasTreeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

/**
 * A Christmas tree two blocks tall (1.12.2). Using it with an item hangs that item on the side facing away from you (each half holds
 * four), taking back what hung there. Breaking the top with shears trims it to a small tree; otherwise the whole tree comes down.
 *
 * Author: MrCrayfish
 */
public class ChristmasTreeBlock extends FurnitureHorizontalBlock implements EntityBlock
{
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 16, 15);

    public ChristmasTreeBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockPos above = context.getClickedPos().above();
        Level level = context.getLevel();
        if(above.getY() >= level.getMaxY() || !level.getBlockState(above).canBeReplaced(context))
            return null;
        return super.getStateForPlacement(context).setValue(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, placer, stack);
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
    }

    // The top needs the bottom; the bottom stands on its own once the top is trimmed off
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        if(state.getValue(HALF) == DoubleBlockHalf.UPPER && direction == Direction.DOWN && !(neighbourState.is(this) && neighbourState.getValue(HALF) == DoubleBlockHalf.LOWER))
            return Blocks.AIR.defaultBlockState();
        return state;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        if(!level.isClientSide() && state.getValue(HALF) == DoubleBlockHalf.UPPER)
        {
            ItemStack tool = player.getMainHandItem();
            if(tool.is(Items.SHEARS))
            {
                tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
            else
            {
                BlockPos below = pos.below();
                BlockState lower = level.getBlockState(below);
                if(lower.is(this) && lower.getValue(HALF) == DoubleBlockHalf.LOWER)
                {
                    level.destroyBlock(below, !player.preventsBlockDrops(), player);
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if(!(level.getBlockEntity(pos) instanceof ChristmasTreeBlockEntity tree))
            return InteractionResult.TRY_WITH_EMPTY_HAND;

        if(!level.isClientSide())
        {
            tree.hangOrnament(player.getDirection(), stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1));
            stack.consume(1, player);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!level.isClientSide() && level.getBlockEntity(pos) instanceof ChristmasTreeBlockEntity tree)
        {
            tree.hangOrnament(player.getDirection(), ItemStack.EMPTY);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ChristmasTreeBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }
}
