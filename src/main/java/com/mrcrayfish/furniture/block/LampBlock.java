package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A lamp in one of sixteen colours. Lamps stack into a tall floor lamp: the top one has the shade and the light, and using any lamp in the
 * stack turns it on or off. A dye changes its colour (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class LampBlock extends FurnitureBlock
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 16, 15);

    private final DyeColor color;

    public LampBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
        this.registerDefaultState(this.getStateDefinition().any().setValue(LIT, false).setValue(UP, false).setValue(DOWN, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Shapes.block();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.connect(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return direction.getAxis().isVertical() ? this.connect(state, level, pos) : state;
    }

    private BlockState connect(BlockState state, LevelReader level, BlockPos pos)
    {
        return state.setValue(UP, level.getBlockState(pos.above()).getBlock() instanceof LampBlock).setValue(DOWN, level.getBlockState(pos.below()).getBlock() instanceof LampBlock);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack heldItem, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        DyeColor dye = heldItem.get(DataComponents.DYE);
        if(dye != null && dye != this.color)
        {
            if(!level.isClientSide())
            {
                BlockState recoloured = byColor(dye).defaultBlockState().setValue(LIT, state.getValue(LIT)).setValue(UP, state.getValue(UP)).setValue(DOWN, state.getValue(DOWN));
                level.setBlockAndUpdate(pos, recoloured);
                heldItem.consume(1, player);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    // The top lamp of a stack is the one that lights up
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!level.isClientSide())
        {
            BlockPos top = pos;
            while(level.getBlockState(top.above()).getBlock() instanceof LampBlock)
            {
                top = top.above();
            }
            BlockState topState = level.getBlockState(top);
            boolean lit = !topState.getValue(LIT);
            level.setBlock(top, topState.setValue(LIT, lit), Block.UPDATE_ALL);
            level.playSound(null, top, SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.3F, lit ? 0.6F : 0.5F);
        }
        return InteractionResult.SUCCESS;
    }

    private static Block byColor(DyeColor color)
    {
        return BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath(Reference.MOD_ID, color.getName() + "_lamp"));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(LIT, UP, DOWN);
    }
}
