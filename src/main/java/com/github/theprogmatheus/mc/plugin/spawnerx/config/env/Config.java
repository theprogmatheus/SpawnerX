package com.github.theprogmatheus.mc.plugin.spawnerx.config.env;

import com.github.theprogmatheus.mc.plugin.spawnerx.config.Configuration;
import com.github.theprogmatheus.mc.plugin.spawnerx.config.ConfigurationHolder;

/**
 * Here your set all config values from auto map
 */

@Configuration("config.yml")
public class Config {


    public static final ConfigurationHolder<String> CONFIG_VERSION = new ConfigurationHolder<>("config-version", String.class);

    public static final ConfigurationHolder<String> LANG_DEFAULT = new ConfigurationHolder<>("lang.default", String.class);
    public static final ConfigurationHolder<Boolean> LANG_INDIVIDUAL = new ConfigurationHolder<>("lang.individual", Boolean.class);

}
