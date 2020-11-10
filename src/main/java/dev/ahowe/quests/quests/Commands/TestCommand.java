package dev.ahowe.quests.quests.Commands;

import dev.ahowe.quests.quests.Quests;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TestCommand implements CommandExecutor {

	private final Quests plugin;
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
