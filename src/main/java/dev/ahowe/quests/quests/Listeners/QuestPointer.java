package dev.ahowe.quests.quests.Listeners;

import dev.ahowe.quests.quests.Quests;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.Random;

public class QuestPointer implements Listener {

	private final Quests plugin;

	public QuestPointer(Quests plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerMoveEvent ev) {
		Player player = ev.getPlayer();
		Location spawn = new Location(player.getWorld(), 0d, 0d, 0d);
		Location location = ev.getTo();

		String message = "Distance: " + trim(1, spawn.distance(location));
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(rainbow(message)));
	}

	public static double trim(final int degree, final double d) {
		String format = "#.#";
		for (int i = 1; i < degree; ++i) {
			format = String.valueOf(format) + "#";
		}
		final DecimalFormat twoDForm = new DecimalFormat(format);
		return Double.valueOf(twoDForm.format(d));
	}

	public static String rainbow(String msg) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < msg.length(); i -= -1) {
			char c = msg.charAt(i);
			ChatColor randomColour = getRandomColor();
			sb.append(randomColour);
			sb.append(c);
		}

		return sb.toString();
	}

	public static ChatColor getRandomColor(){
		ChatColor[] colors = {
			ChatColor.AQUA,
			ChatColor.BLACK,
			ChatColor.BLUE,
			ChatColor.DARK_AQUA,
			ChatColor.DARK_BLUE,
			ChatColor.DARK_GRAY,
			ChatColor.DARK_GREEN,
			ChatColor.DARK_PURPLE,
			ChatColor.DARK_RED,
			ChatColor.GOLD,
			ChatColor.GRAY,
			ChatColor.GREEN,
			ChatColor.LIGHT_PURPLE,
			ChatColor.RED,
			ChatColor.WHITE,
			ChatColor.YELLOW
		};
		int randomColor = new Random().nextInt(colors.length);
		return colors[randomColor];
	}
}
