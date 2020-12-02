package dev.ahowe.quests.quests;

import static com.ea.async.Async.await;
import com.ea.async.Async;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import dev.ahowe.quests.quests.Commands.QuestGiverTestCommand;
import dev.ahowe.quests.quests.Commands.TestCommand;
import dev.ahowe.quests.quests.Listeners.*;
import dev.ahowe.quests.quests.Models.Quest;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.ahowe.quests.quests.Utils.AsyncUtils.toAsyncMulti;
import static dev.ahowe.quests.quests.Utils.AsyncUtils.toAsyncSingle;

public final class Quests extends JavaPlugin {
	private FileConfiguration config;

	public FileConfiguration getQuestConfig() { return config; }

	@Override
	public void onEnable() {
		Async.init();

		getLogger().info("Enabled!");
		saveDefaultConfig();

		this.config = getConfig();

		CompletableFuture<String> example = CompletableFuture.supplyAsync(() -> "Test string");
		getLogger().info(await(example));

		//initDB();
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
		getServer().getPluginManager().registerEvents(new QuestPointer(this), this);
		getServer().getPluginManager().registerEvents(new ChunkGenerated(this), this);
		getServer().getPluginManager().registerEvents(new ChunkLoad(this), this);
		getServer().getPluginManager().registerEvents(new GunFire(this), this);
		getLogger().info("Registered events");
	}

	public void initDB() {
		CodecRegistry registry = CodecRegistries.fromRegistries(
			MongoClientSettings.getDefaultCodecRegistry(),
			CodecRegistries.fromProviders(
				PojoCodecProvider
					.builder()
					.register(Quest.class)
					.build()
			)
		);

		MongoClientSettings settings = MongoClientSettings
			.builder()
			.applyConnectionString(new ConnectionString("mongodb://admin:p0ppydog@localhost:27017/test?authSource=admin&readPreference=primary&appname=Quests&ssl=false"))
			.codecRegistry(registry)
			.build();

		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase db = mongoClient.getDatabase("quests");
		MongoCollection<Quest> collection = db.getCollection("quests", Quest.class);

		await(toAsyncSingle(collection.insertOne(new Quest("First Quest"))::subscribe));
		await(toAsyncSingle(collection.insertOne(new Quest("Second Quest"))::subscribe));
		await(toAsyncSingle(collection.insertOne(new Quest("Third Quest"))::subscribe));

		List<Quest> quests = await(toAsyncMulti(collection.find()::subscribe));

		for(Quest quest : quests) {
			getLogger().info(quest._id.toString());
		}
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled");
	}
}
