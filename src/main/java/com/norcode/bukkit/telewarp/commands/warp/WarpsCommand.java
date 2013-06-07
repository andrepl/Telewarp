package com.norcode.bukkit.telewarp.commands.warp;


import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.commands.BaseCommand;
import com.norcode.bukkit.telewarp.persistence.warp.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WarpsCommand extends BaseCommand {
    private int perPage = 8;
    public WarpsCommand(Telewarp plugin) {
        super(plugin, null);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, LinkedList<String> args) {
        int page = 1;
        if (args.size() > 0) {
            try {
                page = Integer.parseInt(args.peek());
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }
        List<Warp> warps = plugin.getWarpManager().getWarpsFor(sender);

        int maxPage =(int) Math.ceil(warps.size()/(double)perPage);
        int start = (page-1) * perPage;
        if (page > maxPage) {
            page = maxPage;
        }
        if (page == 0) {
            sender.sendMessage(plugin.getMsg("no-warp"));
            return true;
        }
        ArrayList<String> lines = new ArrayList<String>();
        lines.add(plugin.getMsg("available-warps", page, maxPage));
        for (int i=start;i<start+perPage&&i<warps.size();i++) {
            Warp w = warps.get(i);
            lines.add(plugin.getMsg("available-warp-line", w.getName(), w.getCost(), w.getDescription() == null ? "" : w.getDescription()));
        }
        sender.sendMessage(lines.toArray(new String[lines.size()]));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, LinkedList<String> args) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
