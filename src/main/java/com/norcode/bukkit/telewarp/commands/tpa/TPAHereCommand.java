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

/**
 * Created with IntelliJ IDEA.
 * User: andre
 * Date: 6/5/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class TPAHereCommand extends BaseCommand {
    public TPAHereCommand(Telewarp plugin) {
        super(plugin, "tpahere");
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
        TPARequest req = new TPARequest(player.getName(), destination.getName(), player.getName());
        destination.sendMessage(plugin.getMsg("incoming-tpahere-request", player.getName()));
        plugin.setPlayerMeta(destination, MetaKeys.TPA_REQUEST, req);
        player.sendMessage(plugin.getMsg("tpa-request-sent", destination.getName()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
