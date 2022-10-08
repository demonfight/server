package com.demonfight.server.queue;

import com.demonfight.server.common.AfkOrQueue;
import com.demonfight.server.common.FramedText;
import com.demonfight.server.minestom.PlayerServiceQueue;
import com.demonfight.server.minestom.Players;
import com.demonfight.server.minestom.Tasks;
import com.google.inject.Inject;
import java.time.Duration;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.terminable.TerminableConsumer;
import tr.com.infumia.terminable.TerminableModule;

@FieldDefaults(level = AccessLevel.PRIVATE)
final class QueueModule implements TerminableModule {

  final FramedText afkDots = new FramedText(".", "..", "...");

  @Inject
  PlayerServiceQueue queue;

  @Override
  public void setup(@NotNull final TerminableConsumer consumer) {
    Tasks
      .run(
        () -> {
          final var nextAfkDot = this.afkDots.get();
          final var afkTitle = Title.title(
            Component.text(
              nextAfkDot,
              NamedTextColor.WHITE,
              TextDecoration.BOLD
            ),
            Component.empty(),
            Title.Times.times(
              Duration.ofSeconds(1L),
              Duration.ofSeconds(1L),
              Duration.ofSeconds(1L)
            )
          );
          AfkOrQueue
            .getAll(Players.onlinePlayerUniqueIds())
            .join()
            .forEach((uuid, mode) -> {
              final var player = MinecraftServer
                .getConnectionManager()
                .getPlayer(uuid);
              if (player == null) {
                return;
              }
              switch (mode) {
                case AFK -> player.showTitle(afkTitle);
                case QUEUE -> {
                  final var piq = this.queue.position(uuid);
                  final var piqTitle = Title.title(
                    Component.text(
                      QueueLocalizations.positionInQueue(player.getLocale()),
                      NamedTextColor.GOLD
                    ),
                    Component.text(piq, NamedTextColor.YELLOW),
                    Title.Times.times(
                      Duration.ofSeconds(1L),
                      Duration.ofSeconds(1L),
                      Duration.ofSeconds(1L)
                    )
                  );
                  player.showTitle(piqTitle);
                }
              }
            });
        },
        TaskSchedule.immediate(),
        TaskSchedule.seconds(1L),
        ExecutionType.ASYNC
      )
      .bindWith(consumer);
  }
}
