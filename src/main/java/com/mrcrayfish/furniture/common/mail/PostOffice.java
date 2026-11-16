package com.mrcrayfish.furniture.common.mail;

import com.mojang.serialization.Codec;
import com.mrcrayfish.furniture.FurnitureConfig;
import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.tileentity.MailBoxBlockEntity;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Every player's mail boxes and the mail queued for them, saved with the world.
 *
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = Reference.MOD_ID)
public class PostOffice extends SavedData
{
    private static final Codec<Map<UUID, Map<UUID, MailBox>>> MAIL_BOXES_CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.unboundedMap(UUIDUtil.STRING_CODEC, MailBox.CODEC));

    private static final Codec<PostOffice> CODEC = MAIL_BOXES_CODEC.fieldOf("PlayerMailBoxes").codec().xmap(PostOffice::new, office -> office.playerMailboxMap);

    private static final SavedDataType<PostOffice> TYPE = new SavedDataType<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "post_office"), PostOffice::new, CODEC);

    private final Map<UUID, Map<UUID, MailBox>> playerMailboxMap = new HashMap<>();

    public PostOffice()
    {
    }

    private PostOffice(Map<UUID, Map<UUID, MailBox>> playerMailboxMap)
    {
        playerMailboxMap.forEach((playerId, mailBoxes) -> this.playerMailboxMap.put(playerId, new HashMap<>(mailBoxes)));
    }

    public static void registerMailBox(ServerPlayer player, UUID mailBoxId, String name, BlockPos pos)
    {
        PostOffice office = get(player.level().getServer());
        Map<UUID, MailBox> mailBoxMap = office.playerMailboxMap.computeIfAbsent(player.getUUID(), uuid -> new HashMap<>());
        mailBoxMap.put(mailBoxId, new MailBox(mailBoxId, name, player.getUUID(), player.getName().getString(), pos, player.level().dimension()));
        office.setDirty();
    }

    public static void unregisterMailBox(UUID playerId, UUID mailBoxId)
    {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if(server != null)
        {
            PostOffice office = get(server);
            Map<UUID, MailBox> mailBoxMap = office.playerMailboxMap.computeIfAbsent(playerId, uuid -> new HashMap<>());
            mailBoxMap.remove(mailBoxId);
            office.setDirty();
        }
    }

    public static List<MailBox> getMailBoxes(ServerPlayer player)
    {
        PostOffice office = get(player.level().getServer());
        return office.playerMailboxMap.values().stream().flatMap(map -> map.values().stream()).collect(Collectors.toList());
    }

    public static boolean sendMailToPlayer(UUID playerId, UUID mailBoxId, Mail mail)
    {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if(server != null)
        {
            PostOffice office = get(server);
            Map<UUID, MailBox> mailBoxMap = office.playerMailboxMap.computeIfAbsent(playerId, uuid -> new HashMap<>());
            MailBox mailBox = mailBoxMap.get(mailBoxId);
            if(mailBox != null && mailBox.getMailCount() < FurnitureConfig.COMMON.maxMailQueue.get())
            {
                mailBox.addMail(mail);
                office.setDirty();
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a supplier which provides mail for the specified mail box. Every time you call
     * {@link Supplier#get} it will remove mail from the queue. You should also not store this
     * supplier as it looks up the running server.
     */
    public static Supplier<Mail> getMailForPlayerMailBox(UUID playerId, UUID mailBoxId)
    {
        return () ->
        {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if(server != null)
            {
                PostOffice office = get(server);
                Map<UUID, MailBox> mailBoxMap = office.playerMailboxMap.get(playerId);
                if(mailBoxMap != null)
                {
                    MailBox mailBox = mailBoxMap.get(mailBoxId);
                    if(mailBox != null && !mailBox.getMailStorage().isEmpty())
                    {
                        office.setDirty();
                        return mailBox.getMailStorage().remove(0);
                    }
                }
            }
            return null;
        };
    }

    public static boolean setMailBoxName(UUID playerId, UUID mailBoxId, String name)
    {
        name = name.trim();
        if(name.isEmpty())
            return false;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if(server == null)
            return false;

        PostOffice office = get(server);
        Map<UUID, MailBox> mailBoxMap = office.playerMailboxMap.get(playerId);
        if(mailBoxMap == null)
            return false;

        MailBox mailBox = mailBoxMap.get(mailBoxId);
        if(mailBox == null)
            return false;

        mailBox.setName(name);
        office.setDirty();

        ServerLevel level = server.getLevel(mailBox.getLevelResourceKey());
        if(level == null || !level.isLoaded(mailBox.getPos()))
            return false;

        if(!(level.getBlockEntity(mailBox.getPos()) instanceof MailBoxBlockEntity mailBoxBlockEntity))
            return false;

        mailBoxBlockEntity.setMailBoxName(name);
        BlockEntityUtil.sendUpdatePacket(mailBoxBlockEntity);
        return true;
    }

    public static boolean isRegistered(UUID playerId, UUID mailBoxId)
    {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if(server != null)
        {
            Map<UUID, MailBox> mailBoxMap = get(server).playerMailboxMap.get(playerId);
            return mailBoxMap != null && mailBoxMap.containsKey(mailBoxId);
        }
        return false;
    }

    private static PostOffice get(MinecraftServer server)
    {
        return Objects.requireNonNull(server.overworld()).getDataStorage().computeIfAbsent(TYPE);
    }

    /*
     * Cleans up invalid mail boxes or mail boxes removed by other means than a player.
     */
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event)
    {
        MinecraftServer server = event.getServer();
        if(server.getTickCount() % 1200 != 0)
            return;

        PostOffice office = get(server);
        office.playerMailboxMap.values().forEach(map ->
        {
            Predicate<MailBox> removePredicate = mailBox ->
            {
                BlockPos pos = mailBox.getPos();
                ServerLevel level = server.getLevel(mailBox.getLevelResourceKey());
                if(level == null)
                    return true;

                if(!level.isLoaded(pos))
                    return false;

                if(level.getBlockEntity(pos) instanceof MailBoxBlockEntity mailBoxBlockEntity)
                {
                    return mailBoxBlockEntity.getId() == null || !Objects.equals(mailBoxBlockEntity.getId(), mailBox.getId());
                }
                return true;
            };
            if(map.values().removeIf(removePredicate))
            {
                office.setDirty();
            }
        });
    }
}
