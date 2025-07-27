package com.github.theprogmatheus.mc.plugin.spawnerx.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlockLocationKey {

    private String world;
    private int x, y, z;


    public Block getBlock() {
        return toBukkitLocation().getBlock();
    }

    public Location toBukkitLocation() {
        var world = Bukkit.getWorld(this.world);
        if (world == null)
            throw new RuntimeException("The world \"%s\" no longer exists on the server".formatted(this.world));

        return new Location(world, x, y, z);
    }

    public static BlockLocationKey fromBukkitLocation(Location location) {
        if (location == null)
            throw new IllegalArgumentException("bukkit location cannot be null to create a new BlockLocationKey");

        var world = location.getWorld();
        if (world == null)
            throw new IllegalArgumentException("The bukkit location world cannot be null to create a new BlockLocationKey");

        return new BlockLocationKey(world.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
