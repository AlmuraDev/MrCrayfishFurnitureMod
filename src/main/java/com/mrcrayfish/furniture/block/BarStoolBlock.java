package com.mrcrayfish.furniture.block;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A bar stool in one of sixteen colours. Use a dye on it to change its colour, or use it to sit (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class BarStoolBlock extends FurnitureBlock
{
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 13, 14);

    private final DyeColor color;

    public BarStoolBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    public DyeColor getColor()
    {
        return this.color;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack heldItem, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        DyeColor dye = heldItem.get(DataComponents.DYE);
        if(dye != null && dye != this.color)
        {
            if(!level.isClientSide())
            {
                level.setBlockAndUpdate(pos, byColor(dye).defaultBlockState());
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
        return SeatEntity.create(level, pos, 9 * 0.0625, player, player.getDirection().getOpposite());
    }

    private static Block byColor(DyeColor color)
    {
        return BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath(Reference.MOD_ID, color.getName() + "_bar_stool"));
    }
}
