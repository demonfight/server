package com.demonfight.server.common.configurate;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.function.Predicate;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

/**
 * a class that represents {@link Locale} serializers.
 */
public final class LocaleSerializer extends ScalarSerializer<Locale> {

  /**
   * the instance.
   */
  public static final LocaleSerializer INSTANCE = new LocaleSerializer();

  /**
   * ctor.
   */
  private LocaleSerializer() {
    super(Locale.class);
  }

  @Override
  public Locale deserialize(final Type type, final Object obj)
    throws SerializationException {
    final var trim = obj.toString().trim();
    if (trim.isEmpty()) {
      return Locale.ROOT;
    }
    final var strings = trim.split("_");
    if (trim.contains("_") && strings.length != 2) {
      return Locale.ROOT;
    }
    if (strings.length != 2) {
      throw new SerializationException(
        "The node has to be in a X:Y format to deserialize!"
      );
    }
    return new Locale(strings[0], strings[1]);
  }

  @Override
  protected Object serialize(
    final Locale item,
    final Predicate<Class<?>> typeSupported
  ) {
    return item.toString();
  }
}
