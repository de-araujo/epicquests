package dev.ahowe.quests.quests;

import static com.ea.async.Async.await;
import com.ea.async.Async;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.result.InsertManyResult;
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
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.ahowe.quests.quests.Utils.AsyncUtils.toAsyncMulti;
import static dev.ahowe.quests.quests.Utils.AsyncUtils.toAsyncSingle;

public final class Quests extends JavaPlugin {
	private FileConfiguration config;

	private MongoDatabase db;
	private MongoClient dbClient;

	public MongoDatabase getDb() {
		return db;
	}

	public FileConfiguration getQuestConfig() { return config; }

	@Override
	public void onEnable() {
		Async.init();

		getLogger().info("Enabled!");
		saveDefaultConfig();

		this.config = getConfig();

		CompletableFuture<String> example = CompletableFuture.supplyAsync(() -> "Test string");
		getLogger().info(await(example));

		initDB();
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

		this.dbClient = MongoClients.create(settings);
		this.db = this.dbClient.getDatabase("quests");

		MongoCollection<Quest> collection = this.db.getCollection("quests", Quest.class);

		List<Quest> quests = new ArrayList<>();

		quests.add(new Quest("Example Quest", "34c3d4d9-e9af-457c-99d4-e80fd13ebc7e"));
		quests.add(new Quest("Test Quest", "34c3d4d9-e9af-457c-99d4-e80fd13ebc7e"));
		quests.add(new Quest("Epic Quest", "34c3d4d9-e9af-457c-99d4-e80fd13ebc7e"));

		await(toAsyncMulti(collection.insertMany(quests)::subscribe));

//		await(toAsyncSingle(collection.insertOne(new Quest("First Quest"))::subscribe));
//		await(toAsyncSingle(collection.insertOne(new Quest("Second Quest"))::subscribe));
//		await(toAsyncSingle(collection.insertOne(new Quest("Third Quest"))::subscribe));
//
//		List<Quest> quests = await(toAsyncMulti(collection.find()::subscribe));
//
//		for(Quest quest : quests) {
//			getLogger().info(quest._id.toString());
//		}
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled");
	}
}
