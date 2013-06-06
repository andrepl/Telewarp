package com.norcode.bukkit.telewarp.commands.warp;

import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.commands.BaseCommand;
import com.norcode.bukkit.telewarp.persistence.warp.Warp;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: andre
 * Date: 6/2/13
 * Time: 11:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class SetWarpCommand extends BaseCommand {
    protected int minArgs = 1;

    public SetWarpCommand(Telewarp plugin) {
        super(plugin, "setwarp");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args) {
        if (args.size() == 0) {
            return false;
        }
        Player player = (Player) sender;
        String name = args.pop();
        Warp warp = plugin.getWarpManager().getWarp(name);
        String world = player.getWorld().getName();
        Location l = player.getLocation();
        if (warp == null) {
            warp = plugin.getWarpManager().createWarp(name, player.getWorld().getName(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
        } else {
            warp.setWorld(world);
            warp.setX(l.getX());
            warp.setY(l.getY());
            warp.setZ(l.getZ());
            warp.setYaw(l.getYaw());
            warp.setPitch(l.getPitch());
        }
        if (args.size() >= 1) {
            try {
                double cost = Double.parseDouble(args.peek());
                warp.setCost(cost);
                args.pop();
            } catch (IllegalArgumentException ex) {
                sender.sendMessage(plugin.getMsg("invalid-cost", args.pop()));
                return true;
            }
        }
        if (args.size() > 0) {
            String desc = "";
            for (String a: args) {
                desc += a + " ";
            }
            if (desc.endsWith(" ")) {
                desc = desc.substring(0, desc.length()-1);
            }
            warp.setDescription(desc);
        }
        plugin.getWarpManager().saveWarp(warp);
        player.sendMessage(plugin.getMsg("warp-saved", warp.getName()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
