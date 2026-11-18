package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.tileentity.BasicLootBlockEntity;
import com.mrcrayfish.furniture.tileentity.TVStandBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/**
 * A TV stand (1.12.2): a cabinet with a row of nine slots. Stands side by side facing the same way join up into one long unit.
 *
 * Author: MrCrayfish
 */
public class TVStandBlock extends FurnitureHorizontalBlock implements EntityBlock
{
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    public TVStandBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(TYPE, Type.NONE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = super.getStateForPlacement(context);
        return state.setValue(TYPE, this.getType(context.getLevel(), context.getClickedPos(), state.getValue(DIRECTION)));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return state.setValue(TYPE, this.getType(level, pos, state.getValue(DIRECTION)));
    }

    private Type getType(LevelReader level, BlockPos pos, Direction facing)
    {
        boolean left = this.isStand(level, pos.relative(facing.getCounterClockWise()), facing);
        boolean right = this.isStand(level, pos.relative(facing.getClockWise()), facing);
        if(left && right)
            return Type.BOTH;
        if(left)
            return Type.RIGHT;
        if(right)
            return Type.LEFT;
        return Type.NONE;
    }

    private boolean isStand(LevelReader level, BlockPos pos, Direction facing)
    {
        BlockState state = level.getBlockState(pos);
        return state.is(this) && state.getValue(DIRECTION) == facing;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
    {
        if(!level.isClientSide() && level.getBlockEntity(pos) instanceof TVStandBlockEntity stand)
        {
            player.openMenu(stand);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if(level.getBlockEntity(pos) instanceof BasicLootBlockEntity blockEntity)
        {
            blockEntity.updateOpenerCount();
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new TVStandBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    public enum Type implements StringRepresentable
    {
        NONE("none"),
        LEFT("left"),
        RIGHT("right"),
        BOTH("both");

        private final String id;

        Type(String id)
        {
            this.id = id;
        }

        @Override
        public String getSerializedName()
        {
            return this.id;
        }
    }
}
