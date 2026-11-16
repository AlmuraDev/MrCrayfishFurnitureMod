package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.entity.SeatEntity;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import com.mrcrayfish.furniture.util.WaterHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A two-block bath. Buckets and bottles add or take water a level at a time (up to 16); using the tap end (the head) with a water source
 * two blocks below fills it a level. Use it with an empty hand to sit in it. A comparator shows the water level (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class BathBlock extends FurnitureHorizontalBlock
{
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 16);
    public static final int MAX_LEVEL = 16;

    private static final VoxelShape OUTLINE = Block.box(0, 0, 0, 16, 15, 16);
    private static final Map<Direction, VoxelShape> FOOT_SHAPES = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> HEAD_SHAPES = new EnumMap<>(Direction.class);

    static
    {
        VoxelShape bottom = Block.box(0, 0, 0, 16, 2, 16);
        Map<Direction, VoxelShape> sides = new EnumMap<>(Direction.class);
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            sides.put(direction, VoxelShapeHelper.rotate(Shapes.box(0.0, 0.125, 0.0, 1.0, 0.875, 0.125), direction));
        }
        for(Direction facing : Direction.Plane.HORIZONTAL)
        {
            // The side walls of each end, leaving open the side that joins the other end (as 1.12.2 set them out)
            List<VoxelShape> foot = new ArrayList<>(List.of(bottom));
            if(facing != Direction.WEST) foot.add(sides.get(Direction.NORTH));
            if(facing != Direction.EAST) foot.add(sides.get(Direction.SOUTH));
            if(facing != Direction.NORTH) foot.add(sides.get(Direction.EAST));
            if(facing != Direction.SOUTH) foot.add(sides.get(Direction.WEST));
            FOOT_SHAPES.put(facing, VoxelShapeHelper.combineAll(foot));

            List<VoxelShape> head = new ArrayList<>(List.of(bottom));
            if(facing != Direction.EAST) head.add(sides.get(Direction.NORTH));
            if(facing != Direction.WEST) head.add(sides.get(Direction.SOUTH));
            if(facing != Direction.SOUTH) head.add(sides.get(Direction.EAST));
            if(facing != Direction.NORTH) head.add(sides.get(Direction.WEST));
            HEAD_SHAPES.put(facing, VoxelShapeHelper.combineAll(head));
        }
    }

    public BathBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(PART, BedPart.FOOT).setValue(LEVEL, 0));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return OUTLINE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return (state.getValue(PART) == BedPart.FOOT ? FOOT_SHAPES : HEAD_SHAPES).get(state.getValue(DIRECTION));
    }

    // The head goes in front of the foot, in the direction the player faces
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Direction direction = context.getHorizontalDirection();
        BlockPos headPos = context.getClickedPos().relative(direction);
        Level level = context.getLevel();
        if(!level.getBlockState(headPos).canBeReplaced(context) || !level.getWorldBorder().isWithinBounds(headPos))
            return null;
        return this.defaultBlockState().setValue(DIRECTION, direction);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, placer, stack);
        if(!level.isClientSide())
        {
            level.setBlock(pos.relative(state.getValue(DIRECTION)), state.setValue(PART, BedPart.HEAD), Block.UPDATE_ALL);
        }
    }

    private static Direction towardsOtherPart(BlockState state)
    {
        return state.getValue(PART) == BedPart.FOOT ? state.getValue(DIRECTION) : state.getValue(DIRECTION).getOpposite();
    }

    // Each end needs the other
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        if(direction == towardsOtherPart(state))
        {
            boolean partner = neighbourState.is(this) && neighbourState.getValue(PART) != state.getValue(PART);
            return partner ? state : Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    // Like a bed: only the head drops the bath (the foot goes with it); in creative the head goes without dropping anything
    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        if(!level.isClientSide() && player.preventsBlockDrops() && state.getValue(PART) == BedPart.FOOT)
        {
            BlockPos headPos = pos.relative(towardsOtherPart(state));
            BlockState head = level.getBlockState(headPos);
            if(head.is(this) && head.getValue(PART) == BedPart.HEAD)
            {
                level.setBlock(headPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                level.levelEvent(player, 2001, headPos, Block.getId(head));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack heldItem, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        WaterHelper.Use use = WaterHelper.useOf(heldItem);
        int water = state.getValue(LEVEL);
        if(use == WaterHelper.Use.TAKE && water > 0 || use == WaterHelper.Use.ADD && water < MAX_LEVEL)
        {
            if(!level.isClientSide())
            {
                WaterHelper.exchange(level, pos, player, hand, heldItem, use);
                int newLevel = water + (use == WaterHelper.Use.ADD ? 1 : -1);
                this.setLevel(level, pos, state, newLevel);
                if(use == WaterHelper.Use.ADD)
                {
                    WaterHelper.splash(level, pos, 0.75 + newLevel * 0.0265);
                }
            }
            return InteractionResult.SUCCESS;
        }
        if(use != WaterHelper.Use.NONE)
        {
            return InteractionResult.CONSUME;
        }
        if(!heldItem.isEmpty())
        {
            return this.fillFromSource(state, level, pos, player);
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(player.isShiftKeyDown())
        {
            return this.fillFromSource(state, level, pos, player);
        }
        return SeatEntity.create(level, pos, 0, player, state.getValue(DIRECTION));
    }

    // The tap end draws a level of water from a source two blocks below it
    private InteractionResult fillFromSource(BlockState state, Level level, BlockPos pos, Player player)
    {
        if(!level.isClientSide() && state.getValue(LEVEL) < MAX_LEVEL)
        {
            if(state.getValue(PART) == BedPart.HEAD && WaterHelper.hasWaterSource(level, pos))
            {
                WaterHelper.takeWaterSource(level, pos);
                this.setLevel(level, pos, state, state.getValue(LEVEL) + 1);
                level.playSound(null, pos, ModSounds.BLOCK_TAP.get(), SoundSource.BLOCKS, 0.75F, 0.8F);
            }
            else
            {
                player.sendSystemMessage(Component.translatable("message.cfm.bath"));
            }
        }
        return InteractionResult.SUCCESS;
    }

    // Both ends always hold the same water
    private void setLevel(Level level, BlockPos pos, BlockState state, int water)
    {
        level.setBlock(pos, state.setValue(LEVEL, water), Block.UPDATE_ALL);
        BlockPos otherPos = pos.relative(towardsOtherPart(state));
        BlockState other = level.getBlockState(otherPos);
        if(other.is(this))
        {
            level.setBlock(otherPos, other.setValue(LEVEL, water), Block.UPDATE_ALL);
        }
        level.updateNeighbourForOutputSignal(pos, this);
        level.updateNeighbourForOutputSignal(otherPos, this);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction)
    {
        return Math.min(state.getValue(LEVEL), 15);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(PART, LEVEL);
    }
}
