package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.jetbrains.annotations.NotNull;

public class SpawnerBlock extends LinkedObject<BlockLocationKey> {

    public SpawnerBlock(@NotNull Block block) {
        super(BlockLocationKey.fromBukkitLocation(block.getLocation()));
    }

    public Block getBlock() {
        return getOriginal().getBlock();
    }

    public void setup() {

    }

    public static boolean isValidBukkitSpawnerBlock(@NotNull Block block) {
        return block.getState() instanceof CreatureSpawner;
    }

}
