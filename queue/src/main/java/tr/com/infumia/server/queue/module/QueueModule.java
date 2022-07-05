package tr.com.infumia.server.queue.module;

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
import tr.com.infumia.agones4j.AgonesSdk;
import tr.com.infumia.server.common.AfkAndQueue;
import tr.com.infumia.server.common.Dns;
import tr.com.infumia.server.common.FramedText;
import tr.com.infumia.server.common.ServiceQueue;
import tr.com.infumia.server.common.Vars;
import tr.com.infumia.server.minestom.Players;
import tr.com.infumia.server.queue.Localizations;
import tr.com.infumia.terminable.TerminableConsumer;
import tr.com.infumia.terminable.TerminableModule;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class QueueModule implements TerminableModule {

  FramedText afkDots = new FramedText(".", "..", "...");

  @NotNull
  ServiceQueue serviceQueue;

  @Inject
  public QueueModule(
    @NotNull final AgonesSdk agones,
    @NotNull final TerminableConsumer consumer
  ) {
    this.serviceQueue =
      ServiceQueue.init(
        agones,
        Dns.svc(
          Vars.QUEUE_SERVER_SERVICE_NAME,
          Vars.QUEUE_SERVER_SERVICE_NAMESPACE
        ),
        Dns.svc(
          Vars.TEXTURE_SERVER_SERVICE_NAME,
          Vars.TEXTURE_SERVER_SERVICE_NAMESPACE
        )
      );
    this.bindModuleWith(consumer);
  }

  @Override
  public void setup(@NotNull final TerminableConsumer consumer) {
    final var schedule = MinecraftServer
      .getSchedulerManager()
      .scheduleTask(
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
          AfkAndQueue
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
                  final var piq = this.serviceQueue.position(uuid);
                  final var piqTitle = Title.title(
                    Component.text(
                      Localizations.positionInQueue(player.getLocale()),
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
      );
    consumer.bind(schedule::cancel);
  }
}
