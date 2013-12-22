package com.norcode.bukkit.telewarp.persistence.warp;

import com.norcode.bukkit.telewarp.Telewarp;
import org.bukkit.Location;
import org.bukkit.World;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "telewarp_warp")
public class Warp {

	@Transient
	private Telewarp plugin;
	@Id
	@Column
	private String name;
	@Column
	private String world;
	@Column
	private double x;
	@Column
	private double y;
	@Column
	private double z;
	@Column
	private float yaw = 0.0f;
	@Column
	private float pitch = 0.0f;
	@Column
	private double cost = 0.0d;
	@Column
	private String description = null;

	public Warp(Telewarp plugin, String name, String world, double x, double y, double z) {
		this.plugin = plugin;
		this.name = name;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Warp() {
	}

	public Warp(Telewarp plugin, String name, String world, double x, double y, double z, float yaw, float pitch) {
		this(plugin, name, world, x, y, z);
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Warp(Telewarp plugin, String name, String world, double x, double y, double z, float yaw, float pitch, double cost) {
		this(plugin, name, world, x, y, z, yaw, pitch);
		this.cost = cost;
	}

	public Warp(Telewarp plugin, String name, String world, double x, double y, double z, float yaw, float pitch, double cost, String description) {
		this(plugin, name, world, x, y, z, yaw, pitch, cost);
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Location getLocation() {
		World w = plugin.getServer().getWorld(world);
		if (w != null) {
			return new Location(w, x, y, z, yaw, pitch);
		}
		return null;
	}

	@Transient
	public void setPlugin(Telewarp plugin) {
		this.plugin = plugin;
	}
}
