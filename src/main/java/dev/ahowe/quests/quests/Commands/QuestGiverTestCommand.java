package dev.ahowe.quests.quests.Commands;

import dev.ahowe.quests.quests.Quests;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestGiverTestCommand implements CommandExecutor {
	private final Quests plugin;

	public QuestGiverTestCommand(Quests plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player p = (Player) sender;

		if(!p.isOp()) return false;

		Villager questGiver = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
		questGiver.setProfession(Villager.Profession.NITWIT);
		questGiver.setInvulnerable(true);
		questGiver.setCustomName("Quest Giver");
		questGiver.setCustomNameVisible(false);

		ItemStack item = new ItemStack(Material.CLOCK);
		ItemMeta itemMeta = item.getItemMeta();

		itemMeta.setDisplayName("Gold Coin");
		itemMeta.setLore(Arrays.asList("Example lore", "sadjsajhdipasjhdpiajhsdpiasjpidajspdiaspidjaspi"));

		item.setItemMeta(itemMeta);

		item = NBTEditor.set(item, 1, "CustomModelData");

		MerchantRecipe trade = new MerchantRecipe(item, 10000);
		ItemStack neededItem = new ItemStack(Material.WOODEN_AXE);

		ItemMeta neededMeta = neededItem.getItemMeta();

		neededMeta.setDisplayName("Lumbers axe");
		neededMeta.setLore(Arrays.asList("Gamer, I've lost my axe pls go find it"));

		neededItem.setItemMeta(neededMeta);

		p.getInventory().addItem(neededItem);

		trade.addIngredient(neededItem);

		List<MerchantRecipe> trades = new ArrayList<>();
		trades.add(trade);
		questGiver.setRecipes(trades);

		return true;
	}
}
