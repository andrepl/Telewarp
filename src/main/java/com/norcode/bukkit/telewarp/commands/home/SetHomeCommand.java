package com.norcode.bukkit.telewarp.commands.home;

import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.commands.BaseCommand;
import com.norcode.bukkit.telewarp.persistence.home.Home;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SetHomeCommand extends BaseCommand {
    public SetHomeCommand(Telewarp plugin) {
        super(plugin, null);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args) {
        Player player = (Player) sender;
        Block bedBlock = player.getTargetBlock(null, 4);
        if (!bedBlock.getType().equals(Material.BED_BLOCK)) {
            sender.sendMessage(plugin.getMsg("must-target-bed"));
            return true;
        }

        if (plugin.isGriefPreventionSupportEnabled()) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(bedBlock.getLocation(), false, null);
            if (claim == null)   {}
        }

        String homeName = null;
        if (args.size() == 0) {
            homeName = "home";
        } else {
            homeName = args.peek().toLowerCase();
        }
        if (homeName.toLowerCase().equals("list") || !plugin.getValidNamePattern().matcher(homeName).matches()) {
            player.sendMessage(plugin.getMsg("invalid-name", homeName));
            return true;
        }
        Home home = plugin.getHomeManager().getHome(player.getName(), homeName);
        Location l = bedBlock.getLocation();
        if (home != null) {
            home.setWorld(l.getWorld().getName());
            home.setX(l.getX());
            home.setY(l.getY());
            home.setZ(l.getZ());
            home.setYaw(l.getYaw());
            home.setPitch(l.getPitch());
        } else {
            // check totals
            Map<String, Home> playerHomes = plugin.getHomeManager().getHomesFor(player.getName());
            if (playerHomes != null && playerHomes.size() > plugin.getPlayerMaxHomes(player.getName())) {
                sender.sendMessage(plugin.getMsg("too-many-homes"));
                return true;
            }
            home = plugin.getHomeManager().createHome(player.getName(), homeName.toLowerCase(), l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
        }
        home.setPlugin(plugin);
        plugin.getHomeManager().saveHome(home);
        player.sendMessage(plugin.getMsg("home-set", home.getName()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
        return null;
    }
}
