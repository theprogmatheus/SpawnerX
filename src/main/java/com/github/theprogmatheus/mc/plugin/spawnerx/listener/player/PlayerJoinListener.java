package com.github.theprogmatheus.mc.plugin.spawnerx.listener.player;

import com.github.theprogmatheus.mc.plugin.spawnerx.service.PlayerProfileService;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerJoinListener implements Listener {

    private final PlayerProfileService playerProfileService;

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        this.playerProfileService.loadPlayerProfile(event.getPlayer());
    }
}
