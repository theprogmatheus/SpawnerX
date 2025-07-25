package com.github.theprogmatheus.mc.plugin.spawnerx.listener.spawners;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlock;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlockConfig;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.RequiredArgsConstructor;
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

        var player = event.getPlayer();
        var block = event.getBlock();
        if (!SpawnerBlock.isValidBukkitSpawnerBlock(block))
            return;

        var spawnerConfigId = SpawnerBlockConfig.getSpawnerBlockConfigId(plugin, event.getItemInHand());
        if (spawnerConfigId == null)
            return;

        var spawnerConfigLink = LinkedObject.getLink(SpawnerBlockConfig.class, spawnerConfigId);
        if (spawnerConfigLink.isEmpty()) {
            event.setCancelled(true);
            player.sendMessage("§cEste spawner não está ativo no servidor, se você acha que isso é um bug, por favor fale com um admin.");
            return;
        }

        new SpawnerBlock(block, spawnerConfigLink.get()).link();
    }

}
