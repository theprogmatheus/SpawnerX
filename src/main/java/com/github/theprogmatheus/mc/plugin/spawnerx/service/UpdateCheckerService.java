package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.PluginService;
import com.github.theprogmatheus.util.JGRUChecker;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.util.logging.Logger;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UpdateCheckerService extends PluginService {

    private final SpawnerX plugin;
    private final Logger logger;
    private JGRUChecker updateChecker;

    @Override
    public void startup() {
        checkNewUpdates();
    }

    private void checkNewUpdates() {
        if (this.updateChecker == null)
            this.updateChecker = new JGRUChecker("theprogmatheus", this.plugin.getName(), this.plugin.getDescription().getVersion());

        this.updateChecker.checkAsync().thenAcceptAsync(release -> {
            if (release == null || this.updateChecker.getCurrentVersion().equals(release.getVersion()))
                return;

            Bukkit.getScheduler().runTask(this.plugin, () -> {
                Logger log = this.logger;

                // ANSI escape code
                String ANSI_RESET = "\u001B[0m";
                String ANSI_YELLOW = "\u001B[93m";
                String ANSI_CYAN = "\u001B[36m";
                String ANSI_WHITE = "\u001B[37m";
                String ANSI_GOLD = "\u001B[33m";


                log.info(" ");
                log.info(ANSI_YELLOW + "===============================================" + ANSI_RESET);
                log.info(ANSI_GOLD + "[!] A new update is available! [!]" + ANSI_RESET);
                log.info(ANSI_YELLOW + "===============================================" + ANSI_RESET);
                log.info(ANSI_CYAN + " * Plugin: " + ANSI_WHITE + release.getName() + ANSI_RESET);
                log.info(ANSI_CYAN + " * Current Version: " + ANSI_WHITE + this.updateChecker.getCurrentVersion() + ANSI_RESET);
                log.info(ANSI_CYAN + " * New Version: " + ANSI_WHITE + release.getVersion() + ANSI_RESET);
                log.info(ANSI_CYAN + " * Download here: " + ANSI_WHITE + release.getDownloadPage() + ANSI_RESET);
                log.info(ANSI_YELLOW + "===============================================" + ANSI_RESET);
                log.info(ANSI_GOLD + "[!] Upgrade now to enjoy the latest features, improvements, and bug fixes!" + ANSI_RESET);
                log.info(ANSI_GOLD + "[!] Staying updated ensures better performance and security." + ANSI_RESET);
                log.info(ANSI_YELLOW + "===============================================" + ANSI_RESET);
                log.info(" ");
            });
        });
    }

}
