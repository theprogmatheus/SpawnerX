package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.database.repository.SpawnerBlockRepository;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class SpawnerBlockLoader {

    private final Plugin plugin;
    private final SpawnerBlockRepository repository;
    private final Set<ChunkCoord> chunksLoading;

    public SpawnerBlockLoader(Plugin plugin, SpawnerBlockRepository repository) {
        this.plugin = plugin;
        this.repository = repository;
        this.chunksLoading = ConcurrentHashMap.newKeySet();
    }

    public void loadChunk(@NotNull Chunk chunk) {
        ChunkCoord chunkCoord = ChunkCoord.fromChunk(chunk);
        if (this.chunksLoading.contains(chunkCoord))
            return;

        if (Arrays.stream(chunk.getTileEntities())
                .noneMatch(state -> state instanceof CreatureSpawner))
            return;

        this.chunksLoading.add(chunkCoord);
        CompletableFuture.supplyAsync(() -> this.repository.getByChunk(chunk.getWorld().getName(), chunk.getX(), chunk.getZ()))
                .thenAccept(spawnerEntities -> {

                    spawnerEntities.stream()
                            .map(entity -> {
                                BlockLocationKey location = new BlockLocationKey(
                                        entity.getWorld(),
                                        entity.getX(),
                                        entity.getY(),
                                        entity.getZ(),
                                        chunkCoord
                                );

                                if (LinkedObject.getLink(SpawnerBlock.class, location).isPresent())
                                    return Optional.<SpawnerBlock>empty();

                                return LinkedObject.getLink(SpawnerBlockConfig.class, entity.getConfig())
                                        .map(config -> new SpawnerBlock(location, config));
                            })
                            .flatMap(Optional::stream)
                            .forEach(spawner -> Bukkit.getScheduler().runTask(plugin, () -> {
                                if (spawner.isOk()) {
                                    spawner.link();
                                }
                            }));

                }).whenComplete((result, throwable) -> this.chunksLoading.remove(chunkCoord));
    }

}
