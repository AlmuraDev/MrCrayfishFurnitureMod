package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.entity.SeatEntity;
import net.minecraft.world.item.Items;
import com.mrcrayfish.furniture.core.ModBlocks;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The modern couch in one of sixteen colours (from 1.12.2). Couches side by side facing the same way join, losing the armrests between
 * them. Use it to sit; use a dye on it to change its colour.
 *
 * Author: MrCrayfish
 */
public class ModernCouchBlock extends FurnitureHorizontalBlock
{
    // Which armrests the couch has: BOTH on a couch on its own, NONE in the middle of a row
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    private static final Map<String, VoxelShape> SHAPES = new HashMap<>();

    static
    {
        for(Direction direction : Direction.Plane.HORIZONTAL)
        {
            VoxelShape base = VoxelShapeHelper.rotate(Block.box(1, 3, 0, 15, 9, 16), direction);
            VoxelShape backrest = VoxelShapeHelper.rotate(Block.box(11, 9, 0, 15, 19, 16), direction);
            VoxelShape left = VoxelShapeHelper.rotate(Block.box(1, 0, 0, 15, 13, 2), direction);
            VoxelShape right = VoxelShapeHelper.rotate(Block.box(1, 0, 14, 15, 13, 16), direction);
            for(Type type : Type.values())
            {
                List<VoxelShape> shapes = new ArrayList<>(List.of(base, backrest));
                if(type == Type.LEFT || type == Type.BOTH) shapes.add(left);
                if(type == Type.RIGHT || type == Type.BOTH) shapes.add(right);
                SHAPES.put(direction + "/" + type, VoxelShapeHelper.combineAll(shapes));
            }
        }
    }

    private final DyeColor color;

    public ModernCouchBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(TYPE, Type.BOTH));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state.getValue(DIRECTION) + "/" + state.getValue(TYPE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = super.getStateForPlacement(context);
        return state.setValue(TYPE, typeAt(context.getLevel(), context.getClickedPos(), state.getValue(DIRECTION)));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return direction.getAxis().isHorizontal() ? state.setValue(TYPE, typeAt(level, pos, state.getValue(DIRECTION))) : state;
    }

    // A neighbouring couch to the side, facing the same way, takes the armrest on that side
    private static Type typeAt(LevelReader level, BlockPos pos, Direction facing)
    {
        boolean left = joins(level, pos.relative(facing.getClockWise()), facing);
        boolean right = joins(level, pos.relative(facing.getCounterClockWise()), facing);
        if(left && right)
            return Type.NONE;
        if(left)
            return Type.LEFT;
        if(right)
            return Type.RIGHT;
        return Type.BOTH;
    }

    private static boolean joins(LevelReader level, BlockPos pos, Direction facing)
    {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof ModernCouchBlock && state.getValue(DIRECTION) == facing;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack heldItem, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if(heldItem.is(Items.NAME_TAG) && "jeb_".equals(heldItem.getHoverName().getString()) && !state.is(ModBlocks.MODERN_COUCH_JEB.get()))
        {
            if(!level.isClientSide())
            {
                level.setBlockAndUpdate(pos, ModBlocks.MODERN_COUCH_JEB.get().defaultBlockState().setValue(DIRECTION, state.getValue(DIRECTION)).setValue(TYPE, state.getValue(TYPE)));
                heldItem.consume(1, player);
            }
            return InteractionResult.SUCCESS;
        }

        DyeColor dye = heldItem.get(DataComponents.DYE);
        if(dye != null && (dye != this.color || state.is(ModBlocks.MODERN_COUCH_JEB.get())))
        {
            if(!level.isClientSide())
            {
                Block recoloured = BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath(Reference.MOD_ID, dye.getName() + "_modern_couch"));
                level.setBlockAndUpdate(pos, recoloured.defaultBlockState().setValue(DIRECTION, state.getValue(DIRECTION)).setValue(TYPE, state.getValue(TYPE)));
                heldItem.consume(1, player);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(player.isShiftKeyDown())
            return InteractionResult.PASS;
        return SeatEntity.create(level, pos, 5 * 0.0625, player, state.getValue(DIRECTION));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    public enum Type implements StringRepresentable
    {
        NONE,
        LEFT,
        RIGHT,
        BOTH;

        @Override
        public String getSerializedName()
        {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
