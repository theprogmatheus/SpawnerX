package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.theprogmatheus.mc.plugin.spawnerx.config.env.Config.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class MobConfig extends LinkedObject<String> {

    private final @NotNull EntityType entityType;

    private @NotNull String displayName;
    private @NotNull String stackDisplayFormat = "§6x%stack_mob% &f%display_name%";
    private int stackMax = Integer.MAX_VALUE;
    private int killUnstackAmountPerSpawner = 1;
    private boolean ai = true;
    private boolean knockBack = true;
    private boolean hitKill = false;
    private boolean killAll = false;
    private boolean shiftKillOne = false;
    private int dropExp = -1;
    private Collection<MobDrop> drops;

    public MobConfig(@NotNull String id, @NotNull EntityType entityType) {
        super(id);
        this.entityType = entityType;
        this.displayName = StringUtils.prettifyEntityName(entityType.name());
    }

    private void setDisplayNameIfValid(String displayName) {
        if (displayName != null) this.displayName = displayName;
    }

    private void setStackDisplayFormatIfValid(String format) {
        if (format != null) this.stackDisplayFormat = ChatColor.translateAlternateColorCodes('&', format);
    }

    private void setStackMaxIfPositive(int value) {
        if (value > 0) this.stackMax = value;
    }

    private void setKillUnstackAmountIfPositive(int value) {
        if (value > 0) this.killUnstackAmountPerSpawner = value;
    }

    private void setAiIfPresent(Boolean value) {
        if (value != null) this.ai = value;
    }

    private void setKnockBackIfPresent(Boolean value) {
        if (value != null) this.knockBack = value;
    }

    private void setHitKillIfPresent(Boolean value) {
        if (value != null) this.hitKill = value;
    }

    private void setKillAllIfPresent(Boolean value) {
        if (value != null) this.killAll = value;
    }

    private void setShiftKillOneIfPresent(Boolean value) {
        if (value != null) this.shiftKillOne = value;
    }


    public static MobConfig getConfig(@NotNull EntityType entityType) {
        return LinkedObject.getLink(MobConfig.class, entityType.name()).orElse(null);
    }

    public static void loadDefaults(@NotNull Plugin plugin) {
        Arrays.stream(EntityType.values())
                .filter(EntityType::isAlive)
                .forEach(entityType -> {
                    var config = LinkedObject.getLink(MobConfig.class, entityType.name()).orElse(new MobConfig(entityType.name(), entityType));
                    if (!config.hasLinked())
                        config.link();
                    applyDefaultConfig(config);
                });

        applyYamlConfigs(plugin.getLogger());
    }

    private static MobConfig applyDefaultConfig(@NotNull MobConfig config) {
        config.setStackDisplayFormatIfValid(MOBS_FORMAT.getValue());
        config.setStackMaxIfPositive(MOBS_STACK_MAX.getValue());
        config.setKillUnstackAmountIfPositive(MOBS_KILL_STACK_PER_SPAWNER.getValue());

        config.setAiIfPresent(MOBS_AI_MOB.getValue());
        config.setKnockBackIfPresent(MOBS_KNOCKBACK_MOB.getValue());
        config.setHitKillIfPresent(MOBS_HITKILL_MOB.getValue());
        config.setKillAllIfPresent(MOBS_KILL_ALL.getValue());
        config.setShiftKillOneIfPresent(MOBS_SHIFT_KILL_ONE.getValue());

        return config;
    }

    private static void applySpecificConfig(@NotNull ConfigurationSection yaml, @NotNull MobConfig config) {
        config.setDisplayNameIfValid(yaml.getString("display_name"));
        config.setStackDisplayFormatIfValid(yaml.getString("format"));
        config.setStackMaxIfPositive(yaml.getInt("stack_max"));
        config.setKillUnstackAmountIfPositive(yaml.getInt("kill_stack_per_spawner"));

        int exp = yaml.getInt("exp");
        if (exp > 0) config.setDropExp(exp);

        config.setAiIfPresent(yaml.contains("ai_mob") ? yaml.getBoolean("ai_mob") : null);
        config.setKnockBackIfPresent(yaml.contains("knockback_mob") ? yaml.getBoolean("knockback_mob") : null);
        config.setHitKillIfPresent(yaml.contains("hitkill_mob") ? yaml.getBoolean("hitkill_mob") : null);
        config.setKillAllIfPresent(yaml.contains("kill_all") ? yaml.getBoolean("kill_all") : null);
        config.setShiftKillOneIfPresent(yaml.contains("shift_kill_one") ? yaml.getBoolean("shift_kill_one") : null);
    }


    private static void applyYamlConfigs(@NotNull Logger log) {
        var perMobSection = MOBS_PER_MOB.getValue();
        if (perMobSection == null) return;

        for (String key : perMobSection.getKeys(false)) {
            try {
                var entityType = EntityType.valueOf(key.toUpperCase());
                var config = MobConfig.getConfig(entityType);

                if (config == null)
                    throw new IllegalArgumentException("Custom settings cannot be made for this entity.");

                var yaml = perMobSection.getConfigurationSection(key);
                if (yaml == null) continue;

                applySpecificConfig(yaml, config);
                applyDropsConfig(yaml, config);
            } catch (Exception e) {
                log.warning("Could not load custom settings for mob: " + key);
                log.log(Level.FINE, "Exception details:", e);
            }
        }
    }

    private static void applyDropsConfig(@NotNull ConfigurationSection yaml, @NotNull MobConfig config) {
        var allDropsSection = yaml.getConfigurationSection("drops");
        if (allDropsSection == null) return;

        List<MobDrop> drops = new ArrayList<>();

        for (String dropKey : allDropsSection.getKeys(false)) {
            var dropSection = allDropsSection.getConfigurationSection(dropKey);
            if (dropSection == null) continue;

            try {
                MobDrop drop = parseDropSection(dropSection);
                if (drop != null) drops.add(drop);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        config.setDrops(drops);
    }

    private static MobDrop parseDropSection(ConfigurationSection section) {

        var dropType = section.getString("drop_type");
        var materialName = section.getString("material");
        var data = section.getInt("data");
        var durability = section.contains("durability") ? section.getInt("durability") : -1;
        var chance = section.getDouble("chance") / 100.0D;
        var delay = section.getLong("delay");
        var min = section.getInt("min");
        var max = section.getInt("max");
        var positiveNoise = section.getDouble("positive_noise") / 100.0D;
        var negativeNoise = section.getDouble("negative_noise") / 100.0D;
        var looting = section.getDouble("looting") / 100.0D;
        var commands = section.getStringList("commands");

        MobDrop drop = null;
        if ("ITEM".equalsIgnoreCase(dropType)) {
            var material = Material.valueOf(materialName.toUpperCase());
            ItemStack item = resolveItem(material, durability);
            var itemDrop = new MobDrop.MobDropItem(item, min, max);
            itemDrop.setLooting(looting);
            drop = itemDrop;
        } else if ("COMMAND".equalsIgnoreCase(dropType))
            drop = new MobDrop.MobDropCommand(commands);


        if (drop != null) {
            drop.setChance(chance);
            drop.setDelay(delay);
            drop.setPositiveNoise(positiveNoise);
            drop.setNegativeNoise(negativeNoise);
        }

        return drop;
    }

    private static ItemStack resolveItem(Material material, int durability) {
        ItemStack item = new ItemStack(material, 1);
        if (durability == -1)
            return item;

        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable damageable) {
            damageable.setDamage(durability);
            item.setItemMeta(meta);
        }
        return item;
    }

    public MobConfig applyToEntity(@NotNull LivingEntity entity) {
        entity.setAI(this.ai);
        return this;
    }

}

