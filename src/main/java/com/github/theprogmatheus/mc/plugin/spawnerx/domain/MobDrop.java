package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
public abstract class MobDrop {

    private double chance = 100.0;
    private long delay = 0;


    public void handleDrop() {

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public class MobDropItem extends MobDrop {

        private final ItemStack item;
        private final int min, max;
        private boolean looting;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public class MobDropCommand extends MobDrop {

        private final List<String> commands;

    }

}
