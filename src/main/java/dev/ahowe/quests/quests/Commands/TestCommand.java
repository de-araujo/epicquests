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

	private Quests quests;
	public TestCommand(Quests quests) {
		this.quests = quests;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) return false;

		Player p = (Player) sender;

		ItemStack item = new ItemStack(Material.PLAYER_HEAD);

		item = NBTEditor.set(item, 1, "custom_model_data");

		p.getInventory().addItem(item);
		
		return true;
	}
}
