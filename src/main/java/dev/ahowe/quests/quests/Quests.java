package dev.ahowe.quests.quests;

import com.ea.async.Async;
import com.ea.async.Async.*;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.internal.async.SingleResultCallback;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.internal.MongoDatabaseImpl;
import dev.ahowe.quests.quests.Commands.QuestGiverTestCommand;
import dev.ahowe.quests.quests.Commands.TestCommand;
import dev.ahowe.quests.quests.Listeners.FORCETHEPLAYERSTODOWNLOADOURSHIT;
import dev.ahowe.quests.quests.Listeners.PlayerJoin;
import dev.ahowe.quests.quests.Listeners.QuestPointer;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static dev.ahowe.quests.quests.Utils.AsyncUtils.toAsyncMulti;
import static dev.ahowe.quests.quests.Utils.AsyncUtils.toAsyncSingle;

public final class Quests extends JavaPlugin {
	private FileConfiguration config;

	public FileConfiguration getQuestConfig() { return config; }

	@Override
	public void onEnable() {
		getLogger().info("Enabled!");
		saveDefaultConfig();

		this.config = getConfig();

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
	}



	public void initDB() {
		MongoClient mongoClient = MongoClients.create("mongodb://admin:p0ppydog@localhost:27017/test?authSource=admin&readPreference=primary&appname=Quests&ssl=false");
		MongoDatabase db = mongoClient.getDatabase("quests");
		MongoCollection<Document> collection = db.getCollection("quests");

		InsertOneResult r = Async.await(toAsyncSingle(collection.insertOne(new Document("a", "b"))::subscribe));
		getLogger().info(r.getInsertedId().toString());

		List<Document> docs = Async.await(toAsyncMulti(collection.find()::subscribe));

		for(Document doc : docs) {
			getLogger().info(doc.toJson());
			getLogger().info("\n");
		}
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled");
	}
}
