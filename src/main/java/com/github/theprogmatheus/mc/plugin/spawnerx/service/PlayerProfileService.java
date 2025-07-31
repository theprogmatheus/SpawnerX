package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.entity.PlayerProfileEntity;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.mappers.PlayerProfileMapper;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.repository.PlayerProfileRepository;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.PlayerProfile;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.Injector;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.PluginService;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.ExecutorTimeLogger;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerProfileService extends PluginService {

    private final transient Injector injector;
    private final transient SpawnerX plugin;
    private final transient Logger log;

    private PlayerProfileRepository playerProfileRepository;
    private PlayerProfileMapper playerProfilerMapper;

    @Override
    public void startup() {
        this.playerProfileRepository = this.injector.getInstance(PlayerProfileRepository.class);
        this.playerProfilerMapper = this.injector.getInstance(PlayerProfileMapper.class);

        loadPlayerProfiles();
    }


    @Override
    public void shutdown() {
        savePlayerProfiles();
    }


    public void loadPlayerProfiles() {
        ExecutorTimeLogger.executeAndLogTime(this.log, "Load PlayerProfiles", () -> {
            try {
                this.playerProfileRepository.queryForAll()
                        .stream()
                        .map(this.playerProfilerMapper::mapTo)
                        .forEach(PlayerProfile::link);
            } catch (SQLException e) {
                log.log(Level.SEVERE, "It was not possible to load the player profiles.", e);
            }
        });
    }

    //   Por enquanto, não há muito dados importantes a serem salvos, poranto não há necessidade de salvar de tempos em tempos ainda.
    public void savePlayerProfiles() {
        ExecutorTimeLogger.executeAndLogTime(this.log, "Save PlayerProfiles", () -> {
            LinkedObject.getLinkerMap(PlayerProfile.class).ifPresent(map -> {
                try {
                    this.playerProfileRepository.callBatchTasks(() -> {
                        for (PlayerProfile playerProfile : map.values()) {
                            PlayerProfileEntity playerProfileEntity = this.playerProfilerMapper.mapFrom(playerProfile);

                            var result = this.playerProfileRepository.createOrUpdate(playerProfileEntity);
                            if (result.isCreated())
                                playerProfile.setDbId(playerProfileEntity.getId());
                        }
                        return null;
                    });
                } catch (Exception e) {
                    log.log(Level.SEVERE, "An error occurred when trying to save the PlayerProfile's in the database.", e);
                }
            });
        });
    }
}
