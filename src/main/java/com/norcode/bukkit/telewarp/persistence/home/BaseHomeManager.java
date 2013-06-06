package com.norcode.bukkit.telewarp.persistence.home;

import com.norcode.bukkit.telewarp.Telewarp;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: andre
 * Date: 6/5/13
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseHomeManager {

    protected Telewarp plugin;

    public BaseHomeManager(Telewarp plugin) {
        this.plugin = plugin;
    }

    public abstract void reloadData();
    public abstract void delHome(Home home);
    public abstract void saveHome(Home home);
    public abstract Home getHome(String player, String name);
    public abstract Home createHome(String player, String name, String world, double x, double y, double z, float yaw, float pitch);
    public abstract void saveAll();
    public abstract Map<String, Home> getHomesFor(String player);
    public abstract Collection<String> getPlayersWithHomes();
}
