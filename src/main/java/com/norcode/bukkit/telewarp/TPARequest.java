package com.norcode.bukkit.telewarp;

public class TPARequest {
	private String destinationPlayer;
	private String playerToMove;
	private String requestedBy;

	public TPARequest(String destinationPlayer, String playerToMove, String requestedBy) {
		this.destinationPlayer = destinationPlayer;
		this.playerToMove = playerToMove;
		this.requestedBy = requestedBy;
	}

	public String getPlayerToMove() {
		return playerToMove;
	}

	public void setPlayerToMove(String playerToMove) {
		this.playerToMove = playerToMove;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getDestinationPlayer() {

		return destinationPlayer;
	}

	public void setDestinationPlayer(String destinationPlayer) {
		this.destinationPlayer = destinationPlayer;
	}
}
