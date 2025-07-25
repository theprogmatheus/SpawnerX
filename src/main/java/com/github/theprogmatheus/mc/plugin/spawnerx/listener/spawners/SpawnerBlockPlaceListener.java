package com.github.theprogmatheus.mc.plugin.spawnerx.listener.spawners;

import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class SpawnerBlockPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
            return;

        var block = event.getBlock();
        if (!SpawnerBlock.isValidBukkitSpawnerBlock(block))
            return;

        new SpawnerBlock(block).link();
    }


}
