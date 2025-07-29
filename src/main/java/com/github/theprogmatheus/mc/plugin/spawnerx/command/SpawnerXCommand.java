package com.github.theprogmatheus.mc.plugin.spawnerx.command;

import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlockConfig;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Singleton;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
@CommandAlias("spawnerx")
public class SpawnerXCommand extends AbstractCommand {


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
    }


}
