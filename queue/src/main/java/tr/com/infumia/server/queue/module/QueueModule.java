package tr.com.infumia.server.queue.module;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.server.common.AfkAndQueue;
import tr.com.infumia.server.common.CustomProgressText;
import tr.com.infumia.server.queue.Localizations;
import tr.com.infumia.terminable.TerminableConsumer;
import tr.com.infumia.terminable.TerminableModule;

public record QueueModule() implements TerminableModule {
  @Override
  public void setup(@NotNull final TerminableConsumer consumer) {
    final var afkDots = new CustomProgressText(".", "..", "...");
    final Consumer<Map<UUID, AfkAndQueue.Mode>> modesConsumer = map -> {
      map.forEach((uuid, mode) -> {
        final var player = MinecraftServer
          .getConnectionManager()
          .getPlayer(uuid);
        if (player == null) {
          return;
        }
        final var positionInQueue = Localizations.positionInQueue(
          player.getLocale()
        );
        final var isAfk = mode == AfkAndQueue.Mode.AFK;
        final var title = isAfk
          ? Component.text(
            afkDots.get(),
            NamedTextColor.WHITE,
            TextDecoration.BOLD
          )
          : Component.text(positionInQueue, NamedTextColor.GOLD);
        final var subTitle = isAfk
          ? Component.text(100, NamedTextColor.YELLOW)
          : Component.empty();
        player.showTitle(
          Title.title(
            title,
            subTitle,
            Title.Times.times(
              Duration.ofSeconds(1L),
              Duration.ofSeconds(1L),
              Duration.ofSeconds(1L)
            )
          )
        );
      });
    };
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
        .collect(Collectors.toSet());
      final var modes = AfkAndQueue.getAll(uuids).join();
      MinecraftServer
        .getSchedulerManager()
        .scheduleNextProcess(() -> modesConsumer.accept(modes));
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
