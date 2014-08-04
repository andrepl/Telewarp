package com.norcode.bukkit.telewarp.persistence.home;

import com.norcode.bukkit.telewarp.Telewarp;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SQLHomeManager extends BaseHomeManager {
	HashMap<UUID, HashMap<String, Home>> homes = new HashMap<UUID, HashMap<String, Home>>();

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
		for (Home home : plugin.getDatabase().find(Home.class).findList()) {
			if (!homes.containsKey(home.getOwnerId())) {
				homes.put(home.getOwnerId(), new HashMap<String, Home>());
			}
			homes.get(home.getOwnerId()).put(home.getName().toLowerCase(), home);
		}
		plugin.debug("Loaded " + homes.size() + " player homes.");
	}

	@Override
	public void delHome(Home home) {
		Home h = null;
		try {
			h = homes.get(home.getOwnerId()).remove(home.getName().toLowerCase());
		} catch (NullPointerException ex) {
		}

		if (h != null) {
			plugin.getDatabase().delete(h);
		}
	}

	@Override
	public void saveHome(Home home) {
		if (!homes.containsKey(home.getOwnerId())) {
			homes.put(home.getOwnerId(), new HashMap<String, Home>());
		}
		homes.get(home.getOwnerId()).put(home.getName().toLowerCase(), home);
		plugin.getDatabase().save(home);
	}

	@Override
	public Home getHome(UUID playerId, String name) {
		if (!homes.containsKey(playerId)) {
			return null;
		}
		return homes.get(playerId).get(name.toLowerCase());
	}

	@Override
	public Home createHome(UUID playerId, String name, String world, double x, double y, double z, float yaw, float pitch) {
		Home home = plugin.getDatabase().createEntityBean(Home.class);

		home.setPlayerHomeName(new PlayerHomeName(playerId, name));
		home.setWorld(world);
		home.setX(x);
		home.setY(y);
		home.setZ(z);
		home.setYaw(yaw);
		home.setPitch(pitch);
		if (!homes.containsKey(home.getOwnerId())) {
			homes.put(home.getOwnerId(), new HashMap<String, Home>());
		}
		homes.get(home.getOwnerId()).put(home.getName().toLowerCase(), home);
		return home;
	}

	@Override
	public void saveAll() {
		for (HashMap<String, Home> map : homes.values()) {
			plugin.getDatabase().save(map.values());
		}
	}

	@Override
	public HashMap<String, Home> getHomesFor(UUID playerId) {
		HashMap<String, Home> results = new HashMap<String, Home>();
		if (homes.containsKey(playerId)) {
			results.putAll(homes.get(playerId));
		}
		return results;
	}

	@Override
	public Collection<UUID> getPlayersWithHomes() {
		return homes.keySet();
	}

	@Override
	public Map<String, Home> getHomesFor(String name) {
		for (OfflinePlayer p: Bukkit.getOfflinePlayers()) {
			if (p.getName().equalsIgnoreCase(name)) {
				return getHomesFor(p.getUniqueId());
			}
		}
		return new HashMap<String, Home>();
	}

}
