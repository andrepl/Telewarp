package com.norcode.bukkit.telewarp.persistence.home;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PlayerHomeName {
	@Column(nullable = false)
	private String owner;
	@Column(nullable = false)
	private String name;

	public PlayerHomeName(String owner, String name) {
		super();
		this.owner = owner;
		this.name = name;
	}

	public PlayerHomeName() {
	}

	;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
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
		if (getOwner() == null) {
			ownersEqual = other.getOwner() == null;
		} else {
			ownersEqual = getOwner().equals(other.getName());
		}
		return namesEqual && ownersEqual;
	}

	public int hashCode() {
		return (this.owner + ":" + this.name).hashCode();
	}
}
