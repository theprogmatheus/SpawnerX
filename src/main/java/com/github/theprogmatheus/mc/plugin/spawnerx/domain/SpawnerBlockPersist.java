package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.database.SqlQueryLoader;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.entity.SpawnerBlockEntity;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.repository.SpawnerBlockRepository;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Singleton
public class SpawnerBlockPersist {

    public static final int FLUSH_INTERVAL = 60;
    public static final int MAP_LIMIT = 500;

    private final AtomicReference<Map<BlockLocationKey, Optional<SpawnerBlockEntity>>> mapRef;
    private final SpawnerBlockRepository repository;
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean flushing;

    @Inject
    public SpawnerBlockPersist(SpawnerBlockRepository repository) {
        this.repository = repository;
        this.mapRef = new AtomicReference<>(new ConcurrentHashMap<>());
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.flushing = new AtomicBoolean(false);
        this.scheduler.scheduleWithFixedDelay(this::flush, FLUSH_INTERVAL, FLUSH_INTERVAL, TimeUnit.SECONDS);
    }

    private SpawnerBlockEntity fromSpawnerBlock(@NotNull SpawnerBlock spawner) {
        return new SpawnerBlockEntity(
                spawner.getOriginal().getWorld(),
                spawner.getOriginal().getX(),
                spawner.getOriginal().getY(),
                spawner.getOriginal().getZ(),
                spawner.getOriginal().getChunkCoord().getX(),
                spawner.getOriginal().getChunkCoord().getZ(),
                spawner.getConfig().getId()
        );
    }

    public void put(@NotNull SpawnerBlock spawner) {
        this.mapRef.get().put(spawner.getOriginal(), Optional.of(fromSpawnerBlock(spawner)));
        this.checkFlush();
    }

    public void delete(@NotNull SpawnerBlock spawner) {
        this.mapRef.get().put(spawner.getOriginal(), Optional.empty());
        this.checkFlush();
    }

    private void checkFlush() {
        if (this.mapRef.get().size() >= MAP_LIMIT)
            this.scheduler.execute(this::flush);
    }

    private void flush() {
        if (this.mapRef.get().isEmpty())
            return;

        if (!this.flushing.compareAndSet(false, true))
            return;

        var mapToFlush = this.mapRef.getAndSet(new ConcurrentHashMap<>());

        SqlQueryLoader queryLoader = this.repository.getSqlQueryLoader();

        String insertQuery = queryLoader.getQuery("insert_spawner");
        String deleteQuery = queryLoader.getQuery("delete_spawner_by_location");

        try (Connection connection = this.repository.getDataSource().getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement insertPs = connection.prepareStatement(insertQuery);
                 PreparedStatement deletePs = connection.prepareStatement(deleteQuery)) {

                int upserts = 0;
                int deletes = 0;

                for (var entry : mapToFlush.entrySet()) {
                    var location = entry.getKey();
                    var spawnerBlock = entry.getValue().orElse(null);

                    if (spawnerBlock != null) {

                        insertPs.setString(1, spawnerBlock.getWorld());
                        insertPs.setInt(2, spawnerBlock.getX());
                        insertPs.setInt(3, spawnerBlock.getY());
                        insertPs.setInt(4, spawnerBlock.getZ());
                        insertPs.setInt(5, spawnerBlock.getChunkX());
                        insertPs.setInt(6, spawnerBlock.getChunkZ());
                        insertPs.setString(7, spawnerBlock.getConfig());
                        insertPs.addBatch();

                        upserts++;
                    } else {
                        deletePs.setString(1, location.getWorld());
                        deletePs.setInt(2, location.getX());
                        deletePs.setInt(3, location.getY());
                        deletePs.setInt(4, location.getZ());
                        deletePs.addBatch();

                        deletes++;
                    }
                }
                if (upserts > 0)
                    insertPs.executeBatch();

                if (deletes > 0)
                    deletePs.executeBatch();

                connection.commit();
            } catch (SQLException exception) {
                this.mapRef.updateAndGet(current -> {
                    mapToFlush.forEach(current::putIfAbsent);
                    return current;
                });
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        } finally {
            this.flushing.set(false);
        }
    }

    public void shutdown() {
        try {
            this.scheduler.shutdown();
            if (!this.scheduler.awaitTermination(30, TimeUnit.SECONDS))
                this.scheduler.shutdownNow();
        } catch (InterruptedException ignored) {
            this.scheduler.shutdownNow();
        }
        flush();
    }
}