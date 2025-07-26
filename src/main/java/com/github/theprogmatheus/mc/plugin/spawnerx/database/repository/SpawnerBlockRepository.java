package com.github.theprogmatheus.mc.plugin.spawnerx.database.repository;

import com.github.theprogmatheus.mc.plugin.spawnerx.database.entity.SpawnerBlockEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;

@Getter
@Singleton
public class SpawnerBlockRepository {

    private final Dao<SpawnerBlockEntity, Long> spawnerBlockEntities;

    @Inject
    public SpawnerBlockRepository(ConnectionSource connectionSource) throws SQLException {
        this.spawnerBlockEntities = DaoManager.createDao(connectionSource, SpawnerBlockEntity.class);
    }
}
