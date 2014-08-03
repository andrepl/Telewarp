package com.norcode.bukkit.telewarp.persistence.home;

import org.bukkit.Bukkit;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class PlayerHomeName {
	@Column(nullable = false)
	private UUID owner;
	@Column(nullable = false)
	private String name;

	public PlayerHomeName(UUID owner, String name) {
		super();
		this.owner = owner;
		this.name = name;
	}

	public PlayerHomeName() {
	}

	public String getOwnerName() {
		return Bukkit.getServer().getOfflinePlayer(owner).getName();
	}

	public UUID getOwnerId() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || !(o instanceof PlayerHomeName))
			return false;

		PlayerHomeName other = (PlayerHomeName) o;
		boolean namesEqual = false;
		boolean ownersEqual = false;
		if (getName() == null) {
			namesEqual = other.getName() == null;
		} else {
			namesEqual = getName().equals(other.getName());
		}
		if (getOwnerId() == null) {
			ownersEqual = other.getOwnerId() == null;
		} else {
			ownersEqual = getOwnerId().equals(other.getName());
		}
		return namesEqual && ownersEqual;
	}

	public int hashCode() {
		return (this.getOwnerName() + ":" + this.name).hashCode();
	}
}
