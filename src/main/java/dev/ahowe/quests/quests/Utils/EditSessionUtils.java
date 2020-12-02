package dev.ahowe.quests.quests.Utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionBuilder;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import org.bukkit.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EditSessionUtils {
	public static EditSessionBuilder getBuilder() {
		Class<EditSessionBuilder> clazz = EditSessionBuilder.class;
		Constructor<EditSessionBuilder> constructor = (Constructor<EditSessionBuilder>) clazz.getDeclaredConstructors()[0];
		constructor.setAccessible(true);

		try {
			return constructor.newInstance();
		} catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static EditSession getSession(World world) {
		return getBuilder().world(new BukkitWorld(world)).build();
	}

}
