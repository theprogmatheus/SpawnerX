package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.config.env.Config;
import com.github.theprogmatheus.mc.plugin.spawnerx.kdtree.KDNode;
import com.github.theprogmatheus.mc.plugin.spawnerx.kdtree.KDTree;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.InventoryUtils;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Getter
@Setter
public class SpawnerBlock extends LinkedObject<BlockLocationKey> {

    private static final Map<String, KDTree<SpawnerBlock>> kdTrees = new ConcurrentHashMap<>();

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

            addToKdTree();
        }
        return this;
    }

    @Override
    public void unlink() {
        if (this.hasLinked()) {
            super.unlink();
            removeFromKdTree();
        }
    }

    private void addToKdTree() {
        var loc = getOriginal().toBukkitLocation();
        var world = loc.getWorld();
        if (world != null)
            getKdTree(world).insert(new KDNode<>(loc, this, null, null));
    }


    private void removeFromKdTree() {
        var loc = getOriginal().toBukkitLocation();
        var world = loc.getWorld();
        if (world != null)
            getKdTree(world).remove(loc);
    }

    public Optional<MobEntity> findNearbyEntityOfType(@NotNull Location loc, double radius) {
        return findNearbyEntitiesOfType(loc, radius).findAny();
    }

    public Stream<MobEntity> findNearbyEntitiesOfType(@NotNull Location loc, double radius) {
        return loc.getWorld()
                .getNearbyEntities(loc, radius, radius, radius)
                .stream()
                .filter(entity -> entity instanceof LivingEntity && entity.getType().equals(this.config.getMobConfig().getEntityType()))
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

        var inventory = player.getInventory();
        var itemInMainHand = inventory.getItemInMainHand();
        if (Config.SPAWNERS_ONLY_SILKTOUCH.getValue() && (!itemInMainHand.containsEnchantment(Enchantment.SILK_TOUCH)))
            return;

        var loc = event.getBlock().getLocation().add(0.5, 0.5, 0.5);
        var world = loc.getWorld();
        if (world == null)
            return;

        event.setDropItems(false);
        event.setExpToDrop(0);

        var item = this.config.createItemStack(1);

        var availableSpace = InventoryUtils.getAvailableSpaceForItem(inventory.getContents(), item);
        if (availableSpace > 0)
            inventory.addItem(item);
        else
            loc.getWorld().dropItemNaturally(loc, item);
    }


    public static boolean isValidBukkitSpawnerBlock(@NotNull Block block) {
        return block.getState() instanceof CreatureSpawner;
    }

    public static KDTree<SpawnerBlock> getKdTree(@NotNull World world) {
        return kdTrees.computeIfAbsent(world.getName(), worldName -> new KDTree<>());
    }

    public static List<SpawnerBlock> findNearbySpawners(@NotNull Location location, int radius) {
        if (radius <= 0)
            return Collections.emptyList();

        var world = location.getWorld();
        if (world == null)
            return Collections.emptyList();

        return getKdTree(location.getWorld())
                .rangeSearch(location, radius)
                .stream()
                .map(KDNode::getValue)
                .toList();
    }

}
