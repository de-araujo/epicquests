package dev.ahowe.quests.quests.Utils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AsyncUtils {
	public static <T> CompletableFuture<T> toAsyncSingle(Consumer<Subscriber<T>> subFunction) {
		final CompletableFuture<T> future = new CompletableFuture<T>();

		Subscriber<T> subscriber = new Subscriber<T>() {
			@Override
			public void onSubscribe(Subscription s) {
				s.request(1);
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
			public void onComplete() { }
		};

		subFunction.accept(subscriber);

		return future;
	}

	public static <T> CompletableFuture<List<T>> toAsyncMulti(Consumer<Subscriber<T>> subFunction) {
		final CompletableFuture<List<T>> future = new CompletableFuture<>();

		Subscriber<T> subscriber = new Subscriber<T>() {
			public List<T> events = new ArrayList<>();

			@Override
			public void onSubscribe(Subscription s) {
				s.request(Integer.MAX_VALUE);
			}

			@Override
			public void onNext(T result) {
				events.add(result);
			}

			@Override
			public void onError(Throwable t) {
				future.completeExceptionally(t);
			}

			@Override
			public void onComplete() {
				future.complete(events);
			}
		};

		subFunction.accept(subscriber);

		return future;
	}
}
