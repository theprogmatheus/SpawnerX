package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import lombok.Data;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Data
public class MobConfig {

    private final @NotNull EntityType entityType;
    private final @NotNull String displayName;

    private String stackDisplayFormat = "§fx%s";
    private int stackMax = Integer.MAX_VALUE;
    private int killUnstackAmountPerSpawner = 1;
    private boolean ai = true;
    private boolean knockBack = true;
    private boolean hitKill = false;
    private int dropExp = -1;
    private Collection<MobDrop> drops;

}
