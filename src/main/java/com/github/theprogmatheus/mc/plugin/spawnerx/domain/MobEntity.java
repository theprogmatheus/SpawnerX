package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.ExecutorTimeLogger;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Setter
@Getter
public class MobEntity extends LinkedObject<UUID> {

    private static final Gson gson = new Gson();
    private static final String stackedAmountDisplayFormat = "§fx%s";
    private static final NamespacedKey dataNamespacedKey = new NamespacedKey("spawnerx", "mob_entity_data");
    private static final NamespacedKey mobEntityRefNamespacedKey = new NamespacedKey("spawnerx", "mob_entity_ref");

    private transient LivingEntity entity;

    private int stackedAmount;
    private BlockLocationKey spawner;

    MobEntity(@NotNull LivingEntity entity) {
        super(entity.getUniqueId());
        this.entity = entity;
        this.stackedAmount = 1;
    }

    public LivingEntity getEntity() {
        var entity = (LivingEntity) Bukkit.getEntity(getOriginal());
        if (entity == null)
            return this.entity;
        return this.entity = entity;
    }

    public void setEntity(@NotNull LivingEntity entity) {
        if (entity.getUniqueId().equals(getOriginal()))
            this.entity = entity;
    }

    public MobEntity setup() {
        return ((MobEntity) super.link())
                .setupDisplayName()
                .persist();
    }

    private MobEntity setupDisplayName() {
        var entity = getEntity();
        entity.setCustomNameVisible(true);
        return updateDisplayName(entity);
    }

    public MobEntity updateDisplayName() {
        return updateDisplayName(getEntity());
    }

    private MobEntity updateDisplayName(@NotNull LivingEntity entity) {
        entity.setCustomName(stackedAmountDisplayFormat.formatted(this.stackedAmount));
        return this;
    }

    @Override
    public boolean isBroken() {
        return Bukkit.getEntity(getOriginal()) == null;
    }

    public void stack() {
        this.stackedAmount++;
        this.updateDisplayName();
        this.persist();
    }

    public void unstack() {
        unstack(1);
    }

    public void unstack(int amount) {
        this.stackedAmount -= amount;
        if (this.stackedAmount > 0)
            this.updateDisplayName();
        this.persist();
    }

    private LivingEntity spawnFakeEntity() {
        var loc = getEntity().getLocation();
        var fakeEntity = (LivingEntity) loc.getWorld().spawnEntity(loc, getEntity().getType());
        fakeEntity.getPersistentDataContainer()
                .set(mobEntityRefNamespacedKey, PersistentDataType.STRING, getOriginal().toString());
        return fakeEntity;
    }

    public void simulateDeath(@NotNull LivingEntity killer, int amount) {
        this.unstack(amount);
        if (this.stackedAmount > 0) {
            var entity = getEntity();
            entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

            var fakeEntity = spawnFakeEntity();
            fakeEntity.damage(fakeEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), killer);
        }

        this.processMobDrops(killer, this.stackedAmount > 0 ? amount : this.stackedAmount + amount);
    }

    public SpawnerBlock getSpawnerBlock() {
        if (this.spawner != null)
            return LinkedObject.getLink(SpawnerBlock.class, this.spawner).orElse(null);
        return null;
    }

    public void processMobDrops(@NotNull LivingEntity killer, int amount) {
        var spawner = getSpawnerBlock();
        if (spawner != null)
            MobDropper.dropAll(this, spawner.getConfig().getMobConfig(), killer, amount);
    }

    public MobEntity persist() {
        getEntity().getPersistentDataContainer()
                .set(dataNamespacedKey, PersistentDataType.STRING, gson.toJson(this));
        return this;
    }

    public static Optional<MobEntity> fromEntity(@NotNull LivingEntity entity) {
        return getLink(MobEntity.class, entity.getUniqueId());
    }

    public static Optional<MobEntity> fromFakeEntity(@NotNull LivingEntity fakeEntity) {
        var uuidString = fakeEntity.getPersistentDataContainer().get(mobEntityRefNamespacedKey, PersistentDataType.STRING);
        if (uuidString == null || uuidString.isBlank())
            return Optional.empty();

        try {
            return getLink(MobEntity.class, UUID.fromString(uuidString));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    public static boolean hasMobEntityData(@NotNull LivingEntity entity) {
        return entity.getPersistentDataContainer().has(dataNamespacedKey, PersistentDataType.STRING);
    }

    public static MobEntity newMobEntity(@NotNull LivingEntity entity) {
        var dataContainer = entity.getPersistentDataContainer();

        var serializedJsonData = dataContainer.get(dataNamespacedKey, PersistentDataType.STRING);
        if (serializedJsonData == null)
            return new MobEntity(entity).setup();

        var deserializedData = gson.fromJson(serializedJsonData, MobEntity.class);
        var mobEntity = new MobEntity(entity);
        mobEntity.stackedAmount = deserializedData.stackedAmount;

        return mobEntity.setup();
    }

    public static void loadAllPersisted() {
        ExecutorTimeLogger.executeAndLogTime(SpawnerX.getInjector().getInstance(Logger.class),
                "Load MobEntities", () -> Bukkit.getWorlds()
                        .stream()
                        .map(World::getEntities)
                        .flatMap(List::stream)
                        .filter(entity -> (entity instanceof LivingEntity livingEntity) && hasMobEntityData(livingEntity))
                        .forEach(entity -> newMobEntity((LivingEntity) entity)));
    }


    public static Optional<MobEntity> findNearby(@NotNull Location loc, double radius) {
        return loc.getWorld().getNearbyEntities(loc, radius, radius, radius)
                .stream()
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> fromEntity((LivingEntity) entity))
                .flatMap(Optional::stream)
                .findAny();
    }
}
