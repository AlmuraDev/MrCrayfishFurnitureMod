package com.mrcrayfish.furniture.block;

import org.jspecify.annotations.Nullable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.furniture.tileentity.KitchenSinkBlockEntity;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class KitchenSinkBlock extends FurnitureHorizontalBlock implements EntityBlock
{
    private final boolean bigSink;

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public KitchenSinkBlock(Properties properties, boolean bigSink)
    {
        super(properties);
        this.bigSink = bigSink;
        SHAPES = this.generateShapes(this.getStateDefinition().getPossibleStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            List<VoxelShape> shapes = new ArrayList<>();
            Direction direction = state.getValue(DIRECTION);
            if(this.bigSink)
            {
                shapes.add(VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 15.0), Direction.SOUTH))[direction.get2DDataValue()]);
                shapes.add(VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0.0, 9.0, 0.0, 16.0, 16.0, 16.0), Direction.SOUTH))[direction.get2DDataValue()]);
            }
            else
            {
                shapes.add(VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 15.0), Direction.SOUTH))[direction.get2DDataValue()]);
                shapes.add(VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0), Direction.SOUTH))[direction.get2DDataValue()]);
            }
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
    protected InteractionResult useItemOn(ItemStack heldItem, BlockState state, Level level, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult result)
    {
        if(!(level instanceof ServerLevel serverLevel) || !(level.getBlockEntity(pos) instanceof KitchenSinkBlockEntity sink))
        {
            return InteractionResult.SUCCESS;
        }

        // A glass bottle takes a bucket's worth of water out as a water bottle
        if(heldItem.is(Items.GLASS_BOTTLE))
        {
            if(sink.getFluidAmount() > 0)
            {
                ItemStack waterPotion = PotionContents.createItemStack(Items.POTION, Potions.WATER);
                playerEntity.setItemInHand(hand, ItemUtils.createFilledResult(heldItem, playerEntity, waterPotion));
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                sink.drain(FluidType.BUCKET_VOLUME);
            }
            return InteractionResult.SUCCESS;
        }

        // Buckets and other fluid containers fill from or empty into the sink
        if(!heldItem.isEmpty() && FluidUtil.interactWithFluidHandler(playerEntity, hand, pos, sink.getFluidHandler(), null))
        {
            return InteractionResult.SUCCESS;
        }

        // With water two blocks below, the tap fills the sink a bucket at a time
        BlockPos waterPos = pos.below().below();
        if(this.isWaterSource(level, waterPos) && sink.getFluidAmount() < sink.getCapacity())
        {
            sink.fill(Fluids.WATER, FluidType.BUCKET_VOLUME);
            level.playSound(null, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

            Direction direction = state.getValue(DIRECTION);
            double posX = pos.getX() + 0.5 + direction.getStepX() * 0.1;
            double posY = pos.getY() + 1.15;
            double posZ = pos.getZ() + 0.5 + direction.getStepZ() * 0.1;
            serverLevel.sendParticles(ParticleTypes.FALLING_WATER, posX, posY, posZ, 10, 0.01, 0.01, 0.01, 0);

            int adjacentSources = 0;
            adjacentSources += this.isWaterSource(level, waterPos.north()) ? 1 : 0;
            adjacentSources += this.isWaterSource(level, waterPos.east()) ? 1 : 0;
            adjacentSources += this.isWaterSource(level, waterPos.south()) ? 1 : 0;
            adjacentSources += this.isWaterSource(level, waterPos.west()) ? 1 : 0;
            if(adjacentSources < 2) //If it has less then two adjacent water sources, it is not infinite and thus it should be consumed
            {
                level.setBlockAndUpdate(waterPos, Blocks.AIR.defaultBlockState());
            }
        }
        return InteractionResult.SUCCESS;
    }

    private boolean isWaterSource(Level level, BlockPos pos)
    {
        return level.getFluidState(pos).getType() == Fluids.WATER;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new KitchenSinkBlockEntity(pos, state);
    }
}

