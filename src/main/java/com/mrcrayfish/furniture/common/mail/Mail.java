package com.mrcrayfish.furniture.common.mail;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

/**
 * An item sent to a mail box, waiting in the post office until the mail box pulls it.
 *
 * Author: MrCrayfish
 */
public record Mail(String note, ItemStack stack, String sender)
{
    public static final Codec<Mail> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("Note", "").forGetter(Mail::note),
            ItemStack.OPTIONAL_CODEC.fieldOf("Item").forGetter(Mail::stack),
            Codec.STRING.optionalFieldOf("Sender", "").forGetter(Mail::sender)
    ).apply(instance, Mail::new));

    public String getNote()
    {
        return this.note;
    }

    public ItemStack getStack()
    {
        return this.stack;
    }

    public String getSender()
    {
        return this.sender;
    }
}
