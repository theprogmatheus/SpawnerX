package com.github.theprogmatheus.mc.plugin.spawnerx.listener.spawners;

import com.github.theprogmatheus.mc.plugin.spawnerx.database.repository.SpawnerBlockRepository;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.ChunkCoord;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlockLoader;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;

public class SpawnerBlockLoadListener implements Listener {

    private final SpawnerBlockLoader loader;

    @Inject
    public SpawnerBlockLoadListener(Plugin plugin, SpawnerBlockRepository spawnerBlockRepository) {
        this.loader = new SpawnerBlockLoader(plugin, spawnerBlockRepository);
        this.loadLoadedChunks();
    }


    private void loadLoadedChunks() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk loadedChunk : world.getLoadedChunks()) {
                this.loader.loadChunk(loadedChunk);
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        this.loader.loadChunk(event.getChunk());
    }

    @EventHandler
    public void onSpawnerBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Chunk chunk = block.getChunk();
        if ((block.getState() instanceof CreatureSpawner) && this.loader.getChunksLoading().contains(ChunkCoord.fromChunk(chunk))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cEste spawner está sendo verificado, aguarde...");
        }
    }

}
