package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class MobConfig extends LinkedObject<String> {

    private final @NotNull EntityType entityType;

    private @NotNull String displayName;
    private @NotNull String stackDisplayFormat = "§fx%s";
    private int stackMax = Integer.MAX_VALUE;
    private int killUnstackAmountPerSpawner = 1;
    private boolean ai = true;
    private boolean knockBack = true;
    private boolean hitKill = false;
    private int dropExp = -1;
    private Collection<MobDrop> drops;


    public MobConfig(@NotNull String id, @NotNull EntityType entityType) {
        super(id);
        this.entityType = entityType;
        this.displayName = entityType.name();
    }

    public static MobConfig getConfig(@NotNull EntityType entityType) {
        return LinkedObject.getLink(MobConfig.class, entityType.name()).orElse(null);
    }

    public static void loadDefaults() {
        Arrays.stream(EntityType.values())
                .filter(EntityType::isAlive)
                .forEach(entityType -> new MobConfig(entityType.name(), entityType).link());
    }
}
