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
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import javafx.scene.SubScene;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

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


	public <T> CompletableFuture<T> toAsync(Consumer<Subscriber<T>> subFunction) {
		final CompletableFuture<T> future = new CompletableFuture<T>();

		Subscriber<T> subscriber = new Subscriber<T>() {
			@Override
			public void onSubscribe(Subscription s) {

			}

			@Override
			public void onNext(T result) {
				future.complete(result);
			}

			@Override
			public void onError(Throwable t) {
				future.completeExceptionally(t);
			}

			@Override
			public void onComplete() {

			}
		};

		subFunction.accept(subscriber);

		return future;
	}
	public void initDB() {
		MongoClient mongoClient = MongoClients.create("mongodb://localhost");
		MongoDatabase db = mongoClient.getDatabase("quests");
		MongoCollection<Document> collection = db.getCollection("quests");

		//InsertOneResult r = Async.await(toAsync(collection.insertOne(new Document("a", "b"))::subscribe));

		//this.getLogger().info(r.getInsertedId().toString());

		collection.insertOne(new Document("a", "b")).subscribe(new Subscriber<InsertOneResult>() {
			@Override
			public void onSubscribe(Subscription s) {
				getLogger().info("onSubscribe");
			}

			@Override
			public void onNext(InsertOneResult insertOneResult) {
				getLogger().info("onNext");
			}

			@Override
			public void onError(Throwable t) {
				getLogger().info("onError");
			}

			@Override
			public void onComplete() {
				getLogger().info("onComplete");
			}
		});

	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled");
	}
}
