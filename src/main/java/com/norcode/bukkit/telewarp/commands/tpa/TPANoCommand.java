package com.norcode.bukkit.telewarp.commands.tpa;

import com.norcode.bukkit.telewarp.MetaKeys;
import com.norcode.bukkit.telewarp.TPARequest;
import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.commands.BaseCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class TPANoCommand extends BaseCommand {
	public TPANoCommand(Telewarp plugin) {
		super(plugin, null);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("this command cannot be run from the console.");
			return true;
		}
		Player player = (Player) sender;
		if (!player.hasMetadata(MetaKeys.TPA_REQUEST)) {
			((Player) sender).sendMessage(plugin.getMsg("no-tpa-request"));
			return true;
		}
		TPARequest req = (TPARequest) player.getMetadata(MetaKeys.TPA_REQUEST).get(0).value();
		Player playerToMove = plugin.getServer().getPlayer(req.getPlayerToMove());
		if (playerToMove == null) {
			sender.sendMessage(plugin.getMsg("player-not-online", req.getPlayerToMove()));
			return true;
		}
		playerToMove.sendMessage(plugin.getMsg("tpa-request-denied", sender.getName()));
		player.removeMetadata(MetaKeys.TPA_REQUEST, plugin);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
