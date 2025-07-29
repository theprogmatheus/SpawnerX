package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class MobConfig extends LinkedObject<String> {

    private final @NotNull EntityType entityType;
    private final @NotNull String displayName;

    public MobConfig(@NotNull String id, @NotNull EntityType entityType, @NotNull String displayName) {
        super(id.toLowerCase());
        this.entityType = entityType;
        this.displayName = displayName;
    }

    private String stackDisplayFormat = "§fx%s";
    private int stackMax = Integer.MAX_VALUE;
    private int killUnstackAmountPerSpawner = 1;
    private boolean ai = true;
    private boolean knockBack = true;
    private boolean hitKill = false;
    private int dropExp = -1;
    private Collection<MobDrop> drops;

}
