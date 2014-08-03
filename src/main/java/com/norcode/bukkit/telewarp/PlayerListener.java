package com.norcode.bukkit.telewarp;

import com.norcode.bukkit.telewarp.persistence.warp.Warp;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {
	private final Telewarp plugin;

	public PlayerListener(Telewarp plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteractSign(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getClickedBlock().getType().equals(Material.WALL_SIGN) || event.getClickedBlock().getType().equals(Material.SIGN_POST)) {
				if (!plugin.getConfig().getBoolean("enable-warp-signs", true)) {
					event.getPlayer().sendMessage(plugin.getMsg("warp-signs-disabled"));
					return;
				}
				if (!event.getPlayer().hasPermission("telewarp.signs.use")) {
					event.getPlayer().sendMessage(plugin.getMsg("no-sign-use-permission"));
					return;
				}
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_BLUE + "[warp]")) {
					event.setCancelled(true);
					Warp warp = plugin.getWarpManager().getWarp(sign.getLine(1));
					if (warp != null) {
						plugin.setPlayerMeta(event.getPlayer(), MetaKeys.DESTINATION_WARP, warp.getName().toLowerCase());
						event.getPlayer().teleport(warp.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
					} else {
						event.getPlayer().sendMessage(plugin.getMsg("unknown-warp", warp.getName()));
					}
				} else if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_BLUE + "[telewarp]")) {
					if (sign.getLine(1).equalsIgnoreCase("reset") && sign.getLine(2).equalsIgnoreCase("cooldown")) {
						event.setCancelled(true);
						double cost = parseCost(sign.getLine(3));
						if (cost > 0) {
							if (!plugin.economy.withdrawPlayer(event.getPlayer().getName(), cost).transactionSuccess()) {
								event.getPlayer().sendMessage(plugin.getMsg("insufficient-funds", cost));
								return;
							}
						}
						plugin.getCooldowns().remove(event.getPlayer().getName());
						event.getPlayer().sendMessage(plugin.getMsg("your-cooldown-reset", event.getPlayer().getName()));
					}
				}
			}
		}
	}

	public double parseCost(String s) {
		if (s.startsWith("$")) {
			s = s.substring(1);
		}
		if (s.startsWith("-")) {
			s = s.substring(1);
		}
		try {
			return Double.parseDouble(s);
		} catch (IllegalArgumentException ex) {
			return 0;
		}
	}

	@EventHandler
	public void onPlayerCreateWarpSign(SignChangeEvent event) {
		if (event.getPlayer().hasPermission("telewarp.signs.create")) {
			if (event.getLine(0).toLowerCase().equals("[warp]")) {
				Warp warp = plugin.getWarpManager().getWarp(event.getLine(1));
				if (warp == null) {
					event.getPlayer().sendMessage(plugin.getMsg("unknown-warp", event.getLine(1)));
					return;
				}
				event.setLine(0, ChatColor.DARK_BLUE + event.getLine(0));
				event.getPlayer().sendMessage(plugin.getMsg("warp-sign-created", warp.getName()));
			} else if (event.getLine(0).equalsIgnoreCase("[TeleWarp]")) {
				if (event.getLine(1).equalsIgnoreCase("reset")) {
					if (event.getLine(2).equalsIgnoreCase("cooldown")) {
						String cost = event.getLine(3);
						if (cost.startsWith("$")) {
							cost = cost.substring(1);
						}
						if (cost.startsWith("-")) {
							cost = cost.substring(1);
						}
						double dcost = 0;
						if (cost.length() >= 1) {
							try {
								dcost = Double.parseDouble(cost);

							} catch (IllegalArgumentException ex) {
								event.getPlayer().sendMessage(plugin.getMsg("Invalid cost"));
							}
						}
						event.setLine(0, ChatColor.DARK_BLUE + event.getLine(0));
						event.getPlayer().sendMessage(plugin.getMsg("reset-cooldown-sign-created", dcost));
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = (Player) event.getEntity();
		Location deathLoc = player.getLocation();
		if (player.hasPermission("telewarp.commands.back.ondeath")) {
			plugin.debug("Setting /back location to " + plugin.serializeLocation(deathLoc));
			plugin.setPlayerMeta(player, MetaKeys.PREVIOUS_LOCATION, plugin.serializeLocation(deathLoc));
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.getPlayer().hasMetadata(MetaKeys.PENDING_TELEPORT)) {
			Location from = event.getFrom();
			Location to = event.getTo();
			if (from.getWorld() != to.getWorld() || from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
				PendingTeleport tp = (PendingTeleport) event.getPlayer().getMetadata(MetaKeys.PENDING_TELEPORT).get(0).value();
				tp.cancel();
				event.getPlayer().removeMetadata(MetaKeys.PENDING_TELEPORT, plugin);
				event.getPlayer().sendMessage(plugin.getMsg("teleport-cancelled"));
			}
		}
	}


	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		// Passing through a completed and sanctioned teleport.
		if (player.hasMetadata(MetaKeys.ACTIVE_TELEPORT)) {
			plugin.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis() + plugin.getPlayerCooldown(player));
			player.removeMetadata(MetaKeys.ACTIVE_TELEPORT, plugin);
			plugin.debug("Setting /back location to " + plugin.serializeLocation(event.getFrom()));
			player.setMetadata(MetaKeys.PREVIOUS_LOCATION, new FixedMetadataValue(plugin, plugin.serializeLocation(event.getFrom())));
			return;
		}

		// Only handle plugin and command causes
		if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND)) {
			return;
		}

		// cancel the one in progress if they request a new one.
		if (player.hasMetadata(MetaKeys.PENDING_TELEPORT)) {
			PendingTeleport tp = (PendingTeleport) player.getMetadata(MetaKeys.PENDING_TELEPORT).get(0).value();
			player.removeMetadata(MetaKeys.PENDING_TELEPORT, plugin);
			tp.cancel();
		}

		// This was a new valid request.
		event.setCancelled(true);
		double cost = plugin.getCost(player, "");
		long warmup = plugin.getWarmup(player, "");

		if (plugin.getCooldowns().containsKey(player.getName())) {
			long expiry = plugin.getCooldowns().get(player.getName());
			long now = System.currentTimeMillis();
			plugin.debug("Now: " + now + ", Cooldown Expiry: " + expiry);
			if (expiry > System.currentTimeMillis()) {
				player.sendMessage(plugin.getMsg("active-cooldown", formatDuration(expiry - now)));
				return;
			} else {
				plugin.getCooldowns().remove(player.getName());
			}
		}

		if (player.hasMetadata(MetaKeys.TELEPORT_TYPE)) {
			// it was from one of our commands.
			String tt = player.getMetadata(MetaKeys.TELEPORT_TYPE).get(0).asString();
			player.removeMetadata(MetaKeys.TELEPORT_TYPE, plugin);
			if (tt.equals("warp")) {
				Warp warp = plugin.getWarpManager().getWarp(player.getMetadata(MetaKeys.DESTINATION_WARP).get(0).asString());
				player.removeMetadata(MetaKeys.DESTINATION_WARP, plugin);
				if (!player.hasPermission("telewarp.warp." + warp.getName().toLowerCase())) {
					player.sendMessage(plugin.getMsg("no-warp-permission", warp.getName()));
					return;
				}
				cost = warp.getCost();
			} else {
				cost = plugin.getCost(player, tt);
			}
		}
		if (cost > 0) {
			if (plugin.economy != null) {
				if (!plugin.economy.withdrawPlayer(player.getName(), cost).transactionSuccess()) {
					player.sendMessage(plugin.getMsg("insufficient-funds", cost));
					return;
				}
			}
		}
		if (warmup > 1000) {
			player.sendMessage(plugin.getMsg("delay", String.format("%.2f", (warmup / 1000.0F))));
		}
		long warmupTicks = (long) (warmup / 1000.0) * 20;
		PendingTeleport tp = new PendingTeleport(plugin, player, event.getTo(), cost);
		plugin.setPlayerMeta(player, MetaKeys.PENDING_TELEPORT, tp);
		tp.runTaskLater(plugin, warmupTicks);
		if (plugin.getConfig().getBoolean("confusion-on-warmup") && !player.hasPermission("telewarp.confusionimmunity")) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int) warmupTicks, 1, false));
		}
	}

	public String formatDuration(long millis) {
		return String.format("%d min, %d sec",
				TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis) -
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
		);
	}

}
