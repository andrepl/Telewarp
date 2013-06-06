package com.norcode.bukkit.telewarp.persistence.warp;

import com.norcode.bukkit.telewarp.Telewarp;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseWarpManager {
    protected Permission wildcardWarpPermission;
    protected Telewarp plugin;

    protected BaseWarpManager(Telewarp plugin) {
        this.wildcardWarpPermission =  new Permission("telewarp.warp.*", PermissionDefault.TRUE);
        plugin.getServer().getPluginManager().addPermission(this.wildcardWarpPermission);
        this.plugin = plugin;
    }

    public void reload() {
        reloadData();
        this.wildcardWarpPermission.getChildren().clear();
        for (Warp w: getAllWarps()) {
            updatePermissionNode(w.getName());
        }
        this.wildcardWarpPermission.recalculatePermissibles();
    }
    protected abstract void reloadData();

    public abstract Warp getWarp(String name);

    protected abstract void save(Warp warp);
    public void saveWarp(Warp warp) {
        save(warp);
        updatePermissionNode(warp.getName());
    }

    public abstract void saveAll();

    public abstract List<Warp> getAllWarps();
    public List<Warp> getWarpsFor(CommandSender sender) {
        List<Warp> warps = new ArrayList<Warp>();
        ConfigurationSection sec;
        for (Warp w: getAllWarps()) {
            if (sender.hasPermission("telewarp.warp." + w.getName().toLowerCase())) {
                warps.add(new Warp(plugin, w.getName().toLowerCase(), w.getWorld(), w.getX(),
                        w.getY(), w.getZ(), w.getYaw(), w.getPitch(), w.getCost(), w.getDescription()));
            }
        }
        return warps;
    }
    public void deleteWarp(Warp warp) {
        String key = warp.getName().toLowerCase();
        plugin.getServer().getPluginManager().removePermission("telewarp.warp." + key);
        wildcardWarpPermission.getChildren().remove("telewarp.warp." + key);
        wildcardWarpPermission.recalculatePermissibles();
        delWarp(warp);
    }
    protected abstract void delWarp(Warp warp);

    private void updatePermissionNode(String key) {
        Permission oldPerm = plugin.getServer().getPluginManager().getPermission("telewarp.warp." + key.toLowerCase());
        if (oldPerm != null) {
            plugin.getServer().getPluginManager().removePermission(oldPerm);
        }
        Permission perm = new Permission("telewarp.warp." + key.toLowerCase(), PermissionDefault.TRUE);
        perm.addParent(this.wildcardWarpPermission, true);
        plugin.getServer().getPluginManager().addPermission(perm);
        perm.recalculatePermissibles();
    }

    public abstract Warp createWarp(String name, String world, double x, double y, double z, float yaw, float pitch);
}
