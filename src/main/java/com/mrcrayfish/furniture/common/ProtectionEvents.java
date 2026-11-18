package com.mrcrayfish.furniture.common;

import com.mrcrayfish.furniture.core.ModItems;
import com.mrcrayfish.furniture.tileentity.MailBoxBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

/**
 * Only a mail box's owner can break it (1.12.2); a creative-mode player holding the hammer can break anyone's, to clear up.
 */
public class ProtectionEvents
{
    @SubscribeEvent
    public void onBreakBlock(BreakBlockEvent event)
    {
        Player player = event.getPlayer();
        if(player == null || !(event.getLevel().getBlockEntity(event.getPos()) instanceof MailBoxBlockEntity mailBox))
            return;

        if(mailBox.getOwnerId() == null || player.getUUID().equals(mailBox.getOwnerId()))
            return;

        if(player.getAbilities().instabuild && player.getMainHandItem().is(ModItems.HAMMER.get()))
            return;

        event.setCanceled(true);
        player.sendSystemMessage(Component.translatable("message.cfm.mail_box_break").withStyle(ChatFormatting.RED));
    }
}
