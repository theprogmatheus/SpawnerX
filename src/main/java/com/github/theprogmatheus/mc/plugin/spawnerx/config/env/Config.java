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

    public static final ConfigurationHolder<Boolean> SPAWNERS_HOLOGRAM =
            new ConfigurationHolder<>("spawners.hologram", Boolean.class);
    public static final ConfigurationHolder<List<String>> SPAWNERS_FORMAT =
            new ConfigurationHolder<>("spawners.format", (Class<List<String>>) (Class<?>) List.class);
    public static final ConfigurationHolder<Boolean> SPAWNERS_STACK_ENABLE =
            new ConfigurationHolder<>("spawners.stack.enable", Boolean.class);
    public static final ConfigurationHolder<Integer> SPAWNERS_STACK_MAX =
            new ConfigurationHolder<>("spawners.stack.max", Integer.class);
    public static final ConfigurationHolder<Integer> SPAWNERS_STACK_DISTANCE =
            new ConfigurationHolder<>("spawners.stack.distance", Integer.class);
    public static final ConfigurationHolder<Boolean> SPAWNERS_ONLY_SILKTOUCH =
            new ConfigurationHolder<>("spawners.only_silktouch", Boolean.class);

    public static final ConfigurationHolder<String> MOBS_FORMAT =
            new ConfigurationHolder<>("mobs.format", String.class);
    public static final ConfigurationHolder<Integer> MOBS_STACK_MAX =
            new ConfigurationHolder<>("mobs.stack_max", Integer.class);
    public static final ConfigurationHolder<Integer> MOBS_KILL_STACK_PER_SPAWNER =
            new ConfigurationHolder<>("mobs.kill_stack_per_spawner", Integer.class);
    public static final ConfigurationHolder<Double> MOBS_TEMP_SPAWN =
            new ConfigurationHolder<>("mobs.temp_spawn", Double.class);
    public static final ConfigurationHolder<Boolean> MOBS_MORE_SPAWNERS_TEMP_REDUCTION =
            new ConfigurationHolder<>("mobs.more_spawners_temp_reduction", Boolean.class);
    public static final ConfigurationHolder<Double> MOBS_TEMP_REDUCTION =
            new ConfigurationHolder<>("mobs.temp_reduction", Double.class);
    public static final ConfigurationHolder<Double> MOBS_MAX_REDUCTION =
            new ConfigurationHolder<>("mobs.max_reduction", Double.class);
    public static final ConfigurationHolder<Boolean> MOBS_AI_MOB =
            new ConfigurationHolder<>("mobs.ai_mob", Boolean.class);
    public static final ConfigurationHolder<Boolean> MOBS_KNOCKBACK_MOB =
            new ConfigurationHolder<>("mobs.knockback_mob", Boolean.class);
    public static final ConfigurationHolder<Boolean> MOBS_HITKILL_MOB =
            new ConfigurationHolder<>("mobs.hitkill_mob", Boolean.class);
    public static final ConfigurationHolder<Boolean> MOBS_SHIFT_KILL_ONE =
            new ConfigurationHolder<>("mobs.shift_kill_one", Boolean.class);
    public static final ConfigurationHolder<Boolean> MOBS_PERFORMANCE_MOB_STACK_KILL =
            new ConfigurationHolder<>("mobs.performance_mob.stack_kill", Boolean.class);
    public static final ConfigurationHolder<Integer> MOBS_PERFORMANCE_MOB_CHUNK =
            new ConfigurationHolder<>("mobs.performance_mob.chunk", Integer.class);
    public static final ConfigurationHolder<ConfigurationSection> MOBS_PER_MOB =
            new ConfigurationHolder<>("mobs.per_mob", ConfigurationSection.class);
}
