package com.mrcrayfish.furniture.item;

import com.mrcrayfish.furniture.block.Switchable;
import com.mrcrayfish.furniture.core.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The light switch item. Use it on lights to link them, then place it within 16 blocks of them; sneak-use it in the air to forget the
 * linked lights (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class LightSwitchItem extends BlockItem
{
    public static final int MAX_DISTANCE = 16;

    public LightSwitchItem(Block block, Item.Properties properties)
    {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        if(player != null && !player.isShiftKeyDown() && context.getLevel().getBlockState(pos).getBlock() instanceof Switchable)
        {
            ItemStack stack = context.getItemInHand();
            List<BlockPos> lights = new ArrayList<>(stack.getOrDefault(ModDataComponents.LINKED_LIGHTS.get(), List.of()));
            if(!lights.contains(pos))
            {
                lights.add(pos.immutable());
                stack.set(ModDataComponents.LINKED_LIGHTS.get(), List.copyOf(lights));
            }
            if(!context.getLevel().isClientSide())
            {
                player.sendOverlayMessage(Component.translatable("message.cfm.light_switch.linked", lights.size()));
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if(player.isShiftKeyDown() && stack.has(ModDataComponents.LINKED_LIGHTS.get()))
        {
            stack.remove(ModDataComponents.LINKED_LIGHTS.get());
            if(!level.isClientSide())
            {
                player.sendOverlayMessage(Component.translatable("message.cfm.light_switch.cleared"));
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(level, player, hand);
    }

    // The switch has to be within reach of every light it controls
    @Override
    protected boolean canPlace(BlockPlaceContext context, net.minecraft.world.level.block.state.BlockState state)
    {
        BlockPos placed = context.getClickedPos();
        List<BlockPos> lights = context.getItemInHand().getOrDefault(ModDataComponents.LINKED_LIGHTS.get(), List.of());
        for(BlockPos light : lights)
        {
            if(Math.sqrt(light.distSqr(placed)) > MAX_DISTANCE)
            {
                if(context.getPlayer() != null && !context.getLevel().isClientSide())
                {
                    context.getPlayer().sendOverlayMessage(Component.translatable("message.cfm.light_switch.too_far", MAX_DISTANCE));
                }
                return false;
            }
        }
        return super.canPlace(context, state);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag)
    {
        tooltip.accept(Component.translatable("tooltip.cfm.light_switch").withStyle(ChatFormatting.GRAY));
        int linked = stack.getOrDefault(ModDataComponents.LINKED_LIGHTS.get(), List.of()).size();
        if(linked > 0)
        {
            tooltip.accept(Component.translatable("tooltip.cfm.light_switch.linked", linked).withStyle(ChatFormatting.YELLOW));
        }
    }
}
