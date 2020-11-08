package dev.ahowe.quests.quests.Listeners;

import dev.ahowe.quests.quests.Quests;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
	private final Quests plugin;

	public PlayerJoin(Quests plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerJoinEvent ev) {
		Player p = ev.getPlayer();
		String rpUrl = plugin.getQuestConfig().getString("rp");

		if(rpUrl == null) return;

		p.setResourcePack(rpUrl);
	}
}
