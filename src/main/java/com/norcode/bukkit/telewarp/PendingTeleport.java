package com.norcode.bukkit.telewarp;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created with IntelliJ IDEA.
 * User: andre
 * Date: 6/1/13
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class PendingTeleport extends BukkitRunnable {

	private double paid;
	private String player;
	private String world;
	private double x;
	private double y;
	private double z;
	private float yaw = 0.0f;
	private float pitch = 0.0f;
	private Telewarp plugin;

	public PendingTeleport(Telewarp telewarp, Player player, Location location, double paid) {
		this.paid = paid;
		this.plugin = telewarp;
		this.player = player.getName();
		this.world = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
	}

	public String getPlayerName() {
		return player;
	}

	@Override
	public void cancel() {
		getPlayer().removeMetadata(MetaKeys.PENDING_TELEPORT, plugin);
		if (plugin.economy != null) {
			plugin.economy.depositPlayer(getPlayerName(), paid); // refund money if teleport fails.
		}
		super.cancel();
	}

	public Player getPlayer() {
		return plugin.getServer().getPlayer(player);
	}

	public Location getLocation() {
		return new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);
	}

	@Override
	public void run() {
		Player p = getPlayer();
		if (p != null) {
			p.removeMetadata(MetaKeys.PENDING_TELEPORT, plugin);
			Location l = getLocation();
			if (!l.getChunk().isLoaded()) {
				l.getChunk().load();
			}
			plugin.setPlayerMeta(p, MetaKeys.ACTIVE_TELEPORT, this);
			if (p.hasPermission("telewarp.smoke")) {
				for (int i = 0; i < 8; i++) {
					p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, i);
					p.getWorld().playEffect(l, Effect.SMOKE, i);
				}
			}
			p.teleport(getLocation());
		}
	}
}
