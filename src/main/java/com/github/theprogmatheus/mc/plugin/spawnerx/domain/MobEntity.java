package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.ExecutorTimeLogger;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import com.google.gson.Gson;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class MobEntity extends LinkedObject<UUID> {

    private static final Gson gson = new Gson();
    private static final String stackedAmountDisplayFormat = "§fx%s";
    private static final NamespacedKey dataNamespacedKey = new NamespacedKey("spawnerx", "mob_entity_data");
    private static final NamespacedKey mobEntityRefNamespacedKey = new NamespacedKey("spawnerx", "mob_entity_ref");

    private transient LivingEntity entity;
    private final transient MobConfig config;

    private int stackedAmount;
    private BlockLocationKey spawner;

    MobEntity(@NotNull LivingEntity entity, @NotNull MobConfig config) {
        super(entity.getUniqueId());
        this.entity = entity;
        this.config = config;
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
        if (this.stackedAmount >= getConfig().getStackMax())
            return;
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

    public int calculateUnstackAmount(@NotNull LivingEntity killer) {
        if (getConfig().isShiftKillOne() && killer instanceof Player player && player.isSneaking())
            return 1;

        if (getConfig().isKillAll())
            return this.stackedAmount;

        var calculatedAmount = countNearbySpawners(getEntity().getLocation()) * getConfig().getKillUnstackAmountPerSpawner();
        if (calculatedAmount <= 0)
            calculatedAmount = 1;

        return Math.min(calculatedAmount, this.stackedAmount);
    }

    private int countNearbySpawners(@NotNull Location location) {
        return (int) SpawnerBlock.findNearbySpawners(location, 10)
                .stream()
                .filter(spawnerBlock -> getConfig().equals(spawnerBlock.getConfig().getMobConfig()))
                .count();
    }

    public void simulateDeath(@NotNull LivingEntity killer) {
        this.simulateDeath(killer, calculateUnstackAmount(killer));
    }

    public void simulateDeath(@NotNull LivingEntity killer, int amount) {
        if (amount <= 0)
            return;

        amount = Math.min(amount, this.stackedAmount);

        this.unstack(amount);
        if (this.stackedAmount > 0) {
            var entity = getEntity();
            entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

            var fakeEntity = spawnFakeEntity();
            fakeEntity.damage(fakeEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), killer);
        }
        MobDropper.dropAll(this, this.config, killer, amount);
    }

    public SpawnerBlock getSpawnerBlock() {
        if (this.spawner != null)
            return LinkedObject.getLink(SpawnerBlock.class, this.spawner).orElse(null);
        return null;
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
        var config = MobConfig.getConfig(entity.getType());
        if (config == null)
            throw new IllegalArgumentException("A loaded config was not found for this entity type: %s".formatted(entity.getType()));

        // setup config
        entity.setAI(config.isAi());

        var dataContainer = entity.getPersistentDataContainer();

        var serializedJsonData = dataContainer.get(dataNamespacedKey, PersistentDataType.STRING);
        if (serializedJsonData == null)
            return new MobEntity(entity, config).setup();

        var deserializedData = gson.fromJson(serializedJsonData, MobEntity.class);
        var mobEntity = new MobEntity(entity, config);
        mobEntity.stackedAmount = deserializedData.stackedAmount;
        mobEntity.spawner = deserializedData.spawner;

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
