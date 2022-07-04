package tr.com.infumia.server.queue.module;

import java.util.Objects;
import java.util.stream.Collectors;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.server.common.Afk;
import tr.com.infumia.terminable.TerminableConsumer;
import tr.com.infumia.terminable.TerminableModule;

public record QueueModule() implements TerminableModule {
  @Override
  public void setup(@NotNull final TerminableConsumer consumer) {
    final Runnable task = () -> {
      final var players = MinecraftServer
        .getConnectionManager()
        .getOnlinePlayers();
      if (players.isEmpty()) {
        return;
      }
      final var uuids = players
        .stream()
        .map(Entity::getUuid)
        .map(Objects::toString)
        .collect(Collectors.toSet());
      final var modes = Afk.getAll(uuids);
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
