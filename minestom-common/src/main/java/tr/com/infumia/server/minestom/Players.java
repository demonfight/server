package tr.com.infumia.server.minestom;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * an interface that contains utility methods for players.
 */
public interface Players {
  /**
   * gets all the online players' unique ids.
   *
   * @return unique id of the online players.
   */
  @NotNull
  static Collection<UUID> onlinePlayerUniqueIds() {
    return MinecraftServer
      .getConnectionManager()
      .getOnlinePlayers()
      .stream()
      .map(Entity::getUuid)
      .collect(Collectors.toSet());
  }
}
