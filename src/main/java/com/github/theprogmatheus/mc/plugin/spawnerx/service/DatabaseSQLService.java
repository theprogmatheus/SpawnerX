package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.config.env.Config;
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
import java.util.logging.Logger;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class DatabaseSQLService extends PluginService {


    private final SpawnerX plugin;
    private final Injector injector;
    private final Logger log;

    private DatabaseSQLManager databaseManager;

    private DatabaseSQLManager loadDatabaseManager() {
        var inTestMode = Boolean.parseBoolean(System.getProperty("BUKKIT_PLUGIN_DB_IN_TEST_MODE", "false"));
        if (inTestMode) {
            SqlQueryLoader queryLoader = new SqlQueryLoader("/sql/sqlite", "spawnerx_", new ConcurrentHashMap<>());
            return new DatabaseSQLManager(this.injector, loadTestDatabaseConfig(), queryLoader);
        }
        return loadManager();
    }

    private HikariConfig loadMysqlDatabaseConfig() {
        var config = new HikariConfig();

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(3);

        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://%s/%s".formatted(Config.DATABASE_HOST.getValue(), Config.DATABASE_DBNAME.getValue()));
        config.setUsername(Config.DATABASE_USER.getValue());
        config.setPassword(Config.DATABASE_PASS.getValue());

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return config;
    }

    private HikariConfig loadMariadbDatabaseConfig() {
        var config = new HikariConfig();

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(3);

        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setJdbcUrl("jdbc:mariadb://%s/%s".formatted(Config.DATABASE_HOST.getValue(), Config.DATABASE_DBNAME.getValue()));
        config.setUsername(Config.DATABASE_USER.getValue());
        config.setPassword(Config.DATABASE_PASS.getValue());

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return config;
    }


    private HikariConfig loadSqliteDatabaseConfig() {
        var storageFile = new File(this.plugin.getDataFolder(), "storage.sqlite.db");
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

    private DatabaseSQLManager loadManager() {
        HikariConfig config;
        SqlQueryLoader queryLoader;

        String dbType = Config.DATABASE_TYPE.getValue();

        switch (dbType != null ? dbType.toLowerCase() : "sqlite") {
            case "mysql":
                config = loadMysqlDatabaseConfig();
                queryLoader = new SqlQueryLoader("/sql/mysql", Config.DATABASE_PREFIX.getValue(), new ConcurrentHashMap<>());
                break;

            case "mariadb":
                config = loadMariadbDatabaseConfig();
                queryLoader = new SqlQueryLoader("/sql/mysql", Config.DATABASE_PREFIX.getValue(), new ConcurrentHashMap<>());
                break;

            case "sqlite":
            default:
                config = loadSqliteDatabaseConfig();
                queryLoader = new SqlQueryLoader("/sql/sqlite", Config.DATABASE_PREFIX.getValue(), new ConcurrentHashMap<>());
                if (!"sqlite".equalsIgnoreCase(dbType))
                    log.warning("Unknown database type '%s', using SQLite as fallback.".formatted(dbType));
                break;
        }
        return new DatabaseSQLManager(this.injector, config, queryLoader);
    }

    @Override
    public void startup() {
        this.databaseManager = loadDatabaseManager();
        try {
            this.databaseManager.init();
            this.injector.registerSingleton(SpawnerBlockRepository.class,
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
