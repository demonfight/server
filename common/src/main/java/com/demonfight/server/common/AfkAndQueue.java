package com.demonfight.server.common;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * a class that contains utility methods for handling afk states.
 */
@UtilityClass
public class AfkAndQueue {

  /**
   * the key.
   */
  private final String KEY = "afk";

  /**
   * the modes.
   */
  private final AsyncLoadingCache<UUID, Mode> MODES = Caffeine
    .newBuilder()
    .expireAfterWrite(Duration.ofSeconds(3L))
    .buildAsync(key -> {
      final var pool = Redis.connectionPool();
      final var isAfk = pool
        .acquire()
        .thenCompose(connection -> {
          final var has = connection
            .sync()
            .hexists(AfkAndQueue.KEY, key.toString());
          return pool.release(connection).thenApply(unused -> has);
        })
        .join();
      return isAfk ? Mode.AFK : Mode.QUEUE;
    });

  /**
   * gets mode of the player.
   *
   * @param player the player to get.
   *
   * @return mode.
   */
  @NotNull
  public CompletableFuture<Mode> get(@NotNull final UUID player) {
    return AfkAndQueue.MODES.get(player);
  }

  /**
   * gets all the modes.
   *
   * @return all the modes.
   */
  @NotNull
  public CompletableFuture<Map<UUID, Mode>> getAll(
    @NotNull final Iterable<UUID> keys
  ) {
    return AfkAndQueue.MODES.getAll(keys);
  }

  /**
   * sets mode of the player.
   *
   * @param player the player to set.
   * @param mode the mode to set.
   */
  @NotNull
  public CompletableFuture<Void> set(
    @NotNull final UUID player,
    @NotNull final Mode mode
  ) {
    AfkAndQueue.MODES.put(player, CompletableFuture.completedFuture(mode));
    final var pool = Redis.connectionPool();
    return pool
      .acquire()
      .thenCompose(connection -> {
        connection.sync().hset(AfkAndQueue.KEY, player.toString(), "afk");
        return pool.release(connection);
      });
  }

  /**
   * an enum class that contains queue modes.
   */
  public enum Mode {
    AFK,
    QUEUE,
  }
}
