package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.Injector;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.PluginService;
import com.github.theprogmatheus.mc.plugin.spawnerx.listener.lang.PlayerJoinLangConfig;
import com.github.theprogmatheus.mc.plugin.spawnerx.listener.mobs.MobDeathListener;
import com.github.theprogmatheus.mc.plugin.spawnerx.listener.mobs.MobEntityBehaviorController;
import com.github.theprogmatheus.mc.plugin.spawnerx.listener.mobs.MobSpawnListener;
import com.github.theprogmatheus.mc.plugin.spawnerx.listener.player.PlayerJoinListener;
import com.github.theprogmatheus.mc.plugin.spawnerx.listener.spawners.SpawnerBlockBreakListener;
import com.github.theprogmatheus.mc.plugin.spawnerx.listener.spawners.SpawnerBlockInteractListener;
import com.github.theprogmatheus.mc.plugin.spawnerx.listener.spawners.SpawnerBlockPlaceListener;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ListenerService extends PluginService {


    private final SpawnerX plugin;
    private final Injector injector;
    private PluginManager pluginManager;

    /**
     * Register your listeners here
     */
    private void registerAllListeners() {
        registerListener(PlayerJoinLangConfig.class);
        registerListener(SpawnerBlockPlaceListener.class);
        registerListener(SpawnerBlockBreakListener.class);
        registerListener(SpawnerBlockInteractListener.class);
        registerListener(MobEntityBehaviorController.class);
        registerListener(MobSpawnListener.class);
        registerListener(MobDeathListener.class);
        registerListener(PlayerJoinListener.class);
    }

    private void unregisterAllListeners() {
        HandlerList.unregisterAll(this.plugin);
    }


    @Override
    public void startup() {
        this.pluginManager = Bukkit.getPluginManager();
        registerAllListeners();
    }

    @Override
    public void shutdown() {
        unregisterAllListeners();
    }

    private void registerListener(Class<? extends Listener> listenerClass) {
        this.pluginManager.registerEvents(this.injector.getInstance(listenerClass), this.plugin);
    }
}
