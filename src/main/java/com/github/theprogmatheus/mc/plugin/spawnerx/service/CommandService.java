package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import co.aikar.commands.Locales;
import co.aikar.commands.PaperCommandManager;
import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.command.AbstractCommand;
import com.github.theprogmatheus.mc.plugin.spawnerx.command.SpawnerXCommand;
import com.github.theprogmatheus.mc.plugin.spawnerx.config.env.Config;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.Injector;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.PluginService;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LocaleUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Locale;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
@Getter
public class CommandService extends PluginService {

    private final SpawnerX plugin;
    private final Injector injector;
    private PaperCommandManager commandManager;

    /**
     * Register your commands here
     */
    private void registerAllCommands() {
        registerCommand(SpawnerXCommand.class);
    }

    private void unregisterAllCommands() {
        this.commandManager.unregisterCommands();
    }


    @Override
    public void startup() {
        this.commandManager = new PaperCommandManager(this.plugin);
        this.commandManager.usePerIssuerLocale(Config.LANG_INDIVIDUAL.getValue(), false);
        this.commandManager.getLocales().setDefaultLocale(getDefaultLocale());
        registerAllCommands();
    }

    private Locale getDefaultLocale() {
        var defaultLocale = LocaleUtils.getLocaleByString(Config.LANG_DEFAULT.getValue());
        if (defaultLocale == null)
            defaultLocale = Locales.ENGLISH;
        return defaultLocale;
    }

    @Override
    public void shutdown() {
        unregisterAllCommands();
    }

    private void registerCommand(Class<? extends AbstractCommand> commandClass) {
        this.commandManager.registerCommand(this.injector.getInstance(commandClass).init(this.commandManager));
    }
}
