package com.github.theprogmatheus.mc.plugin.spawnerx.lib;

import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityTypes;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockEntityData;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlock;
import com.github.theprogmatheus.mc.plugin.spawnerx.listener.packets.SpawnerPacketsListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import static com.github.retrooper.packetevents.PacketEvents.getAPI;

public class PacketEventsProvider {

    private static SpawnerPacketsListener spawnerPacketsListener;

    public static void init(@NotNull Plugin plugin) {
        registerListener(spawnerPacketsListener = new SpawnerPacketsListener(plugin));
    }

    public static void terminate(@NotNull Plugin plugin) {
        if (spawnerPacketsListener != null) {
            unregisterListener(spawnerPacketsListener);
            spawnerPacketsListener = null;
        }
    }

    public static void registerListener(@NotNull PacketListenerCommon listener) {
        getAPI().getEventManager().registerListener(listener);
    }

    public static void unregisterListener(@NotNull PacketListenerCommon listener) {
        getAPI().getEventManager().unregisterListener(listener);
    }

    public static void hideSpawnerAnimation(@NotNull Player player, @NotNull SpawnerBlock spawnerBlock) {
        var location = spawnerBlock.getOriginal();
        var blockPos = new Vector3i(location.getX(), location.getY(), location.getZ());

        var nbt = new NBTCompound();

        var spawnData = new NBTCompound();
        var emptyEntity = new NBTCompound();
        emptyEntity.setTag("id", new NBTString(""));
        spawnData.setTag("entity", emptyEntity);
        nbt.setTag("SpawnData", spawnData);

        short delay = (short) spawnerBlock.getConfig().getDelay();
        nbt.setTag("Delay", new NBTShort(delay));

        var packet = new WrapperPlayServerBlockEntityData(
                blockPos,
                BlockEntityTypes.MOB_SPAWNER,
                nbt
        );

        getAPI().getPlayerManager().sendPacket(player, packet);
    }
}
