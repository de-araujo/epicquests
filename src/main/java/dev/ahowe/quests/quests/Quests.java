package dev.ahowe.quests.quests;

import dev.ahowe.quests.quests.Commands.QuestGiverTestCommand;
import dev.ahowe.quests.quests.Commands.TestCommand;
import dev.ahowe.quests.quests.Listeners.FORCETHEPLAYERSTODOWNLOADOURSHIT;
import dev.ahowe.quests.quests.Listeners.PlayerJoin;
import jdk.jfr.events.ExceptionThrownEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Quests extends JavaPlugin {
	private FileConfiguration config;

	public FileConfiguration getQuestConfig() { return config; }

	@Override
	public void onEnable() {
		getLogger().info("Enabled!");
		saveDefaultConfig();

		this.config = getConfig();
		initEvents();
		initCommands();
	}

	public void initCommands() {
		getCommand("test").setExecutor(new TestCommand(this));
		getCommand("spawnqg").setExecutor(new QuestGiverTestCommand(this));
	}

	public void initEvents() {
		getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
		getServer().getPluginManager().registerEvents(new FORCETHEPLAYERSTODOWNLOADOURSHIT(this), this);
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled");
	}
}
