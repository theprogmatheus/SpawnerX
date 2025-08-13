package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import com.github.theprogmatheus.mc.plugin.spawnerx.lib.Injector;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.PluginService;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class MainService extends PluginService {

    private final Injector injector;
    private final List<PluginService> services = new ArrayList<>();


    /**
     * Register your services here
     */
    public void setupServices() {
        addService(ConfigurationService.class, 10, 2);
        addService(MessageService.class, 9, 3);
        addService(DatabaseSQLService.class, 8, 4);
        addService(SpawnerXService.class, 8, 10);
        addService(PlayerProfileService.class, 7, 9);
        addService(APIService.class, 6, 8);
        addService(CommandService.class, 5, 7);
        addService(ListenerService.class, 4, 6);
        addService(UpdateCheckerService.class, 3, 5);
    }

    @Override
    public void startup() {
        setupServices();
        startupServices();
    }

    @Override
    public void shutdown() {
        shutdownServices();
    }

    private void startupServices() {
        orderedServices(PluginService::getStartupPriority)
                .forEach(PluginService::startup);
    }

    private void shutdownServices() {
        orderedServices(PluginService::getShutdownPriority)
                .forEach(PluginService::shutdown);
    }


    private List<PluginService> orderedServices(ToIntFunction<? super PluginService> keyExtractor) {
        var services = new ArrayList<>(this.services);
        services.sort(Comparator.comparingInt(keyExtractor)
                .reversed());
        return services;
    }

    private void addService(Class<? extends PluginService> serviceClass) {
        addService(serviceClass, 1, 1);
    }

    private void addService(Class<? extends PluginService> serviceClass, int startupPriority, int shutdownPriority) {
        var service = this.injector.getInstance(serviceClass);

        service.setStartupPriority(startupPriority);
        service.setShutdownPriority(shutdownPriority);

        this.services.add(service);
    }


}
