package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * A five-candle candelabra that gives off light (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class CandleBlock extends FurnitureBlock
{
    private static final VoxelShape OUTLINE = Shapes.box(0.0625, 0.0, 0.0625, 0.9375, 1.2625, 0.9375);
    private static final VoxelShape COLLISION = VoxelShapeHelper.combineAll(List.of(
            Block.box(4, 0, 4, 12, 1, 12),
            Block.box(5, 1, 5, 11, 2, 11),
            Block.box(7, 2, 7, 9, 7, 9),
            Block.box(2, 7, 7, 14, 9, 9),
            Block.box(7, 7, 2, 9, 9, 14),
            Block.box(1, 9, 6, 5, 17, 10),
            Block.box(6, 9, 1, 10, 17, 5),
            Block.box(11, 9, 6, 15, 17, 10),
            Block.box(6, 9, 11, 10, 17, 15),
            Block.box(6, 9, 6, 10, 20, 10)));

    // Where each flame sits: the middle candle is taller than the four around it
    private static final double[][] FLAMES = {{7.95, 1.45, 7.95}, {7.95, 1.2, 12.95}, {7.95, 1.2, 2.95}, {12.95, 1.2, 7.95}, {2.95, 1.2, 7.95}};

    public CandleBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return OUTLINE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return COLLISION;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        boolean smoke = random.nextBoolean();
        double xRand = 0.001D * (random.nextInt(4) - 2);
        double zRand = 0.001D * (random.nextInt(4) - 2);
        for(double[] flame : FLAMES)
        {
            double x = pos.getX() + flame[0] * 0.0625;
            double y = pos.getY() + flame[1];
            double z = pos.getZ() + flame[2] * 0.0625;
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
            if(smoke)
            {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, xRand + (random.nextInt(3) - 1) * 0.01, 0.01D, zRand + (random.nextInt(3) - 1) * 0.01);
            }
        }
    }
}
