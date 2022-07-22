package com.demonfight.server.minestom;

import com.demonfight.server.common.Envs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an interface that contains Minestome nvironment variables.
 */
public interface MinestomVars {
  /**
   * the brand name.
   */
  @NotNull
  String BRAND_NAME = Envs.get("BRAND_NAME", "DemonFight");

  /**
   * the chunk view distance.
   */
  @NotNull
  String CHUNK_VIEW_DISTANCE = Envs.get("CHUNK_VIEW_DISTANCE", "2");

  /**
   * the compression threshold.
   */
  int COMPRESSION_THRESHOLD = Envs.getInt("COMPRESSION_THRESHOLD", 0);

  /**
   * the player capacity.
   */
  int PLAYER_CAPACITY = Envs.getInt("PLAYER_CAPACITY", 20);

  /**
   * the queue server service name.
   */
  @NotNull
  String QUEUE_SERVER_SERVICE_NAME = Envs.get(
    "QUEUE_SERVER_SERVICE_NAME",
    "gs-queue"
  );

  /**
   * the queue server service namespace.
   */
  @NotNull
  String QUEUE_SERVER_SERVICE_NAMESPACE = Envs.get(
    "QUEUE_SERVER_SERVICE_NAMESPACE",
    "gs-queue"
  );

  /**
   * the texture server service name.
   */
  @NotNull
  String TEXTURE_SERVER_SERVICE_NAME = Envs.get(
    "TEXTURE_SERVER_SERVICE_NAME",
    "gs-texture"
  );

  /**
   * the texture server service namespace.
   */
  @NotNull
  String TEXTURE_SERVER_SERVICE_NAMESPACE = Envs.get(
    "TEXTURE_SERVER_SERVICE_NAMESPACE",
    "gs-texture"
  );

  /**
   * the velocity secret.
   */
  @Nullable
  String VELOCITY_FORWARDING_SECRET = Envs.get("VELOCITY_FORWARDING_SECRET");
}
