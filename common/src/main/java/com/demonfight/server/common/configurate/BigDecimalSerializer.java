package com.demonfight.server.common.configurate;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.function.Predicate;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public final class BigDecimalSerializer extends ScalarSerializer<BigDecimal> {

  public static final BigDecimalSerializer INSTANCE = new BigDecimalSerializer();

  private BigDecimalSerializer() {
    super(BigDecimal.class);
  }

  @Override
  public BigDecimal deserialize(final Type type, final Object obj)
    throws SerializationException {
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
