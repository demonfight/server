package tr.com.infumia.server.queue;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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
import tr.com.infumia.server.minestom.Events;
import tr.com.infumia.terminable.TerminableConsumer;
import tr.com.infumia.terminable.TerminableModule;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class EventModule implements TerminableModule {

  @Inject
  @NotNull
  @Named("defaultContainer")
  InstanceContainer container;

  @Override
  public void setup(@NotNull final TerminableConsumer consumer) {
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
        event.setSpawningInstance(this.container);
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
    Events.register(node).bindWith(consumer);
  }
}
