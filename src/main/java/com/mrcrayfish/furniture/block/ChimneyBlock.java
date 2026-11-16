package com.mrcrayfish.furniture.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Locale;

/**
 * A brick chimney. Stacked chimneys join up; the top one smokes (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class ChimneyBlock extends FurnitureBlock
{
    public static final EnumProperty<ChimneyType> TYPE = EnumProperty.create("type", ChimneyType.class);

    private static final VoxelShape SHAPE = Shapes.box(0.125, 0.0, 0.125, 0.875, 1.0, 0.875);

    public ChimneyBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(TYPE, ChimneyType.TOP));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.getType(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return direction == Direction.UP ? this.getType(state, level, pos) : state;
    }

    // A chimney with another on top of it is a bottom section
    private BlockState getType(BlockState state, LevelReader level, BlockPos pos)
    {
        return state.setValue(TYPE, level.getBlockState(pos.above()).is(this) ? ChimneyType.BOTTOM : ChimneyType.TOP);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if(state.getValue(TYPE) != ChimneyType.TOP)
            return;

        for(int i = 0; i < 2; i++)
        {
            double posX = pos.getX() + 0.25 + 0.5 * random.nextDouble();
            double posZ = pos.getZ() + 0.25 + 0.5 * random.nextDouble();
            level.addParticle(ParticleTypes.SMOKE, posX, pos.getY() + 0.9, posZ, 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.LARGE_SMOKE, posX, pos.getY() + 0.9, posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    public enum ChimneyType implements StringRepresentable
    {
        TOP,
        BOTTOM;

        @Override
        public String getSerializedName()
        {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
