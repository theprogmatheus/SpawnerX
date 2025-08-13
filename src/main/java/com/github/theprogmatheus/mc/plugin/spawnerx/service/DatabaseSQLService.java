package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.DatabaseSQLManager;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.SqlQueryLoader;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.repository.SpawnerBlockRepository;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.Injector;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.PluginService;
import com.zaxxer.hikari.HikariConfig;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class DatabaseSQLService extends PluginService {


    private final SpawnerX plugin;
    private final Injector injector;
    private DatabaseSQLManager databaseManager;
    private SpawnerBlockRepository spawnerBlockRepository;

    private DatabaseSQLManager loadDatabaseManager() {
        var inTestMode = Boolean.parseBoolean(System.getProperty("BUKKIT_PLUGIN_DB_IN_TEST_MODE", "false"));
        if (inTestMode) {
            SqlQueryLoader queryLoader = new SqlQueryLoader("/sql/sqlite", "spawnerx_", new ConcurrentHashMap<>());
            return new DatabaseSQLManager(this.injector, loadTestDatabaseConfig(), queryLoader);
        }
        return managerForMySQL();
    }

    private HikariConfig loadMysqlDatabaseConfig(String hostname, String database, String username, String password) {
        var config = new HikariConfig();

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(3);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://%s/%s".formatted(hostname, database));
        config.setUsername(username);
        config.setPassword(password);

        return config;
    }


    private HikariConfig loadSqliteDatabaseConfig() {
        var storageFile = new File(this.plugin.getDataFolder(), "storage.db");
        var dataFolder = storageFile.getParentFile();
        if (!dataFolder.exists())
            dataFolder.mkdirs();

        var config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + storageFile.getAbsolutePath());

        config.addDataSourceProperty("foreign_keys", "true");
        config.addDataSourceProperty("journal_mode", "WAL");
        config.addDataSourceProperty("cache_size", "5000");
        config.addDataSourceProperty("synchronous", "NORMAL");
        config.addDataSourceProperty("busy_timeout", "3000");

        config.setMaximumPoolSize(1);
        config.setMinimumIdle(1);
        return config;
    }

    private HikariConfig loadTestDatabaseConfig() {
        var config = new HikariConfig();
        String dbName = UUID.randomUUID().toString();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite::memory:");
        return config;
    }


    private DatabaseSQLManager managerForSQLite() {
        HikariConfig config = loadSqliteDatabaseConfig();
        SqlQueryLoader queryLoader = new SqlQueryLoader("/sql/sqlite", "spawnerx_", new ConcurrentHashMap<>());
        return new DatabaseSQLManager(this.injector, config, queryLoader);
    }

    private DatabaseSQLManager managerForMySQL() {
        HikariConfig config = loadMysqlDatabaseConfig(
                "localhost",
                "minecraft",
                "root",
                "root"
        );

        SqlQueryLoader queryLoader = new SqlQueryLoader("/sql/mysql", "spawnerx_", new ConcurrentHashMap<>());

        return new DatabaseSQLManager(this.injector, config, queryLoader);
    }

    @Override
    public void startup() {
        this.databaseManager = loadDatabaseManager();
        try {
            this.databaseManager.init();
            this.injector.registerSingleton(SpawnerBlockRepository.class, this.spawnerBlockRepository =
                    new SpawnerBlockRepository(this.databaseManager.getSqlQueryLoader(), this.databaseManager.getDataSource()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void shutdown() {
        try {
            this.databaseManager.terminate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
