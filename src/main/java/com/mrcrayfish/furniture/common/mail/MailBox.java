package com.mrcrayfish.furniture.common.mail;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A registered mail box and the mail queued for it.
 *
 * Author: MrCrayfish
 */
public class MailBox
{
    public static final Codec<MailBox> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("MailBoxUUID").forGetter(MailBox::getId),
            Codec.STRING.fieldOf("MailBoxName").forGetter(MailBox::getName),
            UUIDUtil.CODEC.fieldOf("OwnerUUID").forGetter(MailBox::getOwnerId),
            Codec.STRING.fieldOf("OwnerName").forGetter(MailBox::getOwnerName),
            BlockPos.CODEC.fieldOf("Pos").forGetter(MailBox::getPos),
            Level.RESOURCE_KEY_CODEC.optionalFieldOf("World", Level.OVERWORLD).forGetter(MailBox::getLevelResourceKey),
            Mail.CODEC.listOf().optionalFieldOf("MailStorage", List.of()).forGetter(MailBox::getMailStorage)
    ).apply(instance, MailBox::new));

    private final UUID id;
    private String name;
    private final UUID ownerId;
    private final String ownerName;
    private final BlockPos pos;
    private final ResourceKey<Level> levelResourceKey;
    private final List<Mail> mailStorage;

    public MailBox(UUID id, String name, UUID ownerId, String ownerName, BlockPos pos, ResourceKey<Level> levelResourceKey)
    {
        this(id, name, ownerId, ownerName, pos, levelResourceKey, List.of());
    }

    private MailBox(UUID id, String name, UUID ownerId, String ownerName, BlockPos pos, ResourceKey<Level> levelResourceKey, List<Mail> mailStorage)
    {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.pos = pos;
        this.levelResourceKey = levelResourceKey;
        this.mailStorage = new ArrayList<>(mailStorage);
    }

    public UUID getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public UUID getOwnerId()
    {
        return this.ownerId;
    }

    public String getOwnerName()
    {
        return this.ownerName;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    public ResourceKey<Level> getLevelResourceKey()
    {
        return this.levelResourceKey;
    }

    public void addMail(Mail mail)
    {
        this.mailStorage.add(mail);
    }

    public List<Mail> getMailStorage()
    {
        return this.mailStorage;
    }

    public int getMailCount()
    {
        return this.mailStorage.size();
    }

    public MailBoxEntry toEntry()
    {
        return new MailBoxEntry(this.id, this.name, this.ownerId, this.ownerName);
    }
}
