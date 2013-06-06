package com.norcode.bukkit.telewarp.persistence.home;

import com.norcode.bukkit.telewarp.Telewarp;

import javax.persistence.*;

@Entity
@Table(name="telewarp_home")
public class Home {
    @Transient com.norcode.bukkit.telewarp.Telewarp plugin;
    @EmbeddedId private PlayerHomeName playerHomeName;
    @Column private String world;
    @Column private double x;
    @Column private double y;
    @Column private double z;
    @Column private float yaw;
    @Column private float pitch;

    public Home() {}

    public PlayerHomeName getPlayerHomeName() {
        return playerHomeName;
    }

    public void setPlayerHomeName(PlayerHomeName phn) {
        playerHomeName = phn;
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

    @Transient public String getOwner() {
        return getPlayerHomeName().getOwner();
    }

    @Transient public String getName() {
        return getPlayerHomeName().getName();
    }

    @Transient public Telewarp getPlugin() {
        return plugin;
    }

    @Transient public void  setPlugin(Telewarp plugin) {
        this.plugin = plugin;
    }

    @Transient public String getKey() {
        return String.format("%s:%s", getOwner().toLowerCase(), getName().toLowerCase());
    }
}