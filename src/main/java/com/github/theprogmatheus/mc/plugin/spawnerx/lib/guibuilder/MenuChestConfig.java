package com.github.theprogmatheus.mc.plugin.spawnerx.lib.guibuilder;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryType;

@Getter
public class MenuChestConfig extends MenuConfig {

    private final int rows;

    public MenuChestConfig(String title, int rows) {
        super(title, InventoryType.CHEST);
        this.rows = rows;
    }
}
