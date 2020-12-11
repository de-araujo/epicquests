package dev.ahowe.quests.quests.Listeners;

import com.mongodb.reactivestreams.client.MongoCollection;
import dev.ahowe.quests.quests.Models.Quest;
import dev.ahowe.quests.quests.Quests;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ea.async.Async.await;
import static com.mongodb.client.model.Filters.eq;
import static dev.ahowe.quests.quests.Utils.AsyncUtils.toAsyncMulti;

public class QuestGiverInteractEvent implements Listener {
	private final Quests plugin;
	private final MongoCollection<Quest> questCollection;

	public QuestGiverInteractEvent(Quests plugin) {
		this.plugin = plugin;
		this.questCollection = plugin.getDb().getCollection("quests", Quest.class);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerInteractEntityEvent ev) {
		Entity ent = ev.getRightClicked();
		Player p = ev.getPlayer();
		UUID playerId = p.getUniqueId();
		String playerUuid = playerId.toString();

		if(ent.getType() != EntityType.VILLAGER) return;
		Villager villager = (Villager) ent;

		if(!villager.getCustomName().startsWith("qg")) return;

		List<MerchantRecipe> recipes = new ArrayList<>();

		List<Quest> quests = await(toAsyncMulti(questCollection.find(eq("playerId", playerUuid))::subscribe));

		for(Quest q : quests) {
			Material mat = Material.getMaterial("minecraft:stone");
			ItemStack item = new ItemStack(mat);
			MerchantRecipe recipe = new MerchantRecipe(item, -1);

			List<ItemStack> ingredients = new ArrayList<>();
			Material ingredientMat = Material.getMaterial("minecraft:dirt");
			ItemStack ingredientItem = new ItemStack(ingredientMat);
			ItemMeta ingredientMeta = ingredientItem.getItemMeta();

			ingredientMeta.setDisplayName(q.name);

			ingredientItem.setItemMeta(ingredientMeta);
			ingredients.add(ingredientItem);
			recipe.setIngredients(ingredients);

			recipes.add(recipe);
		}

		villager.setRecipes(recipes);
	}
}
