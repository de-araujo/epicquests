package dev.ahowe.quests.quests.Models;

import org.bson.types.ObjectId;

public class Quest {
	public ObjectId _id;
	public String name;
	public String playerId;

	public Quest(String name, String playerId) {
		this.name = name;
		this.playerId = playerId;
	}

	@Deprecated
	public Quest() {}
}
