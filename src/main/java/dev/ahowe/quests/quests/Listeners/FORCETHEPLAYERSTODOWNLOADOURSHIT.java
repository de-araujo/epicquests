package dev.ahowe.quests.quests.Listeners;

import dev.ahowe.quests.quests.Quests;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class FORCETHEPLAYERSTODOWNLOADOURSHIT implements Listener {
	private final Quests plugin;
	public FORCETHEPLAYERSTODOWNLOADOURSHIT(Quests plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerResourcePackStatusEvent ev) {
		Player p = ev.getPlayer();
		PlayerResourcePackStatusEvent.Status status = ev.getStatus();
		if(
			status == PlayerResourcePackStatusEvent.Status.DECLINED ||
			status == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD
		) {
			// send them a friendly reminder
			p.kickPlayer("ACCEPT THE RESOURCE PACK DOWNLOAD!!!!!!!!");
			plugin.getLogger().info("Some idot dared to join this servar without eccepting the resorce pack");
		}
	}
}
