package com.github.theprogmatheus.mc.plugin.spawnerx.service;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.config.env.Config;
import com.github.theprogmatheus.mc.plugin.spawnerx.lang.MessageKey;
import com.github.theprogmatheus.mc.plugin.spawnerx.lang.MessageManager;
import com.github.theprogmatheus.mc.plugin.spawnerx.lib.PluginService;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.ArrayUtils;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LocaleUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
@Getter
public class MessageService extends PluginService {


    private final SpawnerX plugin;
    private final Logger logger;
    private MessageManager messageManager;


    @Override
    public void startup() {
        var langDefault = Config.LANG_DEFAULT.getValue();
        var langIndividual = Config.LANG_INDIVIDUAL.getValue();
        var defaultLocale = LocaleUtils.getLocaleByString(langDefault);

        this.messageManager = new MessageManager(
                this.logger,
                new File(plugin.getDataFolder(), "lang"),
                "lang",
                defaultLocale
        );
        this.messageManager.setIndividualLang(langIndividual);
        this.messageManager.loadLanguages();
    }

    public void sendMessage(CommandSender sender, MessageKey messageKey) {
        sendMessage(sender, messageKey, new String[0]);
    }

    public void sendMessage(CommandSender sender, MessageKey messageKey, String... arrayPlaceholders) {
        sendMessage(sender, messageKey, ArrayUtils.toMap(arrayPlaceholders));
    }

    public void sendMessage(CommandSender sender, MessageKey messageKey, Map<String, String> placeholders) {
        var messageFile = this.messageManager.getDefaultMessageFile();
        if (sender instanceof Player player)
            messageFile = this.messageManager.getMessageFile(player);

        List<String> message = null;
        if (messageKey.isList())
            message = messageFile.getMessageList(messageKey, placeholders);
        else {
            var rawMessage = messageFile.getMessage(messageKey, placeholders);
            if (rawMessage != null)
                message = List.of(rawMessage);
        }

        if (message != null)
            message.forEach(sender::sendMessage);
    }
}
