package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
@Setter
public class SpawnerBlock extends LinkedObject<BlockLocationKey> {

    private transient Long dbId;
    private final transient SpawnerBlockConfig config;

    public SpawnerBlock(@NotNull Block block, @NotNull SpawnerBlockConfig config) {
        super(BlockLocationKey.fromBukkitLocation(block.getLocation()));
        this.config = config;
    }

    public SpawnerBlock(@NotNull BlockLocationKey blockLocationKey, @NotNull SpawnerBlockConfig config) {
        super(blockLocationKey);
        this.config = config;
    }

    public Block getBlock() {
        return getOriginal().getBlock();
    }

    @Override
    public LinkedObject<BlockLocationKey> link() {
        if (!super.hasLinked()) {
            super.link();

            if (getBlock().getState() instanceof CreatureSpawner creatureSpawner)
                this.config.updateCreatureSpawner(creatureSpawner);
        }
        return this;
    }

    public Optional<MobEntity> findNearbyEntityOfType(@NotNull Location loc, double radius) {
        return findNearbyEntitiesOfType(loc, radius).findAny();
    }

    public Stream<MobEntity> findNearbyEntitiesOfType(@NotNull Location loc, double radius) {
        return loc.getWorld()
                .getNearbyEntities(loc, radius, radius, radius)
                .stream()
                .filter(entity -> entity instanceof LivingEntity && entity.getType().equals(this.config.getEntityType()))
                .map(entity -> MobEntity.fromEntity((LivingEntity) entity))
                .flatMap(Optional::stream);
    }

    @Override
    public boolean isOk() {
        return isValidBukkitSpawnerBlock(getBlock());
    }

    @Override
    public boolean isBroken() {
        return !isOk();
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
