package com.github.theprogmatheus.mc.plugin.spawnerx.command;

import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlockConfig;
import com.github.theprogmatheus.mc.plugin.spawnerx.service.SpawnerXService;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
@CommandAlias("spawnerx")
public class SpawnerXCommand extends AbstractCommand {

    private final SpawnerXService spawnerXService;


    @Override
    public AbstractCommand init(PaperCommandManager commandManager) {
        resolveCompletions(commandManager);
        resolveContexts(commandManager);
        return this;
    }

    @Subcommand("give")
    @CommandCompletion("@players @available_spawners")
    @CommandPermission("spawnerx.cmd.give")
    public void giveSpawner(
            CommandSender sender,
            @Name("target") String targetName,
            @Name("spawner") String spawnerKey,
            @Optional @Name("amount") Integer amount) {
        int finalAmount = (amount != null && amount > 0) ? amount : 1;

        var spawnerConfig = LinkedObject.getLink(SpawnerBlockConfig.class, spawnerKey.toLowerCase()).orElse(null);
        if (spawnerConfig == null) {
            sender.sendMessage("§cSpawner não encontrado.");
            return;
        }
        var target = Bukkit.getPlayer(targetName);

        if (target == null) {
            sender.sendMessage("§cJogador não encontrado.");
            return;
        }

        target.getInventory().addItem(spawnerConfig.createItemStack(finalAmount));
        sender.sendMessage("§aSpawner entregue com sucesso para " + target.getName() + "!");
    }


    @Subcommand("reload|rl")
    @CommandCompletion("@reload_modules")
    @CommandPermission("spawnerx.cmd.reload")
    public void onReloadCommand(CommandSender sender, @Optional @Name("module") String module) {
        if (module == null || module.isBlank()) {
            this.spawnerXService.reloadSpawnerBlocks();
            this.spawnerXService.reloadMobEntities();
            sender.sendMessage("§aConfigurações recarregados com sucesso.");
        } else if ("spawners".equalsIgnoreCase(module)) {
            this.spawnerXService.reloadSpawnerBlocks();
            sender.sendMessage("§aConfigurações de spawners recarregados com sucesso.");
        } else if ("mobs".equalsIgnoreCase(module)) {
            this.spawnerXService.reloadMobEntities();
            sender.sendMessage("§aConfigurações dos mobs recarregados com sucesso.");
        }
        sender.sendMessage("§c[!] O comando reload pode causar comportamentos inesperados no servidor, é recomendado reiniciar o servidor para aplicar as configurações da forma correta.");
        sender.sendMessage("§c[!] NUNCA use esse comando quando o servidor estiver em produção.");
    }


    private void resolveContexts(PaperCommandManager commandManager) {
        var commandContexts = commandManager.getCommandContexts();

        commandContexts.registerContext(SpawnerBlockConfig.class, context ->
                LinkedObject.getLink(SpawnerBlockConfig.class, context.popFirstArg().toLowerCase()).orElse(null));
    }


    private void resolveCompletions(PaperCommandManager commandManager) {
        var completions = commandManager.getCommandCompletions();
        completions.registerAsyncCompletion("available_spawners",
                context -> SpawnerBlockConfig.getAvailableSpawnerConfigs()
                        .stream()
                        .map(SpawnerBlockConfig::getId)
                        .toList());

        completions.registerAsyncCompletion("reload_modules", context -> List.of("spawners", "mobs"));
    }


}
