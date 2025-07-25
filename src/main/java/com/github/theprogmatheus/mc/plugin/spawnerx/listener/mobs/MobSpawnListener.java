package com.github.theprogmatheus.mc.plugin.spawnerx.listener.mobs;

import com.github.theprogmatheus.mc.plugin.spawnerx.domain.BlockLocationKey;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.MobEntity;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

import static com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject.getLink;

public class MobSpawnListener implements Listener {


    @EventHandler(priority = EventPriority.MONITOR)
    void onSpawn(SpawnerSpawnEvent event) {
        if (event.isCancelled())
            return;

        var spawnerLink = getLink(SpawnerBlock.class, BlockLocationKey.fromBukkitLocation(event.getSpawner().getLocation()));
        if (spawnerLink.isEmpty())
            return;

        var spawnerBlock = spawnerLink.get();
        var spawnedMobs = spawnerBlock.getSpawnedMobs();

        if (spawnedMobs.isEmpty())
            spawnedMobs.add(MobEntity.newMobEntity(event.getEntity()));
        else {
            event.setCancelled(true);
            spawnedMobs.get(0).stack();
        }
    }
}
