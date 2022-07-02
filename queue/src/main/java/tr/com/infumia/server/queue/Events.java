package tr.com.infumia.server.queue;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

public interface Events {

  static void init(
    @NotNull final InstanceContainer container
  ) {
    final var spawnPoint = new Pos(0.5d, 63.0d, 0.5d);
    final var eventHandler = MinecraftServer.getGlobalEventHandler();
    eventHandler.addListener(PlayerLoginEvent.class, event -> {
      final var player = event.getPlayer();
      player.setFlying(true);
      player.setAutoViewable(false);
      player.setRespawnPoint(spawnPoint);
      event.setSpawningInstance(container);
    });
    eventHandler.addListener(PlayerSpawnEvent.class, event -> {
      final var player = event.getPlayer();
      player.setGameMode(GameMode.ADVENTURE);
      player.setEnableRespawnScreen(false);
      player.addEffect(new Potion(PotionEffect.BLINDNESS, Byte.MAX_VALUE, Integer.MAX_VALUE));
      player.addEffect(new Potion(PotionEffect.INVISIBILITY, Byte.MAX_VALUE, Integer.MAX_VALUE));
      player.addEffect(new Potion(PotionEffect.SLOWNESS, Byte.MAX_VALUE, Integer.MAX_VALUE));
    });
    eventHandler.addListener(PlayerMoveEvent.class, event -> {
      if (!event.getNewPosition().sameBlock(event.getPlayer().getPosition())) {
        event.setCancelled(true);
      }
    });
    eventHandler.addListener(PlayerChatEvent.class, event -> event.setCancelled(true));
  }
}
