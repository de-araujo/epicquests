package dev.ahowe.quests.quests.Listeners;

import dev.ahowe.quests.quests.Quests;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoad implements Listener {
	private final Quests plugin;


	public ChunkLoad(Quests plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChunkLoad(ChunkLoadEvent ev) {
		plugin.getLogger().info("chuk loaded");
		if(ev.isNewChunk()) plugin.getLogger().info("new chuk");
	}
}
