package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class MobEntity extends LinkedObject<UUID> {

    private static final String DISPLAY_STACKED_AMOUNT_FORMAT = "§fx%s";

    private int stackedAmount;

    MobEntity(@NotNull Entity entity) {
        super(entity.getUniqueId());
        this.stackedAmount = 1;
    }

    public MobEntity setup(Entity entity) {
        this.link();

        entity.setCustomNameVisible(true);
        entity.setCustomName(DISPLAY_STACKED_AMOUNT_FORMAT.formatted(this.stackedAmount));
        if (entity instanceof LivingEntity livingEntity)
            livingEntity.setAI(false);

        return this;
    }


    public void updateDisplayStackedAmount() {
        var entity = getEntity();
        if (entity != null) {
            entity.setCustomNameVisible(true);
            entity.setCustomName(DISPLAY_STACKED_AMOUNT_FORMAT.formatted(this.stackedAmount));
        }
    }

    public Entity getEntity() {
        return Bukkit.getEntity(getOriginal());
    }

    @Override
    public boolean isBroken() {
        return Bukkit.getEntity(getOriginal()) == null;
    }

    public void stack() {
        this.stackedAmount++;
        this.updateDisplayStackedAmount();
    }

    public static boolean handleMobDeath(LivingEntity killer, LivingEntity entity) {

        var maxHealthAttr = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttr != null)
            entity.setHealth(maxHealthAttr.getValue());

        return true;
    }

    public static MobEntity newMobEntity(@NotNull Entity entity) {
        return new MobEntity(entity).setup(entity);
    }
}
