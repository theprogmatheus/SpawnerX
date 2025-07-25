package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.block.BlockBreakEvent;
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
    public boolean isBroken() {
        return !isValidBukkitSpawnerBlock(getBlock());
    }

    public void handleBlockBreak(BlockBreakEvent event) {
        this.unlink();

        var player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        var loc = event.getBlock().getLocation().add(0.5, 0.5, 0.5);
        var world = loc.getWorld();
        if (world == null)
            return;

        event.setDropItems(false);
        event.setExpToDrop(0);

        var item = this.config.createItemStack(1);
        loc.getWorld().dropItemNaturally(loc, item);
    }

    public static boolean isValidBukkitSpawnerBlock(@NotNull Block block) {
        return block.getState() instanceof CreatureSpawner;
    }

}
