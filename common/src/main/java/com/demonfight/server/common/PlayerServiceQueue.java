package com.demonfight.server.common;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.agones4j.AgonesSdk;

/**
 * a class that represents player service queues.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class PlayerServiceQueue {

  /**
   * the positions.
   */
  final Map<UUID, Integer> positions = new ConcurrentHashMap<>();

  /**
   * the agones.
   */
  @Inject
  AgonesSdk agones;

  /**
   * the current.
   */
  @Inject
  @Named("serviceDns")
  String current;

  /**
   * the target.
   */
  @Inject
  @Named("queueTarget")
  String target;

  /**
   * position of the uuid in the queue.
   *
   * @param uuid the uuid to get position.
   *
   * @return position in the queue.
   */
  public int position(@NotNull final UUID uuid) {
    return this.positions.getOrDefault(uuid, -1);
  }
}
