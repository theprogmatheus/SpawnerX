package com.github.theprogmatheus.mc.plugin.spawnerx.config.env;

import com.github.theprogmatheus.mc.plugin.spawnerx.config.Configuration;
import com.github.theprogmatheus.mc.plugin.spawnerx.config.ConfigurationHolder;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

/**
 * Here your set all config values from auto map
 */

@Configuration("config.yml")
public class Config {


    public static final ConfigurationHolder<String> CONFIG_VERSION =
            new ConfigurationHolder<>("config-version", String.class);

    public static final ConfigurationHolder<String> LANG_DEFAULT =
            new ConfigurationHolder<>("lang.default", String.class);
    public static final ConfigurationHolder<Boolean> LANG_INDIVIDUAL =
            new ConfigurationHolder<>("lang.individual", Boolean.class);

    public static final ConfigurationHolder<String> SPAWNERS_ITEM_NAME =
            new ConfigurationHolder<>("spawners.item.name", String.class);
    public static final ConfigurationHolder<List<String>> SPAWNERS_ITEM_LORE =
            new ConfigurationHolder<>("spawners.item.lore", (Class<List<String>>) (Class<?>) List.class);
    public static final ConfigurationHolder<Boolean> SPAWNERS_HOLOGRAM =
            new ConfigurationHolder<>("spawners.hologram", Boolean.class);
    public static final ConfigurationHolder<List<String>> SPAWNERS_HOLOGRAM_FORMAT =
            new ConfigurationHolder<>("spawners.hologram_format", (Class<List<String>>) (Class<?>) List.class);
    public static final ConfigurationHolder<Boolean> SPAWNERS_STACK_ENABLE =
            new ConfigurationHolder<>("spawners.stack.enable", Boolean.class);
    public static final ConfigurationHolder<Integer> SPAWNERS_STACK_MAX =
            new ConfigurationHolder<>("spawners.stack.max", Integer.class);
    public static final ConfigurationHolder<Integer> SPAWNERS_STACK_DISTANCE =
            new ConfigurationHolder<>("spawners.stack.distance", Integer.class);
    public static final ConfigurationHolder<Boolean> SPAWNERS_ONLY_SILKTOUCH =
            new ConfigurationHolder<>("spawners.only_silktouch", Boolean.class);
    public static final ConfigurationHolder<Integer> SPAWNERS_SETTINGS_DELAY =
            new ConfigurationHolder<>("spawners.settings.delay", Integer.class);
    public static final ConfigurationHolder<Integer> SPAWNERS_SETTINGS_MIN_SPAWN_DELAY =
            new ConfigurationHolder<>("spawners.settings.min_spawn_delay", Integer.class);
    public static final ConfigurationHolder<Integer> SPAWNERS_SETTINGS_MAX_SPAWN_DELAY =
            new ConfigurationHolder<>("spawners.settings.max_spawn_delay", Integer.class);
    public static final ConfigurationHolder<Integer> SPAWNERS_SETTINGS_SPAWN_COUNT =
            new ConfigurationHolder<>("spawners.settings.spawn_count", Integer.class);
    public static final ConfigurationHolder<Integer> SPAWNERS_SETTINGS_SPAWN_RANGE =
            new ConfigurationHolder<>("spawners.settings.spawn_range", Integer.class);
    public static final ConfigurationHolder<Integer> SPAWNERS_SETTINGS_REQUIRED_PLAYER_RANGE =
            new ConfigurationHolder<>("spawners.settings.required_player_range", Integer.class);
    public static final ConfigurationHolder<Integer> SPAWNERS_SETTINGS_MAX_NEARBY_ENTITIES =
            new ConfigurationHolder<>("spawners.settings.max_nearby_entities", Integer.class);

    public static final ConfigurationHolder<String> MOBS_FORMAT =
            new ConfigurationHolder<>("mobs.format", String.class);
    public static final ConfigurationHolder<Integer> MOBS_STACK_MAX =
            new ConfigurationHolder<>("mobs.stack_max", Integer.class);
    public static final ConfigurationHolder<Integer> MOBS_KILL_STACK_PER_SPAWNER =
            new ConfigurationHolder<>("mobs.kill_stack_per_spawner", Integer.class);
    public static final ConfigurationHolder<Boolean> MOBS_AI_MOB =
            new ConfigurationHolder<>("mobs.ai_mob", Boolean.class);
    public static final ConfigurationHolder<Boolean> MOBS_KNOCKBACK_MOB =
            new ConfigurationHolder<>("mobs.knockback_mob", Boolean.class);
    public static final ConfigurationHolder<Boolean> MOBS_HITKILL_MOB =
            new ConfigurationHolder<>("mobs.hitkill_mob", Boolean.class);
    public static final ConfigurationHolder<Boolean> MOBS_KILL_ALL =
            new ConfigurationHolder<>("mobs.kill_all", Boolean.class);
    public static final ConfigurationHolder<Boolean> MOBS_SHIFT_KILL_ONE =
            new ConfigurationHolder<>("mobs.shift_kill_one", Boolean.class);
    public static final ConfigurationHolder<Boolean> MOBS_PERFORMANCE_MOB_STACK_KILL =
            new ConfigurationHolder<>("mobs.performance_mob.stack_kill", Boolean.class);
    public static final ConfigurationHolder<Integer> MOBS_PERFORMANCE_MOB_CHUNK =
            new ConfigurationHolder<>("mobs.performance_mob.chunk", Integer.class);
    public static final ConfigurationHolder<ConfigurationSection> MOBS_PER_MOB =
            new ConfigurationHolder<>("mobs.per_mob", ConfigurationSection.class);
}
