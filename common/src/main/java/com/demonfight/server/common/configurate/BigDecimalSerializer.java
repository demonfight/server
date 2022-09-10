package com.demonfight.server.common.configurate;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.function.Predicate;
import org.spongepowered.configurate.serialize.ScalarSerializer;

/**
 * a class that represents {@link BigDecimal} serializers.
 */
public final class BigDecimalSerializer extends ScalarSerializer<BigDecimal> {

  /**
   * the instance.
   */
  public static final BigDecimalSerializer INSTANCE = new BigDecimalSerializer();

  /**
   * ctor.
   */
  private BigDecimalSerializer() {
    super(BigDecimal.class);
  }

  @Override
  public BigDecimal deserialize(final Type type, final Object obj) {
    return new BigDecimal(obj.toString());
  }

  @Override
  protected Object serialize(
    final BigDecimal item,
    final Predicate<Class<?>> typeSupported
  ) {
    return item.toString();
  }
}
