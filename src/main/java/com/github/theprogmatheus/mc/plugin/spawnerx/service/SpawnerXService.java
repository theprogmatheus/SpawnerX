package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.repository.SpawnerBlockRepository;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlockConfig;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.PluginService;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpawnerXService extends PluginService {

    private final transient SpawnerX plugin;
    private final transient SpawnerBlockRepository spawnerBlockRepository;


    @Override
    public void startup() {
        SpawnerBlockConfig.loadDefaults(this.plugin);
    }


    @Override
    public void shutdown() {
        super.shutdown();
    }


    public void loadAllSpawnerBlocks(){
    }

}
