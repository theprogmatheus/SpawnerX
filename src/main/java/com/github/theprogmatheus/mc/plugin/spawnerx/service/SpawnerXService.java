package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.repository.SpawnerBlockRepository;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.*;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.Injector;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.PluginService;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.ExecutorTimeLogger;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.PacketEventsUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.logging.Logger;

@Getter
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpawnerXService extends PluginService {

    private final transient Injector injector;
    private final transient SpawnerX plugin;
    private final transient Logger log;
    private final transient ConfigurationService configurationService;

    private SpawnerBlockRepository spawnerBlockRepository;
    private SpawnerBlockPersist spawnerBlockPersist;
    private File mobDropControllerGlobalFile;

    @Override
    public void startup() {
        this.spawnerBlockRepository = this.injector.getInstance(SpawnerBlockRepository.class);
        this.spawnerBlockPersist = this.injector.getInstance(SpawnerBlockPersist.class);

        MobConfig.loadDefaults(this.plugin);
        SpawnerBlockConfig.loadDefaults(this.plugin);
        loadGlobalMobDropController();
        MobEntity.loadAllPersisted();
        loadPacketEvents();
    }


    @Override
    public void shutdown() {
        saveGlobalMobDropController();
        unloadPacketEvents();
        this.spawnerBlockPersist.shutdown();
    }

    public void loadGlobalMobDropController() {
        if (this.mobDropControllerGlobalFile == null)
            this.mobDropControllerGlobalFile = new File(this.plugin.getDataFolder(), "mob-drop-controller.global.dat");

        if (this.mobDropControllerGlobalFile.exists())
            MobDropController.setGlobal(MobDropController.deserializeFromFile(this.mobDropControllerGlobalFile));
    }

    public void saveGlobalMobDropController() {
        var global = MobDropController.getGlobal();
        if (global.getDropSnapshots().isEmpty())
            return;
        global.serializeInFile(this.mobDropControllerGlobalFile);
    }

    public void loadPacketEvents() {
        if (PacketEventsUtils.isPacketEventsAvailable())
            com.github.theprogmatheus.mc.plugin.spawnerx.lib.
                    PacketEventsProvider.init(this.plugin);
    }

    public void unloadPacketEvents() {
        if (PacketEventsUtils.isPacketEventsAvailable())
            com.github.theprogmatheus.mc.plugin.spawnerx.lib.
                    PacketEventsProvider.terminate(this.plugin);
    }


    public void reloadSpawnerBlocks() {
        ExecutorTimeLogger.executeAndLogTime(this.log, "Reload SpawnerBlockConfig's", () -> {
            this.configurationService.reload();
            SpawnerBlockConfig.loadDefaults(this.plugin);
            LinkedObject.getLinkerMap(SpawnerBlock.class).ifPresent(map -> map.values().forEach(spawner -> {
                if (spawner.isBroken())
                    return;
                spawner.getConfig().applyToSpawnerBlock(spawner);
            }));
        });
    }

    public void reloadMobEntities() {
        ExecutorTimeLogger.executeAndLogTime(this.log, "Reload MobConfig's", () -> {
            this.configurationService.reload();
            MobConfig.loadDefaults(this.plugin);
            LinkedObject.getLinkerMap(MobEntity.class).ifPresent(map -> map.values().forEach(mob -> {
                if (mob.isBroken())
                    return;
                mob.getConfig().applyToEntity(mob.getEntity());
            }));
        });
    }
}
