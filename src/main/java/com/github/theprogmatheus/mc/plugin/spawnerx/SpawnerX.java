package com.github.theprogmatheus.mc.plugin.spawnerx;

import com.github.theprogmatheus.mc.plugin.spawnerx.lib.Injector;
import com.github.theprogmatheus.mc.plugin.spawnerx.service.MainService;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
public class SpawnerX extends JavaPlugin {

    @Getter
    private static Injector injector;
    private MainService mainService;

    @Override
    public void onLoad() {
        injector = new Injector();
    }

    @Override
    public void onEnable() {
        this.registerInjectorSingletons();
        this.mainService = injector.getInstance(MainService.class);
        this.mainService.startup();
    }

    @Override
    public void onDisable() {
        this.mainService.shutdown();
    }

    private void registerInjectorSingletons() {
        injector.registerSingleton(new Class[]{JavaPlugin.class, Plugin.class, SpawnerX.class}, this);
        injector.registerSingleton(Logger.class, getLogger());
    }
}
