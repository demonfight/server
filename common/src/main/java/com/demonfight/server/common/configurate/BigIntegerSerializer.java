package com.demonfight.server.common.configurate;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.function.Predicate;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public final class BigIntegerSerializer extends ScalarSerializer<BigInteger> {

  public static final BigIntegerSerializer INSTANCE = new BigIntegerSerializer();

  private BigIntegerSerializer() {
    super(BigInteger.class);
  }

  @Override
  public BigInteger deserialize(final Type type, final Object obj)
    throws SerializationException {
    return new BigInteger(obj.toString());
  }

  @Override
  protected Object serialize(
    final BigInteger item,
    final Predicate<Class<?>> typeSupported
  ) {
    return item.toString();
  }
}
