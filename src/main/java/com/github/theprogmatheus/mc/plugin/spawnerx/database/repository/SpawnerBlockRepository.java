package com.github.theprogmatheus.mc.plugin.spawnerx.database.repository;

import com.github.theprogmatheus.mc.plugin.spawnerx.database.SqlQueryLoader;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.entity.SpawnerBlockEntity;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
public class SpawnerBlockRepository {

    private final SqlQueryLoader sqlQueryLoader;
    private final HikariDataSource dataSource;

    public SpawnerBlockRepository(SqlQueryLoader sqlQueryLoader, HikariDataSource hikariDataSource) throws SQLException {
        this.sqlQueryLoader = sqlQueryLoader;
        this.dataSource = hikariDataSource;

        try (var connection = this.dataSource.getConnection();
             var statement = connection.createStatement()) {
            for (String createTable : this.sqlQueryLoader.getQueries("create_tables")) {
                statement.executeUpdate(createTable);
            }
        }
    }

    public void insertSpawner(SpawnerBlockEntity placeholder) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlQueryLoader.getQuery("insert_spawner"))) {

            ps.setString(1, placeholder.getWorld());
            ps.setInt(2, placeholder.getX());
            ps.setInt(3, placeholder.getY());
            ps.setInt(4, placeholder.getZ());
            ps.setInt(5, placeholder.getChunkX());
            ps.setInt(6, placeholder.getChunkZ());
            ps.setString(7, placeholder.getConfig());
            ps.executeUpdate();
        }
    }

    public List<SpawnerBlockEntity> getByChunk(String world, int chunkX, int chunkZ) {
        try {
            List<SpawnerBlockEntity> list = new ArrayList<>();

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlQueryLoader.getQuery("get_spawners_by_chunk"))) {

                ps.setString(1, world);
                ps.setInt(2, chunkX);
                ps.setInt(3, chunkZ);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapRow(rs));
                    }
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SpawnerBlockEntity getByLocation(String world, int x, int y, int z) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlQueryLoader.getQuery("get_spawner_by_location"))) {

            ps.setString(1, world);
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setInt(4, z);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public void deleteByLocation(String world, int x, int y, int z) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlQueryLoader.getQuery("delete_spawner_by_location"))) {

            ps.setString(1, world);
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setInt(4, z);
            ps.executeUpdate();
        }
    }

    private SpawnerBlockEntity mapRow(ResultSet rs) throws SQLException {
        return new SpawnerBlockEntity(
                rs.getString("world"),
                rs.getInt("x"),
                rs.getInt("y"),
                rs.getInt("z"),
                rs.getInt("chunk_x"),
                rs.getInt("chunk_z"),
                rs.getString("config")
        );
    }
}
