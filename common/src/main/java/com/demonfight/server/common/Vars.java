package com.demonfight.server.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an interface that contains environment variables.
 */
public interface Vars {
  /**
   * the pod dns format.
   */
  @NotNull
  String POD_ADDRESS_FORMAT = Envs.get(
    "POD_ADDRESS_FORMAT",
    "%s.%s.pod.cluster.local:%s"
  );

  /**
   * the redis master id.
   */
  @NotNull
  String REDIS_MASTER_ID = Envs.getOrThrow("REDIS_MASTER_ID");

  /**
   * the redis password.
   */
  @NotNull
  String REDIS_PASSWORD = Envs.getOrThrow("REDIS_PASSWORD");

  /**
   * the redis service name.
   */
  @NotNull
  String REDIS_SERVICE_NAME = Envs.get("REDIS_SERVICE_NAME", "db-redis");

  /**
   * the redis service name.
   */
  @NotNull
  String REDIS_SERVICE_NAMESPACE = Envs.get(
    "REDIS_SERVICE_NAMESPACE",
    "db-redis"
  );

  /**
   * the redis service port.
   */
  int REDIS_SERVICE_PORT = Envs.getInt("REDIS_SERVICE_PORT", 26379);

  /**
   * the redis username.
   */
  @Nullable
  String REDIS_USERNAME = Envs.get("REDIS_USERNAME");

  /**
   * the server port.
   */
  int SERVER_PORT = Envs.getInt("SERVER_PORT", 25565);

  /**
   * the svc dns format.
   */
  @NotNull
  String SERVICE_ADDRESS_FORMAT = Envs.get(
    "SERVICE_ADDRESS_FORMAT",
    "%s.%s.svc.cluster.local:%s"
  );
}
