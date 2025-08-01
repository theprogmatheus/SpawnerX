package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
public abstract class MobDrop {

    private final @NotNull String id;

    private double chance = 1.0;
    private double positiveNoise = 0.15;
    private double negativeNoise = 0.15;
    private long delay = 0;
    private long delayPerPlayer = 0;
    private int dropLimitByDelay = -1;


    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    public static class MobDropItem extends MobDrop {

        private final ItemStack item;
        private final int min, max;
        private double looting;

        public MobDropItem(@NotNull String id, ItemStack item, int min, int max) {
            super(id);
            this.item = item;
            this.min = min;
            this.max = max;
        }
    }

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    public static class MobDropCommand extends MobDrop {

        private final List<String> commands;

        public MobDropCommand(@NotNull String id, List<String> commands) {
            super(id);
            this.commands = commands;
        }
    }

}
