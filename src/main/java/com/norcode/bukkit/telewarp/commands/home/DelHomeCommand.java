package com.norcode.bukkit.telewarp.commands.home;

import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.commands.BaseCommand;
import com.norcode.bukkit.telewarp.persistence.home.Home;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: andre
 * Date: 6/5/13
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class DelHomeCommand extends BaseCommand {
	public DelHomeCommand(Telewarp plugin) {
		super(plugin, null);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args) {
		Home targetHome = null;
		Map<String, Home> homes = plugin.getHomeManager().getHomesFor(sender.getName());
		if (homes == null || homes.isEmpty()) {
			sender.sendMessage(plugin.getMsg("no-home-location"));
			return true;
		}
		if (args.size() == 0) {
			targetHome = homes.get("home");
		} else {
			List<String> matches = new ArrayList<String>();
			for (String n : homes.keySet()) {
				if (n.startsWith(args.peek().toLowerCase())) {
					matches.add(n);
				}
			}
			if (matches.size() == 1) {
				targetHome = homes.get(matches.get(0));
			} else {
				sender.sendMessage(plugin.getMsg("home-not-found", args.peek()));
			}
		}
		plugin.getHomeManager().delHome(targetHome);
		sender.sendMessage(plugin.getMsg("home-deleted", targetHome));
		return true;
	}


	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
		Map<String, Home> homes = plugin.getHomeManager().getHomesFor(sender.getName());
		List<String> results = new LinkedList<String>();
		for (Home h : homes.values()) {
			if (h.getName().toLowerCase().startsWith(args.peekLast().toLowerCase())) {
				results.add(h.getName());
			}
		}
		return results;
	}
}
