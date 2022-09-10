package com.demonfight.server.minestom;

import com.demonfight.server.minestom.annotations.QueueTarget;
import com.demonfight.server.minestom.annotations.ServiceDns;
import com.google.inject.Inject;
import it.unimi.dsi.fastutil.PriorityQueue;
import it.unimi.dsi.fastutil.PriorityQueues;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
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
  final Map<UUID, Integer> positions = new ConcurrentHashMap<>(
    MinestomVars.PLAYER_CAPACITY
  );

  /**
   * the queue.
   */
  final PriorityQueue<UUID> queue = PriorityQueues.synchronize(
    new ObjectArrayFIFOQueue<>(MinestomVars.PLAYER_CAPACITY)
  );

  /**
   * the agones.
   */
  @Inject
  AgonesSdk agones;

  /**
   * the current.
   */
  @Inject
  @ServiceDns
  String current;

  /**
   * the target.
   */
  @Inject
  @QueueTarget
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
