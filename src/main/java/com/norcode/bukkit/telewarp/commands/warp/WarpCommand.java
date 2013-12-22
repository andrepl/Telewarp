package com.norcode.bukkit.telewarp.commands.warp;

import com.norcode.bukkit.telewarp.MetaKeys;
import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.commands.BaseCommand;
import com.norcode.bukkit.telewarp.persistence.warp.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WarpCommand extends BaseCommand {


	public WarpCommand(Telewarp plugin) {
		super(plugin, "warp");
		minArgs = 1;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args) {
		Player player = (Player) sender;
		if (args.size() == 0 || args.get(0).equals("list")) {
			if (args.size() >= 1) {
				args.pop();
			}
			return plugin.warpsCommand.onCommand(sender, plugin.getServer().getPluginCommand("warps"), "warp", args);
		}
		Warp warp = plugin.getWarpManager().getWarp(args.peek());
		if (warp == null) {
			player.sendMessage(plugin.getMsg("unknown-warp", args.peek()));
			return true;
		}
		plugin.setPlayerMeta(player, MetaKeys.TELEPORT_TYPE, this.getName());
		plugin.setPlayerMeta(player, MetaKeys.DESTINATION_WARP, warp.getName().toLowerCase());
		player.teleport(warp.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
		List<Warp> warps = plugin.getWarpManager().getWarpsFor(sender);
		List<String> results = new ArrayList<String>();
		for (Warp w : warps) {
			if (w.getName().toLowerCase().startsWith(args.peekLast().toLowerCase())) {
				results.add(w.getName());
			}
		}
		return results;
	}
}
