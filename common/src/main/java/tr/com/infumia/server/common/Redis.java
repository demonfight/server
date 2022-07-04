package tr.com.infumia.server.common;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.support.AsyncConnectionPoolSupport;
import io.lettuce.core.support.BoundedAsyncPool;
import io.lettuce.core.support.BoundedPoolConfig;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tr.com.infumia.terminable.Terminable;

/**
 * a class that contains utility methods for redis.
 */
@UtilityClass
public class Redis {

  /**
   * the client.
   */
  @Nullable
  private RedisClient client;

  /**
   * the connection pool.
   */
  @Nullable
  private BoundedAsyncPool<StatefulRedisConnection<String, String>> connectionPool;

  /**
   * the pub sub pool.
   */
  @Nullable
  private BoundedAsyncPool<StatefulRedisPubSubConnection<byte[], byte[]>> pubSubPool;

  /**
   * the uri.
   */
  @Nullable
  private RedisURI uri;

  /**
   * obtains the redis connection pool.
   *
   * @return redis connection pool.
   */
  @NotNull
  public BoundedAsyncPool<StatefulRedisConnection<String, String>> connectionPool() {
    return Exceptions.checkNotNull(Redis.connectionPool, "init redis first!");
  }

  /**
   * obtains the redis client.
   *
   * @return redis client.
   */
  @NotNull
  public RedisClient get() {
    return Exceptions.checkNotNull(Redis.client, "init redis first!");
  }

  /**
   * initiates the redis.
   */
  @NotNull
  public Terminable init() {
    final var svc = Dns.svc(
      Vars.REDIS_SERVICE_NAME,
      Vars.REDIS_SERVICE_NAMESPACE
    );
    final var builder = RedisURI.Builder.sentinel(
      svc,
      Vars.REDIS_SERVICE_PORT,
      Vars.REDIS_MASTER_ID
    );
    if (Vars.REDIS_USERNAME == null) {
      builder.withPassword(Vars.REDIS_PASSWORD.toCharArray());
    } else {
      builder.withAuthentication(Vars.REDIS_USERNAME, Vars.REDIS_PASSWORD);
    }
    Redis.uri = builder.build();
    Redis.client = RedisClient.create(Redis.uri);
    Redis.connectionPool =
      AsyncConnectionPoolSupport
        .createBoundedObjectPoolAsync(
          () -> Redis.get().connectAsync(StringCodec.UTF8, Redis.uri),
          BoundedPoolConfig.create()
        )
        .toCompletableFuture()
        .join();
    Redis.pubSubPool =
      AsyncConnectionPoolSupport
        .createBoundedObjectPoolAsync(
          () ->
            Redis.get().connectPubSubAsync(ByteArrayCodec.INSTANCE, Redis.uri),
          BoundedPoolConfig.create()
        )
        .toCompletableFuture()
        .join();
    return () -> {
      Redis.connectionPool().close();
      Redis.pubSubPool().close();
      Redis.get().shutdown();
    };
  }

  /**
   * obtains the redis pub sub pool.
   *
   * @return redis pub sub pool.
   */
  @NotNull
  public BoundedAsyncPool<StatefulRedisPubSubConnection<byte[], byte[]>> pubSubPool() {
    return Exceptions.checkNotNull(Redis.pubSubPool, "init redis first!");
  }
}
