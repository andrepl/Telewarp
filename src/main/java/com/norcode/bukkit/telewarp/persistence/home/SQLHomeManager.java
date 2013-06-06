package com.norcode.bukkit.telewarp.persistence.home;

import com.norcode.bukkit.telewarp.Telewarp;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class SQLHomeManager extends BaseHomeManager {
    HashMap<String, HashMap<String, Home>> homes = new HashMap<String, HashMap<String, Home>>();
    public SQLHomeManager(Telewarp telewarp) {
        super(telewarp);
        reloadData();
    }

    @Override
    public void reloadData() {
        try {
            int rowCount = plugin.getDatabase().find(Home.class).findRowCount();
        } catch (PersistenceException ex) {
            plugin.initDB();
        }
        for (Home home: plugin.getDatabase().find(Home.class).findList()) {
            if (!homes.containsKey(home.getOwner().toLowerCase())) {
                homes.put(home.getOwner().toLowerCase(), new HashMap<String, Home>());
            }
            homes.get(home.getOwner().toLowerCase()).put(home.getName().toLowerCase(), home);
        }
        plugin.debug("Loaded " + homes.size() + " player homes.");
    }

    @Override
    public void delHome(Home home) {
        Home h = null;
        try {
            h = homes.get(home.getOwner().toLowerCase()).remove(home.getName().toLowerCase());
        } catch (NullPointerException ex) {}

        if (h != null) {
            plugin.getDatabase().delete(h);
        }
    }

    @Override
    public void saveHome(Home home) {
        if (!homes.containsKey(home.getOwner().toLowerCase())) {
            homes.put(home.getOwner().toLowerCase(), new HashMap<String, Home>());
        }
        homes.get(home.getOwner().toLowerCase()).put(home.getName().toLowerCase(), home);
        plugin.getDatabase().save(home);
    }

    @Override
    public Home getHome(String player, String name) {
        if (!homes.containsKey(player.toLowerCase())) {
            return null;
        }
        return homes.get(player.toLowerCase()).get(name.toLowerCase());
    }

    @Override
    public Home createHome(String player, String name, String world, double x, double y, double z, float yaw, float pitch) {
        Home home = plugin.getDatabase().createEntityBean(Home.class);
        home.setPlayerHomeName(new PlayerHomeName(player, name));
        home.setWorld(world);
        home.setX(x);
        home.setY(y);
        home.setZ(z);
        home.setYaw(yaw);
        home.setPitch(pitch);
        if (!homes.containsKey(home.getOwner().toLowerCase())) {
            homes.put(home.getOwner().toLowerCase(), new HashMap<String, Home>());
        }
        homes.get(home.getOwner().toLowerCase()).put(home.getName().toLowerCase(), home);
        return home;
    }

    @Override
    public void saveAll() {
        for (HashMap<String, Home> map: homes.values()) {
            plugin.getDatabase().save(map.values());
        }
    }

    @Override
    public HashMap<String, Home> getHomesFor(String player) {
        HashMap<String, Home> results =  new HashMap<String, Home>();
        if (homes.containsKey(player.toLowerCase())) {
            results.putAll(homes.get(player.toLowerCase()));
        }
        return results;
    }

    @Override
    public Collection<String> getPlayersWithHomes() {
        return homes.keySet();
    }
}
