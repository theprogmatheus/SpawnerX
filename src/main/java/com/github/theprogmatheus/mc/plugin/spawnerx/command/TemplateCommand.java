package com.github.theprogmatheus.mc.plugin.spawnerx.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.lang.MessageKey;
import com.github.theprogmatheus.mc.plugin.spawnerx.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
@CommandAlias("template")
public class TemplateCommand extends BaseCommand {

    private final SpawnerX plugin;
    private final MessageService messageService;

    @Default
    void onDefault(Player player) {
        messageService.sendMessage(player, MessageKey.COMMAND_TEMPLATE,
                "plugin", plugin.getName()
        );
    }

}
