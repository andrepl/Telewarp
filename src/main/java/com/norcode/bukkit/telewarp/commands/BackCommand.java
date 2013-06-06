package com.norcode.bukkit.telewarp.commands;

import com.norcode.bukkit.telewarp.MetaKeys;
import com.norcode.bukkit.telewarp.Telewarp;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.LinkedList;
import java.util.List;


public class BackCommand extends BaseCommand {

    public BackCommand(Telewarp plugin) {
        super(plugin, "back");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args) {
        Player player = (Player) sender;
        if (!player.hasMetadata(MetaKeys.PREVIOUS_LOCATION)) {
            player.sendMessage(plugin.getMsg("no-previous-location"));
            return true;
        }
        String locStr = player.getMetadata(MetaKeys.PREVIOUS_LOCATION).get(0).asString();
        Location loc = plugin.deserializeLocation(locStr);
        if (loc == null) {
            player.sendMessage(plugin.getMsg("no-previous-location"));
            return true;
        }
        plugin.setPlayerMeta(player, MetaKeys.TELEPORT_TYPE, getName());
        player.teleport(loc, PlayerTeleportEvent.TeleportCause.COMMAND);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
        return null;
    }
}
