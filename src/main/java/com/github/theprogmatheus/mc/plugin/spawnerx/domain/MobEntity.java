package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MobEntity extends LinkedObject<UUID> {

    public MobEntity(@NotNull Entity entity) {
        super(entity.getUniqueId());
    }

    public Entity getEntity() {
        return Bukkit.getEntity(getOriginal());
    }
}
