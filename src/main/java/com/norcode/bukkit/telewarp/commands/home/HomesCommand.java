package com.norcode.bukkit.telewarp.commands.home;

import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.commands.BaseCommand;
import com.norcode.bukkit.telewarp.persistence.home.Home;
import com.norcode.bukkit.telewarp.util.Util;
import com.norcode.bukkit.telewarp.util.chat.ClickAction;
import com.norcode.bukkit.telewarp.util.chat.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

        Text text = new Text("").append(plugin.getMsg("available-homes"));

        for (Map.Entry<String, Home> entry: homes.entrySet()) {
            Text line = new Text("").append(plugin.getMsg("home-list-entry", entry.getKey()));
            Text link = new Text("").append("【").setBold(true).setColor(ChatColor.BLUE).
                    append(" GO NOW ").setColor(ChatColor.GREEN).setClick(ClickAction.RUN_COMMAND, "/home " + entry.getKey())
                    .append("】").setBold(true).setColor(ChatColor.GREEN);
            text = text.append(line).append(link);
        }

        Util.send(player, text);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
        return null;
    }
}
