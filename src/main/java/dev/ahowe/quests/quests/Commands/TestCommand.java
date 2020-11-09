package dev.ahowe.quests.quests.Commands;

import dev.ahowe.quests.quests.Quests;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TestCommand implements CommandExecutor {

	private Quests plugin;
	public TestCommand(Quests plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.isOp()) return false;
		if(!(sender instanceof Player)) return false;

		Player player = (Player) sender;

		ItemStack item = new ItemStack(Material.CLOCK);

		item = NBTEditor.set(item, 1, "CustomModelData");

		player.getInventory().addItem(item);
		
		return true;
	}
}
