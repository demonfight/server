package com.demonfight.server.minestom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.terminable.Terminable;

/**
 * an interface that contains utility methods for events.
 */
public interface Events {
  /**
   * registers the event node.
   *
   * @param node the node to register.
   *
   * @return terminable registration.
   */
  @NotNull
  static Terminable register(@NotNull final EventNode<? extends Event> node) {
    MinecraftServer.getGlobalEventHandler().addChild(node);
    return () -> MinecraftServer.getGlobalEventHandler().removeChild(node);
  }
}
