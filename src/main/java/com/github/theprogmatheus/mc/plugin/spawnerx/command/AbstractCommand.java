package com.github.theprogmatheus.mc.plugin.spawnerx.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;

public abstract class AbstractCommand extends BaseCommand {


    public abstract AbstractCommand init(PaperCommandManager commandManager);

}
