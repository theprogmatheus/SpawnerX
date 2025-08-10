package com.github.theprogmatheus.mc.plugin.spawnerx.database;

import com.github.theprogmatheus.mc.plugin.spawnerx.lib.Injector;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@Getter
@RequiredArgsConstructor
public class DatabaseSQLManager {

    private final Injector injector;
    private final HikariConfig databaseConfig;

    private HikariDataSource dataSource;

    public void openConnection() throws SQLException {
        this.dataSource = new HikariDataSource(this.databaseConfig);
    }

    public void closeConnection() throws Exception {
        this.dataSource.close();
    }

}
