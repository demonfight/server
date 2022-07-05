package tr.com.infumia.server.common;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.agones4j.AgonesSdk;
import tr.com.infumia.terminable.Terminable;

/**
 * an interface to determine service queues.
 */
public interface PlayerServiceQueue extends Terminable {
  /**
   * initiates the queue for services.
   *
   * @param agones the agones to initiate.
   * @param current the current service dns to initiate.
   * @param target the target service dns to initiate.
   */
  @NotNull
  static Impl init(
    @NotNull final AgonesSdk agones,
    @NotNull final String current,
    @NotNull final String target
  ) {
    return new Impl(agones, current, target);
  }

  /**
   * position of the uuid in the queue.
   *
   * @param uuid the uuid to get position.
   */
  int position(@NotNull UUID uuid);

  /**
   * a simple implementation of {@link PlayerServiceQueue}.
   */
  @RequiredArgsConstructor
  @Accessors(fluent = true)
  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  final class Impl implements PlayerServiceQueue {

    /**
     * the agones.
     */
    @NotNull
    AgonesSdk agones;

    /**
     * the current.
     */
    @NotNull
    String current;

    /**
     * the positions.
     */
    Map<UUID, Integer> positions = new ConcurrentHashMap<>();

    /**
     * the target.
     */
    @NotNull
    String target;

    @Override
    public void close() {}

    @Override
    public int position(@NotNull final UUID uuid) {
      return this.positions.getOrDefault(uuid, -1);
    }
  }
}
