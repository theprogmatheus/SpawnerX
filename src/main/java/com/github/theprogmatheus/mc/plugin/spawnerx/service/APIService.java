package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.api.APIProvider;
import com.github.theprogmatheus.mc.plugin.spawnerx.api.impl.APIProviderImpl;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.PluginService;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class APIService extends PluginService {


    private final SpawnerX plugin;
    private ServicesManager servicesManager;

    /**
     * Register your APIs here
     */
    private void registerAllAPIs() {
        registerAPI(APIProvider.class, new APIProviderImpl());
    }

    private void unregisterAllAPIs() {
        this.servicesManager.unregisterAll(this.plugin);
    }

    @Override
    public void startup() {
        this.servicesManager = this.plugin.getServer().getServicesManager();
        registerAllAPIs();
    }

    @Override
    public void shutdown() {
        unregisterAllAPIs();
    }

    private <T> void registerAPI(Class<T> apiClass, T provider) {
        this.servicesManager.register(apiClass, provider, this.plugin, ServicePriority.Normal);
    }

    public <T> T getAPI(Class<T> apiClass) {
        var registration = this.servicesManager.getRegistration(apiClass);
        if (registration != null)
            return registration.getProvider();
        return null;
    }

}
