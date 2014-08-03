package com.norcode.bukkit.telewarp.persistence.home;

import com.norcode.bukkit.telewarp.Telewarp;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public abstract class BaseHomeManager {

	protected Telewarp plugin;

	public BaseHomeManager(Telewarp plugin) {
		this.plugin = plugin;
	}

	public abstract void reloadData();

	public abstract void delHome(Home home);

	public abstract void saveHome(Home home);

	public abstract Home getHome(UUID playerId, String name);

	public abstract Home createHome(UUID playerId, String name, String world, double x, double y, double z, float yaw, float pitch);

	public abstract void saveAll();

	public abstract Map<String, Home> getHomesFor(UUID playerId);

	public abstract Collection<UUID> getPlayersWithHomes();

	public abstract Map<String,Home> getHomesFor(String name);
}
