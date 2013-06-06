package com.norcode.bukkit.telewarp.commands.home;

import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.commands.BaseCommand;
import com.norcode.bukkit.telewarp.persistence.home.Home;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: andre
 * Date: 6/5/13
 * Time: 7:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class HomesCommand extends BaseCommand {
    public HomesCommand(Telewarp plugin) {
        super(plugin, null);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args) {
        Player player = (Player) sender;
        Map<String, Home> homes = plugin.getHomeManager().getHomesFor(sender.getName());
        // List Homes
        if (homes.size() == 0) {
            player.sendMessage(plugin.getMsg("no-home-location"));
            return true;
        }
        List<String> list = new LinkedList<String>();
        list.add(plugin.getMsg("available-homes"));
        for (Map.Entry<String, Home> entry: homes.entrySet()) {
            list.add(plugin.getMsg("home-list-entry", entry.getKey()));
        }
        player.sendMessage(list.toArray(new String[0]));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
        return null;
    }
}
