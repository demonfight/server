package tr.com.infumia.server;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.MathUtils;
import org.jetbrains.annotations.NotNull;

@Slf4j
public final class Server {

  private static final TextColor PINK_COLOR = TextColor.color(209, 72, 212);

  public static void main(@NotNull final String[] args) {
    final var server = MinecraftServer.init();
    final var instanceManager = MinecraftServer.getInstanceManager();
    final var instanceContainer = instanceManager.createInstanceContainer();
    instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 5, Block.GRASS_BLOCK));
    instanceContainer.saveChunksToStorage().join();
    final var globalEventHandler = MinecraftServer.getGlobalEventHandler();
    globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
      event.setSpawningInstance(instanceContainer);
      event.getPlayer().setRespawnPoint(new Pos(0, 42, 0));
    });
    final var lastTick = new AtomicReference<TickMonitor>();
    globalEventHandler.addListener(ServerTickMonitorEvent.class, event -> lastTick.set(event.getTickMonitor()));
    MinecraftServer.getExceptionManager().setExceptionHandler(e -> Server.log.error("Global exception handler", e));
    final var header = Component.newline()
      .append(Component.text("Minestom Arena", Server.PINK_COLOR))
      .append(Component.newline()).append(Component.newline())
      .append(Component.text("RAM USAGE: %ram_usage% MB", NamedTextColor.GRAY)
        .append(Component.newline())
        .append(Component.text("TICK TIME: %tick_time%ms", NamedTextColor.GRAY)))
      .append(Component.newline());
    MinecraftServer.getSchedulerManager().scheduleTask(() -> {
      if (MinecraftServer.getConnectionManager().getOnlinePlayers().isEmpty()) {
        return;
      }
      final var runtime = Runtime.getRuntime();
      final var tickTime = String.valueOf(MathUtils.round(lastTick.get().getTickTime(), 2));
      final var ramUsage = String.valueOf((runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);
      Audiences.players().sendPlayerListHeader(header
        .replaceText(builder -> builder.match("%ram_usage%").replacement(ramUsage))
        .replaceText(builder -> builder.match("%tick_time%").replacement(tickTime)));
    }, TaskSchedule.tick(10), TaskSchedule.tick(10));
    server.start(new InetSocketAddress(25565));
  }
}
