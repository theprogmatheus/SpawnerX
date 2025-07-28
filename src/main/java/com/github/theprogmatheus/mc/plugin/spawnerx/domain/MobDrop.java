package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
public abstract class MobDrop {

    private double chance = 1.0;
    private double positiveNoise = 0.15;
    private double negativeNoise = 0.25;
    private long delay = 0;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class MobDropItem extends MobDrop {
        private final ItemStack item;
        private final int min, max;
        private double looting;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class MobDropCommand extends MobDrop {
        private final List<String> commands;
    }

}
