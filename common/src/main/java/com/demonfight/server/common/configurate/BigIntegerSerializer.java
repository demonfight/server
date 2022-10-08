package com.demonfight.server.common.configurate;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.function.Predicate;
import org.spongepowered.configurate.serialize.ScalarSerializer;

/**
 * a class that represents {@link BigInteger} serializers.
 */
public final class BigIntegerSerializer extends ScalarSerializer<BigInteger> {

  /**
   * the instance.
   */
  public static final BigIntegerSerializer INSTANCE = new BigIntegerSerializer();

  /**
   * ctor.
   */
  private BigIntegerSerializer() {
    super(BigInteger.class);
  }

  @Override
  public BigInteger deserialize(final Type type, final Object obj) {
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
