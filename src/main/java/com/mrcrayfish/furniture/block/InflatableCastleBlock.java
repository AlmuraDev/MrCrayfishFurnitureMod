package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.util.BounceHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * An inflatable castle (1.12.2, one block per colour): a bouncy block. Fall on it and you bounce back up (up to about four blocks);
 * sneak to land without bouncing. Castles next to each other join up, whatever their colour.
 *
 * Author: MrCrayfish
 */
public class InflatableCastleBlock extends Block
{
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    public InflatableCastleBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(TYPE, Type.NONE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(TYPE, getType(context.getLevel(), context.getClickedPos()));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return direction.getAxis().isHorizontal() ? state.setValue(TYPE, getType(level, pos)) : state;
    }

    private static Type getType(LevelReader level, BlockPos pos)
    {
        boolean north = level.getBlockState(pos.north()).getBlock() instanceof InflatableCastleBlock;
        boolean east = level.getBlockState(pos.east()).getBlock() instanceof InflatableCastleBlock;
        boolean south = level.getBlockState(pos.south()).getBlock() instanceof InflatableCastleBlock;
        boolean west = level.getBlockState(pos.west()).getBlock() instanceof InflatableCastleBlock;
        int count = (north ? 1 : 0) + (east ? 1 : 0) + (south ? 1 : 0) + (west ? 1 : 0);
        return switch(count)
        {
            case 0 -> Type.NONE;
            case 1 -> north ? Type.SOUTH : east ? Type.WEST : south ? Type.NORTH : Type.EAST;
            case 2 -> north && south ? Type.HORIZONTAL_NORTH_SOUTH : east && west ? Type.HORIZONTAL_EAST_WEST
                    : east && south ? Type.CORNER_EAST_SOUTH : south && west ? Type.CORNER_WEST_SOUTH
                    : north && east ? Type.CORNER_NORTH_EAST : Type.CORNER_NORTH_WEST;
            case 3 -> !north ? Type.THREE_NORTH : !east ? Type.THREE_EAST : !south ? Type.THREE_SOUTH : Type.THREE_WEST;
            default -> Type.CENTER;
        };
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance)
    {
        if(entity instanceof LivingEntity)
        {
            float height = (float) Math.min(entity.fallDistance, 4.0);
            if(height > 0 && !entity.isShiftKeyDown())
            {
                BounceHelper.prepareBounce(entity, Math.sqrt(0.22 * (height + 1)));
                if(level.isClientSide())
                {
                    level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1.0F, 1.0F, 1.0F), entity.getX(), entity.getY(), entity.getZ(), 1.0, 1.0, 1.0);
                }
                else
                {
                    level.playSound(null, pos, ModSounds.BLOCK_TRAMPOLINE_BOUNCE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
            entity.fallDistance = 0;
        }
    }

    // Throws the entity up at the speed fallOn worked out (see BounceHelper)
    @Override
    public float getBounceRestitution(Level level, BlockPos pos, BlockState state, Entity entity)
    {
        return BounceHelper.takeRestitution(entity);
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles)
    {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(TYPE);
    }

    // Named for the side the castle's edge is on, as 1.12.2 named them
    public enum Type implements StringRepresentable
    {
        NONE, NORTH, EAST, SOUTH, WEST, HORIZONTAL_EAST_WEST, HORIZONTAL_NORTH_SOUTH, CORNER_EAST_SOUTH, CORNER_WEST_SOUTH, CORNER_NORTH_EAST,
        CORNER_NORTH_WEST, THREE_NORTH, THREE_EAST, THREE_SOUTH, THREE_WEST, CENTER;

        @Override
        public String getSerializedName()
        {
            return this.name().toLowerCase(java.util.Locale.ROOT);
        }
    }
}
