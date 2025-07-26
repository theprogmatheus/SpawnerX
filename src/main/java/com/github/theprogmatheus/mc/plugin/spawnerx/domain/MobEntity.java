package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@Getter
public class MobEntity extends LinkedObject<UUID> {

    private static final String stackedAmountDisplayFormat = "§fx%s";
    private static final NamespacedKey mobEntityRefNamespacedKey = new NamespacedKey("spawnerx", "mob_entity_ref");

    private final transient LivingEntity entity;
    private int stackedAmount;

    MobEntity(@NotNull LivingEntity entity) {
        super(entity.getUniqueId());
        this.entity = entity;
        this.stackedAmount = 1;
    }

    public MobEntity setup() {
        this.link();

        entity.setCustomNameVisible(true);
        //entity.setAI(false);

        updateDisplayStackedAmount();
        return this;
    }

    public void updateDisplayStackedAmount() {
        entity.setCustomName(stackedAmountDisplayFormat.formatted(this.stackedAmount));
    }

    @Override
    public boolean isBroken() {
        return Bukkit.getEntity(getOriginal()) == null;
    }

    public void stack() {
        this.stackedAmount++;
        this.updateDisplayStackedAmount();
    }

    public void unstack() {
        unstack(1);
    }

    public void unstack(int amount) {
        this.stackedAmount -= amount;
        if (this.stackedAmount > 0)
            this.updateDisplayStackedAmount();
    }

    private LivingEntity spawnFakeEntity() {
        var loc = entity.getLocation();
        var fakeEntity = (LivingEntity) loc.getWorld().spawnEntity(loc, entity.getType());
        fakeEntity.getPersistentDataContainer()
                .set(mobEntityRefNamespacedKey, PersistentDataType.STRING, getOriginal().toString());
        return fakeEntity;
    }

    public void simulateDeath(LivingEntity killer, int amount) {
        this.unstack(amount);
        if (this.stackedAmount > 0) {
            this.entity.setHealth(this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

            var fakeEntity = spawnFakeEntity();
            fakeEntity.damage(fakeEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), killer);
        }
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

    public static MobEntity newMobEntity(@NotNull LivingEntity entity) {
        return new MobEntity(entity).setup();
    }
}
