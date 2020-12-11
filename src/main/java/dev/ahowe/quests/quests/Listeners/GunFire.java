package dev.ahowe.quests.quests.Listeners;

import dev.ahowe.quests.quests.Quests;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Set;

public class GunFire implements Listener {
	private final Quests plugin;

	public GunFire(Quests plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerInteractEvent ev) {
		Player player = ev.getPlayer();
		if(ev.getAction() != Action.RIGHT_CLICK_AIR) return;

		ItemStack item = ev.getItem();

		if(!NBTEditor.getKeys(item).contains("CustomModelData")) return;

		int model = NBTEditor.getInt(item, "CustomModelData");

		if(model != 2) return;

//		Entity lookingAt = getNearestEntityInSight(player, 50);
//		if(lookingAt == null) return;
//		lookingAt.remove();

		SpectralArrow arrow = (SpectralArrow) player.getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.SPECTRAL_ARROW);
		arrow.setVelocity(player.getLocation().getDirection().multiply(3));
		arrow.setShooter(player);
	}

	public static Entity getNearestEntityInSight(Player player, int range) {
		ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
		ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight( (Set<Material>) null, range);
		ArrayList<Location> sight = new ArrayList<Location>();
		for (int i = 0;i<sightBlock.size();i++)
			sight.add(sightBlock.get(i).getLocation());
		for (int i = 0;i<sight.size();i++) {
			for (int k = 0;k<entities.size();k++) {
				if (Math.abs(entities.get(k).getLocation().getX()-sight.get(i).getX())<1.3) {
					if (Math.abs(entities.get(k).getLocation().getY()-sight.get(i).getY())<1.5) {
						if (Math.abs(entities.get(k).getLocation().getZ()-sight.get(i).getZ())<1.3) {
							return entities.get(k);
						}
					}
				}
			}
		}
		return null; //Return null/nothing if no entity was found
	}
}
