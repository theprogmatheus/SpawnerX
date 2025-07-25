package com.github.theprogmatheus.mc.plugin.spawnerx.domain;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

@RequiredArgsConstructor
@Data
public class BlockLocationKey {

    private final String world;
    private final int x, y, z;

    public static BlockLocationKey fromBukkitLocation(Location location) {
        if (location == null)
            throw new RuntimeException("bukkit location cannot be null to create a new BlockLocationKey");

        var world = location.getWorld();
        if (world == null)
            throw new RuntimeException("The bukkit location world cannot be null to create a new BlockLocationKey");

        return new BlockLocationKey(world.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
