package com.norcode.bukkit.telewarp.persistence.warp;

import com.norcode.bukkit.telewarp.Telewarp;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SQLWarpManager extends BaseWarpManager {
    HashMap<String, Warp> warps;
    public SQLWarpManager(Telewarp plugin) {
        super(plugin);
        warps = new HashMap<String, Warp>();
        try {
            int rc = plugin.getDatabase().find(Warp.class).findRowCount();
        } catch (PersistenceException ex) {
            plugin.initDB();
        }
        reload();
    }

    @Override
    protected void reloadData() {
        for (Warp w: plugin.getDatabase().find(Warp.class).findList()) {
            w.setPlugin(plugin);
            warps.put(w.getName().toLowerCase(), w);
        }
    }

    @Override
    public Warp getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    @Override
    protected void save(Warp warp) {
        warps.put(warp.getName(), warp);
        plugin.getDatabase().save(warp);
    }

    @Override
    public void saveAll() {
        plugin.getDatabase().save(warps.values());
    }

    @Override
    public List<Warp> getAllWarps() {
        return new ArrayList<Warp>(warps.values());
    }

    @Override
    protected void delWarp(Warp warp) {
        warps.remove(warp.getName().toLowerCase());
        plugin.getDatabase().delete(warp);
    }

    @Override
    public Warp createWarp(String name, String world, double x, double y, double z, float yaw, float pitch) {
        Warp w = plugin.getDatabase().createEntityBean(Warp.class);
        w.setPlugin(plugin);
        w.setName(name.toLowerCase());
        w.setWorld(world);
        w.setX(x);
        w.setY(y);
        w.setZ(z);
        w.setYaw(yaw);
        w.setPitch(pitch);
        warps.put(w.getName(), w);
        return w;
    }
}
