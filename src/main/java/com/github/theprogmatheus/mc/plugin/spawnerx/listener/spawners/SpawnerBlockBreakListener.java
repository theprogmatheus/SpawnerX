package com.github.theprogmatheus.mc.plugin.spawnerx.listener.spawners;

import com.github.theprogmatheus.mc.plugin.spawnerx.domain.BlockLocationKey;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlock;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlockPersist;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpawnerBlockBreakListener implements Listener {

    private final transient SpawnerBlockPersist spawnerBlockPersist;

    @EventHandler(priority = EventPriority.MONITOR)
    void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;

        var block = event.getBlock();
        if (!SpawnerBlock.isValidBukkitSpawnerBlock(block))
            return;

        LinkedObject.getLink(SpawnerBlock.class, BlockLocationKey.fromBukkitLocation(block.getLocation()))
                .ifPresent(spawnerBlock -> {
                    spawnerBlock.handleBlockBreak(event);
                    spawnerBlockPersist.delete(spawnerBlock);
                });
    }
}
