package com.norcode.bukkit.telewarp.commands;

import com.norcode.bukkit.telewarp.MetaKeys;
import com.norcode.bukkit.telewarp.Telewarp;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TPCommand extends BaseCommand {


    public TPCommand(Telewarp plugin) {
        super(plugin, "tp");
        minArgs = 1;
        allowConsole = true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args) {
        if (args.size() == 0) {
            return false;
        }
        Player playerToMove = null;
        Location destination = null;
        if (args.size() == 1 || args.size() == 3) {
            if (!(sender instanceof Player)) {
                return false;
            }
            playerToMove = ((Player) sender);
        } else if (args.size() == 2 || args.size() == 4) {
            String partial = args.pop();
            List<Player> moverMatches = plugin.getServer().matchPlayer(partial);
            if (moverMatches.size() != 1) {
                sender.sendMessage(plugin.getMsg("unknown-player", partial));
                return true;
            }
            playerToMove = moverMatches.get(0);
        } else {
            return false;
        }
        if (args.size() == 3) {
            destination = new Location(playerToMove.getWorld(),
                                    Double.parseDouble(args.get(0)),
                                    Double.parseDouble(args.get(1)),
                                    Double.parseDouble(args.get(2)));

        } else {
            List<Player> matches = plugin.getServer().matchPlayer(args.peek());
            if (matches.size() != 1) {
                sender.sendMessage(plugin.getMsg("unknown-player", args.peek()));
                return true;
            }
            destination = matches.get(0).getLocation().clone();
        }
        if (destination != null && playerToMove != null) {
            plugin.setPlayerMeta(playerToMove, MetaKeys.TELEPORT_TYPE, getName());
            playerToMove.teleport(destination, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        if (!playerToMove.getName().equals(sender.getName())) {
            sender.sendMessage(plugin.getMsg("3rd-party-tp-inititated"));

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
        List<String> results = new LinkedList<String>();
        if (args.size() <= 2) {
            List<Player> matches = plugin.getServer().matchPlayer(args.peekLast());
            for (Player p: matches) {
                if (p.getName().toLowerCase().startsWith(args.peekLast().toLowerCase())) {
                    results.add(p.getName());
                }
            }
        }
        return results;
    }
}
