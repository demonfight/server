package tr.com.infumia.server;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.block.Block;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.MathUtils;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

@Slf4j
public final class Server {

  public static void main(@NotNull final String[] args) {
    System.setProperty("minestom.terminal.disabled", "true");
    final var server = MinecraftServer.init();
    MojangAuth.init();
    MinecraftServer.setBrandName("Infumia");
    final var instanceContainer = MinecraftServer
      .getInstanceManager()
      .createInstanceContainer();
    instanceContainer.setGenerator(unit -> {
      final var modifier = unit.modifier();
      modifier.fillHeight(0, 1, Block.BEDROCK);
      modifier.fillHeight(1, 2, Block.GRASS_BLOCK);
    });
    final var eventHandler = MinecraftServer.getGlobalEventHandler();
    final var lastTick = new AtomicReference<TickMonitor>();
    eventHandler
      .addListener(
        PlayerLoginEvent.class,
        event -> {
          event.setSpawningInstance(instanceContainer);
          event.getPlayer().setRespawnPoint(new Pos(0, 2, 0));
          event.getPlayer().setGameMode(GameMode.CREATIVE);
        }
      )
      .addListener(
        ServerTickMonitorEvent.class,
        event -> lastTick.set(event.getTickMonitor())
      )
      .addListener(
        ItemDropEvent.class,
        event -> {
          final var itemEntity = new ItemEntity(event.getItemStack());
          itemEntity.setPickupDelay(40, TimeUnit.SERVER_TICK);
          itemEntity.setInstance(
            event.getInstance(),
            event.getPlayer().getPosition().add(0, 1.5, 0)
          );
          itemEntity.setVelocity(
            event.getPlayer().getPosition().direction().mul(6)
          );
        }
      );
    MinecraftServer
      .getExceptionManager()
      .setExceptionHandler(e -> Server.log.error("Global exception handler", e)
      );
    final var header = Component
      .newline()
      .append(Component.text("Infumia", NamedTextColor.LIGHT_PURPLE))
      .append(Component.newline())
      .append(Component.text("RAM USAGE: %ram_usage% MB", NamedTextColor.GRAY))
      .append(Component.newline())
      .append(Component.text("TICK TIME: %tick_time%ms", NamedTextColor.GRAY))
      .append(Component.newline())
      .append(Component.text("PING: %ping%ms", NamedTextColor.GRAY))
      .append(Component.newline());
    final var schedulerManager = MinecraftServer.getSchedulerManager();
    schedulerManager.scheduleTask(
      () -> {
        final var players = MinecraftServer
          .getConnectionManager()
          .getOnlinePlayers();
        if (players.isEmpty()) {
          return;
        }
        final var runtime = Runtime.getRuntime();
        final var ramUsage = String.valueOf(
          (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024
        );
        final var tickTime = String.valueOf(
          MathUtils.round(lastTick.get().getTickTime(), 2)
        );
        for (final var player : players) {
          final var latency = String.valueOf(player.getLatency());
          player.sendPlayerListHeader(
            header
              .replaceText(builder ->
                builder.match("%ram_usage%").replacement(ramUsage)
              )
              .replaceText(builder ->
                builder.match("%tick_time%").replacement(tickTime)
              )
              .replaceText(builder ->
                builder.match("%ping%").replacement(latency)
              )
          );
        }
      },
      TaskSchedule.tick(5),
      TaskSchedule.tick(5)
    );
    server.start(new InetSocketAddress(25565));
  }
}
