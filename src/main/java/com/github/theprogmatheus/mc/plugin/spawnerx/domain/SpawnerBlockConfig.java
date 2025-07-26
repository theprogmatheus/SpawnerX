package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class SpawnerBlockConfig extends LinkedObject<String> {

    private final transient Plugin plugin;
    private final transient NamespacedKey namespacedKey;

    private final String id;
    private final EntityType entityType;

    private String entityName;
    private int delay = 20;
    private int minSpawnDelay = 40;
    private int maxSpawnDelay = 100;
    private int spawnCount = 4;
    private int spawnRange = 3;
    private int requiredPlayerRange = 4;
    private int maxNearbyEntities = 16;

    public SpawnerBlockConfig(@NotNull Plugin plugin, @NotNull String id, @NotNull EntityType entityType) {
        super(id.toLowerCase());
        this.plugin = plugin;
        this.namespacedKey = getNamespacedKey(plugin);
        this.id = id.toLowerCase();
        this.entityType = entityType;
        this.entityName = entityType.name();
    }


    public ItemStack createItemStack(int amount) {
        var itemStack = new ItemStack(Material.SPAWNER, amount);
        var itemMeta = itemStack.getItemMeta();
        var persistentDataContainer = itemMeta.getPersistentDataContainer();

        persistentDataContainer.set(this.namespacedKey, PersistentDataType.STRING, this.id);
        itemMeta.setDisplayName("§6Mob Spawner");
        itemMeta.setLore(List.of(
                "§7Type: §f" + this.entityName
        ));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean updateCreatureSpawner(@NotNull CreatureSpawner creatureSpawner) {
        creatureSpawner.setSpawnedType(this.entityType);

        creatureSpawner.setDelay(this.delay);
        creatureSpawner.setMinSpawnDelay(this.minSpawnDelay);
        creatureSpawner.setMaxSpawnDelay(this.maxSpawnDelay);
        creatureSpawner.setSpawnCount(this.spawnCount);
        creatureSpawner.setSpawnRange(this.spawnRange);
        creatureSpawner.setRequiredPlayerRange(this.requiredPlayerRange);
        creatureSpawner.setMaxNearbyEntities(this.maxNearbyEntities);

        return creatureSpawner.update();
    }

    public static void loadDefaults(@NotNull Plugin plugin) {
        Arrays.stream(EntityType.values())
                .filter(EntityType::isAlive)
                .forEach(entityType -> new SpawnerBlockConfig(plugin, entityType.name(), entityType).link());
    }

    public static String getSpawnerBlockConfigId(@NotNull Plugin plugin, @NotNull ItemStack itemStack) {
        var itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return null;
        return itemMeta.getPersistentDataContainer().get(getNamespacedKey(plugin), PersistentDataType.STRING);
    }

    public static NamespacedKey getNamespacedKey(@NotNull Plugin plugin) {
        return new NamespacedKey(plugin, "spawner_block_config");
    }

}
