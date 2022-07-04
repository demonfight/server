package tr.com.infumia.server.queue;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.server.minestom.EventFilters;
import tr.com.infumia.server.minestom.EventHandlers;

public interface Events {
  static void init(@NotNull final InstanceContainer container) {
    final var blindness = new Potion(
      PotionEffect.BLINDNESS,
      Byte.MAX_VALUE,
      Integer.MAX_VALUE
    );
    final var invisibility = new Potion(
      PotionEffect.INVISIBILITY,
      Byte.MAX_VALUE,
      Integer.MAX_VALUE
    );
    final var slowness = new Potion(
      PotionEffect.SLOWNESS,
      Byte.MAX_VALUE,
      Integer.MAX_VALUE
    );
    final var spawnPoint = new Pos(0.5d, 63.0d, 0.5d);
    final var login = EventListener
      .builder(PlayerLoginEvent.class)
      .handler(event -> {
        final var player = event.getPlayer();
        player.setFlying(true);
        player.setAutoViewable(false);
        player.setRespawnPoint(spawnPoint);
        event.setSpawningInstance(container);
      })
      .build();
    final var spawn = EventListener
      .builder(PlayerSpawnEvent.class)
      .handler(event -> {
        final var player = event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
        player.setEnableRespawnScreen(false);
        player.addEffect(blindness);
        player.addEffect(invisibility);
        player.addEffect(slowness);
      })
      .build();
    final var move = EventListener
      .builder(PlayerMoveEvent.class)
      .filter(EventFilters.ignoreSameBlock())
      .handler(EventHandlers.cancel())
      .build();
    final var chat = EventListener
      .builder(PlayerChatEvent.class)
      .handler(EventHandlers.cancel())
      .build();
    final var node = EventNode
      .type("player-events", EventFilter.PLAYER)
      .addListener(login)
      .addListener(spawn)
      .addListener(move)
      .addListener(chat);
    MinecraftServer.getGlobalEventHandler().addChild(node);
  }
}
