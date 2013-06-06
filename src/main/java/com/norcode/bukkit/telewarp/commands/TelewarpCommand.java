package com.norcode.bukkit.telewarp.commands;

import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.commands.BaseCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: andre
 * Date: 6/2/13
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class TelewarpCommand extends BaseCommand {

    public TelewarpCommand(Telewarp plugin) {
        super(plugin, null);
        minArgs = 1;
        allowConsole = true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args) {
        if (args.peek().toLowerCase().equals("reload")) {
            plugin.reloadAll();
            sender.sendMessage(plugin.getMsg("config-reloaded"));
            return true;
        } else if (args.peek().toLowerCase().equals("save")) {
            plugin.saveConfig();
            plugin.getWarpManager().saveAll();
            sender.sendMessage(plugin.getMsg("config-saved"));
            return true;
        } else if (args.peek().toLowerCase().equals("resetcooldown")) {
            args.pop();
            Player targetPlayer = null;
            if (args.size() == 0 && sender instanceof Player) {
                targetPlayer = (Player) sender;
            } else {
                List<Player> matches = plugin.getServer().matchPlayer(args.peek());
                if (matches.size() != 1) {
                    sender.sendMessage(plugin.getMsg("unknown-player", args.peek()));
                    return true;
                }
                targetPlayer = matches.get(0);
            }
            plugin.getCooldowns().remove(targetPlayer.getName());
            if (sender.getName().equals(targetPlayer.getName())) {
                sender.sendMessage(plugin.getMsg("your-cooldown-reset", sender.getName()));
            } else {
                sender.sendMessage(plugin.getMsg("player-cooldown-reset", targetPlayer.getName()));
                targetPlayer.sendMessage(plugin.getMsg("your-cooldown-reset", sender.getName()));
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
        List<String> results = new ArrayList<String>();
        if (args.size() == 1) {
            if (args.peek().toLowerCase().startsWith("reload")) {
                results.add("reload");
            }
            if (args.peek().toLowerCase().startsWith("save")) {
                results.add("save");
            }
            if (args.peek().toLowerCase().startsWith("resetcooldown")) {
                results.add("resetcooldown");
            }
        } else if (args.size() == 2) {
            if (args.peekFirst().equalsIgnoreCase("resetcooldown")) {
                for (String key: plugin.getCooldowns().keySet()) {
                    if (key.toLowerCase().startsWith(args.peekLast().toLowerCase())) {
                        results.add(key);
                    }
                }
            }
        }
        return results;
    }
}
