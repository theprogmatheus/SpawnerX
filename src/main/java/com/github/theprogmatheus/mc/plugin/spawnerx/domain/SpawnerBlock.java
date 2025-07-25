package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.jetbrains.annotations.NotNull;

@Getter
public class SpawnerBlock extends LinkedObject<BlockLocationKey> {

    private final transient SpawnerBlockConfig config;

    public SpawnerBlock(@NotNull Block block, @NotNull SpawnerBlockConfig config) {
        super(BlockLocationKey.fromBukkitLocation(block.getLocation()));
        this.config = config;
    }

    public Block getBlock() {
        return getOriginal().getBlock();
    }

    @Override
    public void link() {
        super.link();

        if (getBlock().getState() instanceof CreatureSpawner creatureSpawner)
            this.config.updateCreatureSpawner(creatureSpawner);
    }

    @Override
    public void unlink() {
        super.unlink();
    }

    @Override
    public boolean isBroken() {
        return !isValidBukkitSpawnerBlock(getBlock());
    }

    public static boolean isValidBukkitSpawnerBlock(@NotNull Block block) {
        return block.getState() instanceof CreatureSpawner;
    }

}
