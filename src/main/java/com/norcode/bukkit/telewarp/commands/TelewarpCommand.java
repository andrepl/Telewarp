package com.norcode.bukkit.telewarp.commands;

import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.commands.BaseCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
        List<String> results = new ArrayList<String>();
        if (args.peekLast().toLowerCase().startsWith("reload")) {
            if (sender.hasPermission("telewarp.admin")) {
                results.add("reload");
            }
        }
        return results;
    }
}
