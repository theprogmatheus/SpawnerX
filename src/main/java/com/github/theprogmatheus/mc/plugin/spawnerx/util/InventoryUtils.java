package com.github.theprogmatheus.mc.plugin.spawnerx.util;

import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public static int getAvailableSpaceForItem(ItemStack[] contents, ItemStack incoming) {
        int totalSpace = 0;
        int maxStackSize = incoming.getMaxStackSize();
        for (ItemStack current : contents) {
            if (current == null || current.getType().isAir()) {
                totalSpace += maxStackSize;
            } else if (current.isSimilar(incoming)) {
                int remaining = maxStackSize - current.getAmount();
                if (remaining > 0) {
                    totalSpace += remaining;
                }
            }
        }
        return totalSpace;
    }

}
