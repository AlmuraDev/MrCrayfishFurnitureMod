package com.mrcrayfish.furniture.item;

import com.mrcrayfish.furniture.core.ModDataComponents;
import com.mrcrayfish.furniture.inventory.container.PresentMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

/**
 * A present (1.12.2). Use it in the air to open it and put up to four things in, then wrap it, which signs it with your name and
 * closes it for good. Only a wrapped present with something in it can be set down; whoever breaks it gets what is inside.
 *
 * Author: MrCrayfish
 */
public class PresentItem extends BlockItem
{
    public PresentItem(Block block, Item.Properties properties)
    {
        super(block, properties);
    }

    public static boolean isWrapped(ItemStack stack)
    {
        return stack.has(ModDataComponents.PRESENT_FROM.get());
    }

    public static boolean isEmpty(ItemStack stack)
    {
        return stack.getOrDefault(ModDataComponents.PRESENT_CONTENTS.get(), ItemContainerContents.EMPTY).nonEmptyItems().iterator().hasNext() == false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        if(hand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);
        if(player instanceof ServerPlayer serverPlayer)
        {
            if(isWrapped(stack))
            {
                player.sendSystemMessage(Component.translatable("message.cfm.present_wrapped"));
            }
            else
            {
                serverPlayer.openMenu(new SimpleMenuProvider((windowId, inventory, p) -> new PresentMenu(windowId, inventory), stack.getHoverName()));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context)
    {
        ItemStack stack = context.getItemInHand();
        String problem = !isWrapped(stack) ? "message.cfm.present_sign" : isEmpty(stack) ? "message.cfm.present_place" : null;
        if(problem != null)
        {
            if(context.getPlayer() != null && !context.getLevel().isClientSide())
            {
                context.getPlayer().sendSystemMessage(Component.translatable(problem));
            }
            return InteractionResult.FAIL;
        }
        return super.place(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        String from = stack.get(ModDataComponents.PRESENT_FROM.get());
        builder.accept((from != null ? Component.translatable("tooltip.cfm.present_from", from) : Component.translatable("tooltip.cfm.present_unwrapped")).withStyle(ChatFormatting.GRAY));
    }
}
