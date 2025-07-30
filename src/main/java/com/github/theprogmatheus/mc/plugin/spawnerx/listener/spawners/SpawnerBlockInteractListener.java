package com.github.theprogmatheus.mc.plugin.spawnerx.listener.spawners;

import com.github.theprogmatheus.mc.plugin.spawnerx.domain.BlockLocationKey;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlock;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpawnerBlockInteractListener implements Listener {

    @EventHandler
    public void onPlayerUseSpawnEgg(PlayerInteractEvent event) {
        var clickedBlock = event.getClickedBlock();
        var item = event.getItem();
        var action = event.getAction();

        if (action != Action.RIGHT_CLICK_BLOCK)
            return;

        if (item == null || !isSpawnEgg(item))
            return;

        if (clickedBlock == null)
            return;

        var spawnerOptional = LinkedObject.getLink(SpawnerBlock.class, BlockLocationKey.fromBukkitLocation(clickedBlock.getLocation()));
        spawnerOptional.ifPresent(spawner -> {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cNão é possível alterar o tipo de mob desse spawner.");
        });
    }

    private boolean isSpawnEgg(@NotNull ItemStack item) {
        return item.getType().name().endsWith("_SPAWN_EGG");
    }

}
