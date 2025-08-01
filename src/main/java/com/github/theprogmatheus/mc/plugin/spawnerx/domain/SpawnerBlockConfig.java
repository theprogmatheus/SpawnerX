package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.config.env.Config;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class SpawnerBlockConfig extends LinkedObject<String> {

    private final transient Plugin plugin;
    private final transient NamespacedKey namespacedKey;

    private final String id;
    private final MobConfig mobConfig;
    private boolean hologram;
    private List<String> hologramLines;
    private String itemName;
    private List<String> itemLore;
    private boolean stackEnabled;
    private int stackMax;
    private int stackDistance;
    private boolean onlySilkTouchBreak;
    private int delay = 20;
    private int minSpawnDelay = 40;
    private int maxSpawnDelay = 100;
    private int spawnCount = 4;
    private int spawnRange = 3;
    private int requiredPlayerRange = 4;
    private int maxNearbyEntities = 16;

    public SpawnerBlockConfig(@NotNull Plugin plugin, @NotNull String id, @NotNull MobConfig mobConfig) {
        super(id.toLowerCase());
        this.plugin = plugin;
        this.namespacedKey = getNamespacedKey(plugin);
        this.id = id.toLowerCase();
        this.mobConfig = mobConfig;
    }


    public ItemStack createItemStack(int amount) {
        var itemStack = new ItemStack(Material.SPAWNER, amount);
        var itemMeta = itemStack.getItemMeta();
        var persistentDataContainer = itemMeta.getPersistentDataContainer();

        persistentDataContainer.set(this.namespacedKey, PersistentDataType.STRING, this.id);
        if (itemName != null)
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName)
                    .replace("%display_name%", this.mobConfig.getDisplayName()));

        if (itemLore != null) {
            List<String> lore = new ArrayList<>();
            itemLore.forEach(line -> {
                lore.add(ChatColor.translateAlternateColorCodes('&', line)
                        .replace("%display_name%", this.mobConfig.getDisplayName()));
            });
            itemMeta.setLore(lore);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void applyToSpawnerBlock(@NotNull SpawnerBlock spawnerBlock) {
        var block = spawnerBlock.getBlock();
        var blockState = block.getState();
        if (blockState instanceof CreatureSpawner creatureSpawner)
            updateCreatureSpawner(creatureSpawner);
    }

    private boolean updateCreatureSpawner(@NotNull CreatureSpawner creatureSpawner) {
        creatureSpawner.setSpawnedType(this.mobConfig.getEntityType());

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
                .forEach(entityType -> applyDefaultConfigs(LinkedObject.getLink(SpawnerBlockConfig.class, entityType.name().toLowerCase())
                        .orElseGet(() -> (SpawnerBlockConfig) new SpawnerBlockConfig(plugin, entityType.name(), MobConfig.getConfig(entityType)).link())));
    }

    private static void applyDefaultConfigs(@NotNull SpawnerBlockConfig config) {
        config.setItemName(Config.SPAWNERS_ITEM_NAME.getValue());
        config.setItemLore(Config.SPAWNERS_ITEM_LORE.getValue());
        config.setHologram(Config.SPAWNERS_HOLOGRAM.getValue());
        config.setHologramLines(Config.SPAWNERS_HOLOGRAM_FORMAT.getValue());
        config.setStackEnabled(Config.SPAWNERS_STACK_ENABLE.getValue());
        config.setStackMax(Config.SPAWNERS_STACK_MAX.getValue());
        config.setStackDistance(Config.SPAWNERS_STACK_DISTANCE.getValue());
        config.setOnlySilkTouchBreak(Config.SPAWNERS_ONLY_SILKTOUCH.getValue());
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

    public static Collection<SpawnerBlockConfig> getAvailableSpawnerConfigs() {
        return LinkedObject.getLinkerMap(SpawnerBlockConfig.class).orElse(Map.of()).values();
    }

}
