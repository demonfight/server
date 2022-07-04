package tr.com.infumia.server.common;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

/**
 * an interface that contains utility methods for handling afk states.
 */
public interface Afk {
  /**
   * the key.
   */
  String KEY = "afk";

  /**
   * the modes.
   */
  AsyncLoadingCache<String, Mode> MODES = Caffeine
    .newBuilder()
    .expireAfterAccess(Duration.ofSeconds(2L))
    .buildAsync(key -> {
      final var pool = Redis.connectionPool();
      final var isAfk = pool
        .acquire()
        .thenCompose(connection -> {
          final var has = connection.sync().hexists(Afk.KEY, key);
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
  static CompletableFuture<Mode> get(@NotNull final String player) {
    return Afk.MODES.get(player);
  }

  /**
   * gets all the modes.
   *
   * @return all the modes.
   */
  @NotNull
  static CompletableFuture<Map<String, Mode>> getAll(
    @NotNull final Iterable<String> keys
  ) {
    return Afk.MODES.getAll(keys);
  }

  /**
   * sets mode of the player.
   *
   * @param player the player to set.
   * @param mode the mode to set.
   */
  @NotNull
  static CompletableFuture<Void> set(
    @NotNull final String player,
    @NotNull final Mode mode
  ) {
    Afk.MODES.put(player, CompletableFuture.completedFuture(mode));
    final var pool = Redis.connectionPool();
    return pool
      .acquire()
      .thenCompose(connection -> {
        connection.sync().hset(Afk.KEY, player, "afk");
        return pool.release(connection);
      });
  }

  /**
   * an enum class that contains queue modes.
   */
  enum Mode {
    AFK,
    QUEUE,
  }
}
