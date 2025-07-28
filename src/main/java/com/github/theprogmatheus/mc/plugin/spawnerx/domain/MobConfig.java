package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import lombok.Data;
import org.bukkit.entity.EntityType;

import java.util.Collection;

@Data
public class MobConfig {

    private final EntityType entityType;
    private final String displayName;
    private String stackDisplayFormat = "§fx%s";
    private int stackMax = Integer.MAX_VALUE;
    private int killUnstackAmountPerSpawner = 1;
    private boolean ai = true;
    private boolean knockBack = true;
    private boolean hitKill = false;
    private Collection<MobDrop> drops;

}
