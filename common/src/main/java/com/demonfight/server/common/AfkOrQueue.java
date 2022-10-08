package com.demonfight.server.common;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that contains utility methods for handling afk states.
 */
@UtilityClass
public class AfkOrQueue {

  /**
   * the afk.
   */
  private final String AFK = "afk";

  /**
   * the key.
   */
  private final String KEY = "AfkOrQueue";

  /**
   * the modes.
   */
  private final AsyncLoadingCache<UUID, Mode> MODES = Caffeine
    .newBuilder()
    .expireAfterWrite(Duration.ofSeconds(3L))
    .buildAsync(key -> {
      final var pool = Redis.connectionPool();
      return pool
        .acquire()
        .thenCompose(connection -> {
          final var afkOrQueue = connection
            .sync()
            .hget(AfkOrQueue.KEY, key.toString());
          final var mode = afkOrQueue == null
            ? Mode.NONE
            : afkOrQueue.equals(AfkOrQueue.AFK) ? Mode.AFK : Mode.QUEUE;
          return pool.release(connection).thenApply(unused -> mode);
        })
        .join();
    });

  /**
   * the queue.
   */
  private final String QUEUE = "queue";

  /**
   * gets mode of the player.
   *
   * @param key the key to get.
   *
   * @return mode.
   */
  @NotNull
  public CompletableFuture<Mode> get(@NotNull final UUID key) {
    return AfkOrQueue.MODES.get(key);
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
    return AfkOrQueue.MODES.getAll(keys);
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
    AfkOrQueue.MODES.put(player, CompletableFuture.completedFuture(mode));
    final var pool = Redis.connectionPool();
    return pool
      .acquire()
      .thenCompose(connection -> {
        final var sync = connection.sync();
        switch (mode) {
          case AFK -> sync.hset(
            AfkOrQueue.KEY,
            player.toString(),
            AfkOrQueue.AFK
          );
          case QUEUE -> sync.hset(
            AfkOrQueue.KEY,
            player.toString(),
            AfkOrQueue.QUEUE
          );
          case NONE -> sync.hdel(AfkOrQueue.KEY, player.toString());
        }
        return pool.release(connection);
      });
  }

  /**
   * an enum class that contains queue modes.
   */
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  public enum Mode {
    AFK(AfkOrQueue.AFK),
    QUEUE(AfkOrQueue.QUEUE),
    NONE(null);

    /**
     * the type.
     */
    @Nullable
    String type;
  }
}
