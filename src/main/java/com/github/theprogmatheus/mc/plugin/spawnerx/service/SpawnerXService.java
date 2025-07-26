package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.entity.SpawnerBlockEntity;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.mappers.SpawnerBlockMapper;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.repository.SpawnerBlockRepository;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlock;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlockConfig;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.Injector;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.PluginService;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.ExecutorTimeLogger;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpawnerXService extends PluginService {

    private final transient Injector injector;
    private final transient SpawnerX plugin;
    private final transient Logger log;
    private final transient SpawnerBlockRepository spawnerBlockRepository;

    private SpawnerBlockMapper spawnerBlockMapper;
    private BukkitTask autoSaveAndPurgeTask;

    @Override
    public void startup() {
        SpawnerBlockConfig.loadDefaults(this.plugin);

        this.spawnerBlockMapper = this.injector.getInstance(SpawnerBlockMapper.class);
        this.autoSaveAndPurgeTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, this::executeAndLogTimeSaveAndPurge, 20 * 300, 20 * 300);

        this.executeAndLogTimeLoadSpawners();
    }


    @Override
    public void shutdown() {
        if (this.autoSaveAndPurgeTask != null)
            this.autoSaveAndPurgeTask.cancel();
        executeAndLogTimeSaveAndPurge();
    }

    public synchronized void executeAndLogTimeLoadSpawners() {
        ExecutorTimeLogger.executeAndLogTime(this.log, "loadSpawners", () -> {
            try {
                loadSpawnerBlocks();
            } catch (SQLException e) {
                log.log(Level.SEVERE, "It was not possible to load the database spawners.", e);
            }
        });
    }

    public synchronized void executeAndLogTimeSaveAndPurge() {
        ExecutorTimeLogger.executeAndLogTime(this.log, "saveSpawnerBlocks", () -> {
            try {
                saveSpawnerBlocks();
            } catch (Exception e) {
                log.log(Level.SEVERE, "An error occurred when trying to save the spawners in the database.", e);
            }
        });

        ExecutorTimeLogger.executeAndLogTime(this.log, "purge", () -> {
            try {
                purge();
            } catch (Exception e) {
                log.log(Level.SEVERE, "An error occurred when trying to clean the database of broken spawners", e);
            }
        });
    }

    public void loadSpawnerBlocks() throws SQLException {
        this.spawnerBlockRepository
                .queryForAll()
                .stream()
                .map(spawnerBlockMapper::mapTo)
                .filter(SpawnerBlock::isOk)
                .forEach(SpawnerBlock::link);
    }

    public void saveSpawnerBlocks() throws Exception {
        var link = LinkedObject.getLinkerMap(SpawnerBlock.class);
        if (link.isPresent()) {
            var toSave = link.get()
                    .values()
                    .stream()
                    .filter(SpawnerBlock::isOk)
                    .map(spawnerBlockMapper::mapFrom)
                    .toList();

            this.spawnerBlockRepository.callBatchTasks(() -> {
                for (SpawnerBlockEntity entity : toSave)
                    this.spawnerBlockRepository.createOrUpdate(entity);
                return null;
            });
        }
    }

    public void purge() throws SQLException {
        var toDelete = this.spawnerBlockRepository
                .queryForAll()
                .stream()
                .map(spawnerBlockMapper::mapTo)
                .filter(SpawnerBlock::isBroken)
                .map(spawnerBlockMapper::mapFrom)
                .toList();
        this.spawnerBlockRepository.delete(toDelete);
    }

}
