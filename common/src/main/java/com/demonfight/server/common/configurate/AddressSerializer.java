package com.demonfight.server.common.configurate;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.function.Predicate;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public final class AddressSerializer
  extends ScalarSerializer<InetSocketAddress> {

  public static final AddressSerializer INSTANCE = new AddressSerializer();

  private AddressSerializer() {
    super(InetSocketAddress.class);
  }

  @Override
  public InetSocketAddress deserialize(final Type type, final Object obj)
    throws SerializationException {
    final var address = obj.toString();
    final var trim = address.trim();
    final var split = trim.split(":");
    if (split.length != 2) {
      throw new SerializationException(
        "The node has to be in a X:Y format to deserialize!"
      );
    }
    return new InetSocketAddress(
      split[0],
      new BigDecimal(split[1]).intValueExact()
    );
  }

  @Override
  protected Object serialize(
    final InetSocketAddress item,
    final Predicate<Class<?>> typeSupported
  ) {
    return item.getHostName() + ":" + item.getPort();
  }
}
