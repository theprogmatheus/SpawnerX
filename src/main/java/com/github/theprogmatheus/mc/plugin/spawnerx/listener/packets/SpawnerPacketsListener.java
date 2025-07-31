package com.github.theprogmatheus.mc.plugin.spawnerx.listener.packets;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.chunk.Column;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockEntityData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.BlockLocationKey;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.PlayerProfile;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlock;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
public class SpawnerPacketsListener extends PacketListenerAbstract {

    private final Plugin plugin;

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = event.getUser();
        if (user == null) return;
        UUID uniqueId = user.getUUID();
        if (uniqueId == null)
            return;
        Player player = Bukkit.getPlayer(uniqueId);
        if (player == null)
            return;

        if (event.getPacketType().equals(PacketType.Play.Server.CHUNK_DATA))
            handleChunkDataPacket(event, player);
        else if (event.getPacketType().equals(PacketType.Play.Server.BLOCK_ENTITY_DATA))
            handleBlockEntityDataPacket(event, player);
    }

    private void handleBlockEntityDataPacket(@NotNull PacketSendEvent event, @NotNull Player player) {
        WrapperPlayServerBlockEntityData packet = new WrapperPlayServerBlockEntityData(event);
        NBTCompound spawnData = packet.getNBT().getCompoundTagOrNull("SpawnData");
        if (spawnData == null)
            return;
        NBTCompound entity = spawnData.getCompoundTagOrNull("entity");
        if (entity == null)
            return;
        NBTString id = entity.getStringTagOrNull("id");
        if (id == null || id.getValue() == null || id.getValue().isBlank())
            return;

        Vector3i position = packet.getPosition();
        BlockLocationKey blockLocationKey = new BlockLocationKey(player.getWorld().getName(), position.getX(), position.getY(), position.getZ());
        LinkedObject.getLink(SpawnerBlock.class, blockLocationKey).ifPresent(spawner -> {
            if (!isAnimatedSpawner(player))
                event.setCancelled(true);
        });
    }

    public void handleChunkDataPacket(@NotNull PacketSendEvent event, @NotNull Player player) {
        World world = player.getWorld();

        WrapperPlayServerChunkData packet = new WrapperPlayServerChunkData(event);

        Column column = packet.getColumn();
        Chunk chunk = world.getChunkAt(column.getX(), column.getZ());

        Bukkit.getScheduler().runTask(plugin, () -> {

            BlockState[] tileEntities = chunk.getTileEntities();
            if (tileEntities.length == 0)
                return;
            Arrays.stream(tileEntities)
                    .filter(CreatureSpawner.class::isInstance)
                    .forEach(blockState -> {
                        SpawnerBlock spawner = LinkedObject.getLink(
                                SpawnerBlock.class,
                                BlockLocationKey.fromBukkitLocation(blockState.getLocation())
                        ).orElse(null);

                        if (spawner != null && !isAnimatedSpawner(player))
                            spawner.hideSpawnerAnimation(player);
                    });
        });
    }

    private boolean isAnimatedSpawner(@NotNull Player player) {
        var profile = PlayerProfile.fromPlayer(player);
        return profile.getPlayerData().isAnimatedSpawners();
    }
}
