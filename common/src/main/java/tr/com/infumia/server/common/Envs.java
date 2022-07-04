package tr.com.infumia.server.common;

import java.util.Optional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an interface that contains utility methods for environment variables.
 */
public interface Envs {

  /**
   * gets the variable.
   *
   * @param key the key to get.
   *
   * @return environment variable.
   */
  @Nullable
  static String get(@NotNull final String key) {
    return System.getenv(key);
  }

  /**
   * gets the variable.
   *
   * @param key the key to get.
   * @param def the default to get.
   *
   * @return environment variable.
   */
  @Nullable
  @Contract("_, !null -> !null")
  static String get(@NotNull final String key, @Nullable final String def) {
    return Optional.ofNullable(Envs.get(key)).orElse(def);
  }

  /**
   * gets the variable.
   *
   * @param key the key to get.
   * @param def the default to get.
   *
   * @return environment variable.
   */
  static double getDouble(@NotNull final String key, final double def) {
    final var variable = Envs.get(key);
    if (variable == null) {
      return def;
    }
    try {
      return Double.parseDouble(variable);
    } catch (final Exception e) {
      e.printStackTrace();
    }
    return def;
  }

  /**
   * gets the variable as float.
   *
   * @param key the key to get.
   * @param def the default to get.
   *
   * @return environment variable as float.
   */
  static float getFloat(@NotNull final String key, final float def) {
    final var variable = Envs.get(key);
    if (variable == null) {
      return def;
    }
    try {
      return Float.parseFloat(variable);
    } catch (final Exception e) {
      e.printStackTrace();
    }
    return def;
  }

  /**
   * gets the variable as int.
   *
   * @param key the key to get.
   * @param def the default to get.
   *
   * @return environment variable as int.
   */
  static int getInt(@NotNull final String key, final int def) {
    final var variable = Envs.get(key);
    if (variable == null) {
      return def;
    }
    try {
      return Integer.parseInt(variable);
    } catch (final Exception e) {
      e.printStackTrace();
    }
    return def;
  }

  /**
   * gets the env. or throw.
   *
   * @param key the key to get.
   *
   * @return environment variable.
   */
  @NotNull
  static String getOrThrow(@NotNull final String key) {
    return Exceptions.checkNotNull(
      Envs.get(key),
      "Env. called '%s' not found!",
      key
    );
  }
}
