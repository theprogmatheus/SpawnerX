package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.DatabaseSQLManager;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.Injector;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.PluginService;
import com.zaxxer.hikari.HikariConfig;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.sql.SQLException;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class DatabaseSQLService extends PluginService {


    private final SpawnerX plugin;
    private final Injector injector;
    private DatabaseSQLManager databaseManager;


    /**
     * Change how to load the database config
     */
    private HikariConfig loadDatabaseConfig() {
        var inTestMode = Boolean.parseBoolean(System.getProperty("BUKKIT_PLUGIN_DB_IN_TEST_MODE", "false"));
        if (inTestMode)
            return loadTestDatabaseConfig();

        return loadLocalDatabaseConfig();
    }


    private HikariConfig loadLocalDatabaseConfig() {
        var storageFile = new File(this.plugin.getDataFolder(), "storage-h2");
        var dataFolder = storageFile.getParentFile();
        if (!dataFolder.exists())
            dataFolder.mkdirs();

        var config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:file:%s".formatted(storageFile.toPath().toAbsolutePath()));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return config;
    }

    private HikariConfig loadTestDatabaseConfig() {
        var config = new HikariConfig();
        String dbName = UUID.randomUUID().toString();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1".formatted(dbName));
        return config;
    }

    @Override
    public void startup() {
        this.databaseManager = new DatabaseSQLManager(this.injector, loadDatabaseConfig());
        try {
            this.databaseManager.openConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void shutdown() {
        try {
            this.databaseManager.closeConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
