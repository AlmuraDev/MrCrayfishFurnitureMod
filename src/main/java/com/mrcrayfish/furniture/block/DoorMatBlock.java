package com.mrcrayfish.furniture.block;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ScheduledTickAccess;
import org.jspecify.annotations.Nullable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.furniture.network.message.S2CMessageOpenDoorMat;
import com.mrcrayfish.furniture.tileentity.DoorMatBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class DoorMatBlock extends FurnitureHorizontalBlock implements EntityBlock
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public DoorMatBlock(Properties properties)
    {
        super(properties);
        SHAPES = this.generateShapes(this.getStateDefinition().getPossibleStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            final VoxelShape[] BOXES = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0, 0, 2, 16, 1, 14), Direction.SOUTH));
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(BOXES[state.getValue(DIRECTION).get2DDataValue()]);
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state);
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state)
    {
        return SHAPES.get(state);
    }

    @Override
    protected BlockState updateShape(BlockState stateIn, LevelReader level, ScheduledTickAccess ticks, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random)
    {
        return !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, level, ticks, currentPos, facing, facingPos, facingState, random);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos)
    {
        return !reader.isEmptyBlock(pos.below());
    }

    // The player who placed the mat writes on it straight away
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, placer, stack);
        if(placer instanceof ServerPlayer player && level.getBlockEntity(pos) instanceof DoorMatBlockEntity doorMat && !doorMat.hasMessage())
        {
            PacketDistributor.sendToPlayer(player, new S2CMessageOpenDoorMat(pos));
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DoorMatBlockEntity(pos, state);
    }
}
