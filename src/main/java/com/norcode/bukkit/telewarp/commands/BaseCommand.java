package com.norcode.bukkit.telewarp.commands;

import com.norcode.bukkit.telewarp.Telewarp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseCommand implements TabExecutor {
	protected boolean allowConsole = false;
	protected int minArgs = 0;
	protected Telewarp plugin;
	protected String name;

	public BaseCommand(Telewarp plugin, String name) {
		this.plugin = plugin;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!allowConsole && !(sender instanceof Player)) {
			sender.sendMessage("This command cannot be run from the console.");
			return true;
		}
		if (args.length < minArgs) {
			return false;
		}
		LinkedList<String> params = new LinkedList<String>();
		params.addAll(Arrays.asList(args));
		return onCommand(sender, command, label, params);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		LinkedList<String> params = new LinkedList<String>();
		params.addAll(Arrays.asList(args));
		return onTabComplete(sender, command, alias, params);
	}


	public abstract boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args);

	public abstract List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args);
}
