package com.github.theprogmatheus.mc.plugin.spawnerx.listener.mobs;

import com.github.theprogmatheus.mc.plugin.spawnerx.domain.BlockLocationKey;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.MobEntity;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlock;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Arrays;

import static com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject.getLink;

public class MobSpawnListener implements Listener {


    @EventHandler(priority = EventPriority.MONITOR)
    void onSpawn(SpawnerSpawnEvent event) {
        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof LivingEntity entity))
            return;

        var spawnerLink = getLink(SpawnerBlock.class, BlockLocationKey.fromBukkitLocation(event.getSpawner().getLocation()));
        if (spawnerLink.isEmpty())
            return;

        var spawnerBlock = spawnerLink.get();
        var spawnedMobs = spawnerBlock.getSpawnedMobs();

        if (spawnedMobs.isEmpty())
            spawnedMobs.add(MobEntity.newMobEntity(entity));
        else {
            event.setCancelled(true);
            spawnedMobs.get(0).stack();
        }
    }


    @EventHandler
    void onChunkLoad(ChunkLoadEvent event) {
        Arrays.stream(event.getChunk().getEntities())
                .filter(entity -> (entity instanceof LivingEntity living)
                        && !living.isDead()
                        && MobEntity.hasMobEntityData(living)
                        && LinkedObject.getLink(MobEntity.class, living.getUniqueId()).isEmpty())
                .forEach(entity -> MobEntity.newMobEntity((LivingEntity) entity));
    }
}
