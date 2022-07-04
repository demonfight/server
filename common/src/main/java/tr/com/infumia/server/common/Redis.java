package tr.com.infumia.server.common;

import io.lettuce.core.ConnectionFuture;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
   * the uri.
   */
  @Nullable
  private RedisURI uri;

  /**
   * connects to database as sync.
   *
   * @return sync connection.
   */
  @NotNull
  public StatefulRedisConnection<String, String> connect() {
    return Redis.get().connect();
  }

  /**
   * connects to database as async.
   *
   * @return async connection.
   */
  @NotNull
  public ConnectionFuture<StatefulRedisConnection<byte[], byte[]>> connectAsync() {
    return Redis.get().connectAsync(ByteArrayCodec.INSTANCE, Redis.uri);
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
  public void init() {
    final var svc = Dns.svc(Vars.REDIS_SERVICE_NAME, Vars.REDIS_SERVICE_NAMESPACE);
    final var builder = RedisURI.Builder.sentinel(svc, Vars.REDIS_SERVICE_PORT, Vars.REDIS_MASTER_ID);
    if (Vars.REDIS_USERNAME == null) {
      builder.withPassword(Vars.REDIS_PASSWORD.toCharArray());
    } else {
      builder.withAuthentication(Vars.REDIS_USERNAME, Vars.REDIS_PASSWORD);
    }
    Redis.uri = builder.build();
    Redis.client = RedisClient.create(Redis.uri);
  }

  /**
   * connects to the pub sub.
   *
   * @return pub sub connection.
   */
  @NotNull
  public ConnectionFuture<StatefulRedisPubSubConnection<byte[], byte[]>> pubSubAsync() {
    return Redis.get().connectPubSubAsync(ByteArrayCodec.INSTANCE, Redis.uri);
  }

  /**
   * connects to the pub sub.
   *
   * @return pub sub connection.
   */
  @NotNull
  public StatefulRedisPubSubConnection<byte[], byte[]> pubSubSync() {
    return Redis.get().connectPubSub(ByteArrayCodec.INSTANCE);
  }
}
