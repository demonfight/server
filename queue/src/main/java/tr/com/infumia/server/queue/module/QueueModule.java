package tr.com.infumia.server.queue.module;

import java.time.Duration;
import java.util.stream.Collectors;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.server.common.Redis;
import tr.com.infumia.terminable.TerminableConsumer;
import tr.com.infumia.terminable.TerminableModule;

public record QueueModule() implements TerminableModule {
  private static final String QUEUE_MODE_KEY = "queue-mode";

  @Override
  public void setup(@NotNull final TerminableConsumer consumer) {
    final var pool = Redis.connectionPool();
    final var schedule = MinecraftServer
      .getSchedulerManager()
      .buildTask(() -> {
        final var players = MinecraftServer
          .getConnectionManager()
          .getOnlinePlayers()
          .stream()
          .map(Entity::getUuid)
          .map(Object::toString)
          .collect(Collectors.toSet());
        final var modes = pool
          .acquire()
          .thenCompose(connection -> {
            final var map = connection
              .sync()
              .hgetall(QueueModule.QUEUE_MODE_KEY);
            return pool.release(connection).thenApply(unused -> map);
          })
          .join();
      })
      .repeat(Duration.ofSeconds(1L))
      .schedule();
    consumer.bind(schedule::cancel);
  }
}
