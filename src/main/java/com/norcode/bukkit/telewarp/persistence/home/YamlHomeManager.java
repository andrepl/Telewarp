package com.norcode.bukkit.telewarp.persistence.home;

import com.norcode.bukkit.telewarp.Telewarp;
import com.norcode.bukkit.telewarp.util.ConfigAccessor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: andre
 * Date: 6/5/13
 * Time: 11:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class YamlHomeManager extends BaseHomeManager {
    ConfigAccessor accessor;
    public YamlHomeManager(Telewarp telewarp) {
        super(telewarp);
        accessor = new ConfigAccessor(plugin, "homes.yml");
        reloadData();
    }

    @Override
    public void reloadData() {
        accessor.reloadConfig();
    }

    @Override
    public void delHome(Home home) {
        ConfigurationSection sec = accessor.getConfig().getConfigurationSection(home.getOwner().toLowerCase());
        if (sec == null) {
            return;
        }
        sec.set(home.getName().toLowerCase(), null);
    }

    @Override
    public void saveHome(Home home) {
        ConfigurationSection sec = accessor.getConfig().getConfigurationSection(home.getOwner().toLowerCase());
        if (sec == null) {
            sec = accessor.getConfig().createSection(home.getOwner().toLowerCase());
        }
        ConfigurationSection hs = sec.getConfigurationSection(home.getName().toLowerCase());
        if (hs == null) {
            hs = sec.createSection(home.getName().toLowerCase());
        }
        hs.set("name", home.getName().toLowerCase());
        hs.set("world", home.getWorld());
        hs.set("x", home.getX());
        hs.set("y", home.getY());
        hs.set("z", home.getZ());
        hs.set("yaw", home.getYaw());
        hs.set("pitch", home.getPitch());
    }

    @Override
    public Home getHome(String player, String name) {
        ConfigurationSection sec = accessor.getConfig().getConfigurationSection(player.toLowerCase() + "." + name.toLowerCase());
        if (sec == null) {
            return null;
        } else {
            Home h = new Home();
            h.setPlayerHomeName(new PlayerHomeName(player.toLowerCase(), name.toLowerCase()));
            h.setWorld(sec.getString("world"));
            h.setX(sec.getDouble("x"));
            h.setY(sec.getDouble("y"));
            h.setZ(sec.getDouble("z"));
            h.setYaw((float) sec.getDouble("yaw"));
            h.setPitch((float) sec.getDouble("pitch"));
            h.setPlugin(plugin);
            return h;
        }
    }

    @Override
    public Home createHome(String player, String name, String world, double x, double y, double z, float yaw, float pitch) {
        Home h = new Home();
        h.setPlayerHomeName(new PlayerHomeName(player.toLowerCase(), name.toLowerCase()));
        h.setWorld(world);
        h.setX(x);
        h.setY(y);
        h.setZ(z);
        h.setYaw(yaw);
        h.setPitch(pitch);
        h.setPlugin(plugin);
        saveHome(h);
        return h;
    }

    @Override
    public void saveAll() {
        accessor.saveConfig();
    }

    @Override
    public Map<String, Home> getHomesFor(String player) {
        HashMap<String, Home> results = new HashMap<String, Home>();
        ConfigurationSection sec = accessor.getConfig().getConfigurationSection(player.toLowerCase());
        if (sec != null) {
            Home h;
            ConfigurationSection sec2;
            for (String key: sec.getKeys(false)) {
                sec2 = sec.getConfigurationSection(key);
                h = new Home();
                h.setPlayerHomeName(new PlayerHomeName(player.toLowerCase(), sec2.getString("name").toLowerCase()));
                h.setWorld(sec2.getString("world"));
                h.setX(sec2.getDouble("x"));
                h.setY(sec2.getDouble("y"));
                h.setZ(sec2.getDouble("z"));
                h.setYaw((float) sec2.getDouble("yaw"));
                h.setPitch((float) sec2.getDouble("pitch"));
                h.setPlugin(plugin);
                results.put(key.toLowerCase(), h);
            }
        }
        return results;
    }

    @Override
    public Collection<String> getPlayersWithHomes() {
        return accessor.getConfig().getKeys(false);
    }
}
