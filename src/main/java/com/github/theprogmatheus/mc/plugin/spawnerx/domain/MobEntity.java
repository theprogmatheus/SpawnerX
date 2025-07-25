package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class MobEntity extends LinkedObject<UUID> {

    private static final String DISPLAY_STACKED_AMOUNT_FORMAT = "§fx%s";

    private int stackedAmount;

    public MobEntity(@NotNull Entity entity) {
        super(entity.getUniqueId());
        this.stackedAmount = 1;
    }

    public MobEntity setup() {
        this.link();
        return this;
    }


    public void updateDisplayStackedAmount() {
        if (!isBroken()) {
            var entity = getEntity();
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
}
