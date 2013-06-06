package com.norcode.bukkit.telewarp.persistence.warp;

import com.norcode.bukkit.telewarp.util.ConfigAccessor;
import com.norcode.bukkit.telewarp.Telewarp;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class YamlWarpManager extends BaseWarpManager {
    ConfigAccessor config;


    public YamlWarpManager(Telewarp plugin) {
        super(plugin);
        config = new ConfigAccessor(plugin, "warp.yml");
        reload();
    }

    @Override
    public void reloadData() {
        config.reloadConfig();
    }

    @Override
    public Warp getWarp(String name) {
        String key = name.toLowerCase();
        ConfigurationSection sec = config.getConfig().getConfigurationSection(key);
        if (sec == null) {
            return null;
        }
        return new Warp(plugin, key, sec.getString("world"), sec.getDouble("x"),
                sec.getDouble("y"), sec.getDouble("z"), (float) sec.getDouble("yaw", 0.0d),
                (float) sec.getDouble("pitch", 0.0), sec.getDouble("cost", 0.0), sec.getString("description"));
    }

    @Override
    protected void save(Warp warp) {
        String key = warp.getName().toLowerCase();
        ConfigurationSection sec = config.getConfig().getConfigurationSection(key);
        if (sec == null) {
            sec = config.getConfig().createSection(key);
        }
        sec.set("world", warp.getWorld());
        sec.set("x", warp.getX());
        sec.set("y", warp.getY());
        sec.set("z", warp.getZ());
        sec.set("yaw", warp.getYaw());
        sec.set("pitch", warp.getPitch());
        sec.set("cost", warp.getCost());
        sec.set("description", warp.getDescription());

    }

    @Override
    public void saveAll() {
        config.saveConfig();
    }

    @Override
    public List<Warp> getAllWarps() {
        List<Warp> warps = new ArrayList<Warp>();
        ConfigurationSection sec;
        for (String key: config.getConfig().getKeys(false)) {
            sec = config.getConfig().getConfigurationSection(key);
                warps.add(new Warp(plugin, key, sec.getString("world"), sec.getDouble("x"),
                        sec.getDouble("y"), sec.getDouble("z"), (float) sec.getDouble("yaw", 0.0d),
                        (float) sec.getDouble("pitch", 0.0), sec.getDouble("cost", 0.0), sec.getString("description")));
        }
        return warps;
    }

    @Override
    protected void delWarp(Warp warp) {
        String key = warp.getName().toLowerCase();
        config.getConfig().set(key, null);
    }

    @Override
    public Warp createWarp(String name, String world, double x, double y, double z, float yaw, float pitch) {
        Warp w = new Warp(plugin, name, world, x, y, z, yaw, pitch);
        save(w);
        return w;
    }
}
