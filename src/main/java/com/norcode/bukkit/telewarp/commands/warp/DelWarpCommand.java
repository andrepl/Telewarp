package com.norcode.bukkit.telewarp.commands.warp;

import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.commands.BaseCommand;
import com.norcode.bukkit.telewarp.persistence.warp.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: andre
 * Date: 6/2/13
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DelWarpCommand extends BaseCommand {

	public DelWarpCommand(Telewarp plugin) {
		super(plugin, null);
		minArgs = 1;
		allowConsole = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args) {

		Warp warp = plugin.getWarpManager().getWarp(args.peek());
		if (warp == null) {
			sender.sendMessage(plugin.getMsg("unknown-warp", args.peek()));
			return true;
		}

		plugin.getWarpManager().deleteWarp(warp);
		sender.sendMessage(plugin.getMsg("warp-deleted", warp.getName()));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
		List<String> results = new ArrayList<String>();
		for (Warp w : plugin.getWarpManager().getWarpsFor(sender)) {
			if (w.getName().toLowerCase().startsWith(args.peekLast().toLowerCase())) {
				results.add(w.getName());
			}
		}
		return results;
	}
}
