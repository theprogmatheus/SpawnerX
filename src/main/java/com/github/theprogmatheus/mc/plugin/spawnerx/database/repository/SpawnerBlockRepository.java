package com.github.theprogmatheus.mc.plugin.spawnerx.database.repository;

import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlock;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpawnerBlockRepository {

    private final HikariDataSource dataSource;

    public SpawnerBlockRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private String getSQL(String path) {
        throw new UnsupportedOperationException("Necessário implementar isso aqui ainda.");
    }

    public void insert(SpawnerBlock spawner) throws SQLException {

        Block block = spawner.getBlock();
        Location loc = block.getLocation();
        Chunk chunk = block.getChunk();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getSQL("sql/insert/spawner_block.sql"))) {

            stmt.setString(1, loc.getWorld().getName());
            stmt.setInt(2, loc.getBlockX());
            stmt.setInt(3, loc.getBlockY());
            stmt.setInt(4, loc.getBlockZ());
            stmt.setInt(5, chunk.getX());
            stmt.setInt(6, chunk.getZ());
            stmt.setString(7, spawner.getConfig().getId());
            stmt.setInt(8, spawner.getStackedAmount());

            stmt.executeUpdate();
        }
    }

    public List<SpawnerBlock> findByChunk(String world, int chunkX, int chunkZ) throws SQLException {
        List<SpawnerBlock> spawners = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getSQL("sql/select/spawner_block_by_chunk.sql"))) {

            stmt.setString(1, world);
            stmt.setInt(2, chunkX);
            stmt.setInt(3, chunkZ);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    spawners.add(mapRow(rs));
                }
            }
        }
        return spawners;
    }

    public Optional<SpawnerBlock> findByLocation(String world, int x, int y, int z) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getSQL("sql/select/spawner_block_by_location.sql"))) {

            stmt.setString(1, world);
            stmt.setInt(2, x);
            stmt.setInt(3, y);
            stmt.setInt(4, z);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public void deleteByLocation(String world, int x, int y, int z) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getSQL("sql/delete/spawner_block_by_location.sql"))) {

            stmt.setString(1, world);
            stmt.setInt(2, x);
            stmt.setInt(3, y);
            stmt.setInt(4, z);

            stmt.executeUpdate();
        }
    }

    private SpawnerBlock mapRow(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Ainda preciso implementar o mapRow()");
    }
}
