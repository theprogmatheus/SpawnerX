package com.github.theprogmatheus.mc.plugin.spawnerx.command;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Name;
import co.aikar.commands.annotation.Subcommand;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlockConfig;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
@CommandAlias("spawnerx")
public class SpawnerXCommand extends AbstractCommand {

    @Subcommand("get")
    void getSpawner(Player player, @Name("spawnerConfig") SpawnerBlockConfig spawnerConfig, @Name("amount") int amount) {
        if (spawnerConfig == null) {
            player.sendMessage("§cConfig não encontrada.");
        } else {
            player.getInventory().addItem(spawnerConfig.createItemStack(amount));
            player.sendMessage("§aRecebido com sucesso.");
        }
    }

    @Override
    public void resolveContexts(CommandContexts<BukkitCommandExecutionContext> commandContexts) {

        commandContexts.registerContext(SpawnerBlockConfig.class, context ->
                LinkedObject.getLink(SpawnerBlockConfig.class, context.popFirstArg()).orElse(null));
    }
}
