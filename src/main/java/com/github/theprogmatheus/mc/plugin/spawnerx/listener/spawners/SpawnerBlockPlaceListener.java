package com.github.theprogmatheus.mc.plugin.spawnerx.listener.spawners;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlock;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlockConfig;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.inject.Inject;


@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpawnerBlockPlaceListener implements Listener {

    private final transient SpawnerX plugin;

    @EventHandler(priority = EventPriority.MONITOR)
    void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
            return;

        var block = event.getBlock();
        if (!SpawnerBlock.isValidBukkitSpawnerBlock(block))
            return;

        var configLink = LinkedObject.getLink(SpawnerBlockConfig.class, EntityType.COW.name());
        if (configLink.isEmpty())
            return;

        new SpawnerBlock(block, configLink.get()).link();
    }

}
