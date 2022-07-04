package tr.com.infumia.server.queue.module;

import java.util.stream.Collectors;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.server.common.Redis;
import tr.com.infumia.terminable.TerminableConsumer;
import tr.com.infumia.terminable.TerminableModule;

public record QueueModule() implements TerminableModule {
  @Override
  public void setup(@NotNull final TerminableConsumer consumer) {
    final var queueModeKey = "queue-mode";
    final var pool = Redis.connectionPool();
    final Runnable task = () -> {
      final var players = MinecraftServer
        .getConnectionManager()
        .getOnlinePlayers();
      final var uuids = players
        .stream()
        .map(Entity::getUuid)
        .map(Object::toString)
        .collect(Collectors.toSet());
      final var modes = pool
        .acquire()
        .thenCompose(connection -> {
          final var map = connection.sync().hgetall(queueModeKey);
          return pool.release(connection).thenApply(unused -> map);
        })
        .join();
    };
    final var schedule = MinecraftServer
      .getSchedulerManager()
      .scheduleTask(
        task,
        TaskSchedule.immediate(),
        TaskSchedule.seconds(1L),
        ExecutionType.ASYNC
      );
    consumer.bind(schedule::cancel);
  }
}
