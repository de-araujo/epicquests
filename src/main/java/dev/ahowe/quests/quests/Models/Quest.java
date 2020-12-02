package dev.ahowe.quests.quests.Models;

import org.bson.types.ObjectId;

public class Quest {
	public ObjectId _id;
	public String name;

	public Quest(String name) {
		this.name = name;
	}

	@Deprecated
	public Quest() {}
}
