package com.norcode.bukkit.telewarp.commands.tpa;

import com.norcode.bukkit.telewarp.MetaKeys;
import com.norcode.bukkit.telewarp.TPARequest;
import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.commands.BaseCommand;
import com.norcode.bukkit.telewarp.util.Util;
import com.norcode.bukkit.telewarp.util.chat.ClickAction;
import com.norcode.bukkit.telewarp.util.chat.Text;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.LinkedList;
import java.util.List;

public class TPACommand extends BaseCommand {
    public TPACommand(Telewarp plugin) {
        super(plugin, "tpa");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args) {
        Player player = (Player) sender;
        if (args.size() == 0) {
            return false;
        }
        List<Player> matches = plugin.getServer().matchPlayer(args.peek());
        if (matches.size() != 1) {
            sender.sendMessage(plugin.getMsg("unknown-player", args.peek()));
            return true;
        }
        Player destination = matches.get(0);
        TPARequest req = new TPARequest(destination.getName(), player.getName(), player.getName());
        destination.sendMessage(plugin.getMsg("incoming-tpa-request", player.getName()));

        Text text = new Text("").append("Or you can click the links below.");
        Text accept_button = new Text("").append("【").setBold(true).setColor(ChatColor.BLUE).
                append(" ACCEPT ").setColor(ChatColor.GREEN).setClick(ClickAction.RUN_COMMAND, "/tpayes")
                .append("】").setBold(true).setColor(ChatColor.GREEN);
        Text reject_button = new Text("").append("【").setBold(true).setColor(ChatColor.BLUE).
                append(" REJECT ").setColor(ChatColor.RED).setClick(ClickAction.RUN_COMMAND, "/tpano")
                .append("】").setBold(true).setColor(ChatColor.GREEN);

        text = text.append(accept_button).append(reject_button);
        Util.send(destination, text);

        plugin.setPlayerMeta(destination, MetaKeys.TPA_REQUEST, req);
        player.sendMessage(plugin.getMsg("tpa-request-sent", destination.getName()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
        return null;
    }
}
