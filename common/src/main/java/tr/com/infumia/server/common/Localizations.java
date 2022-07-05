package tr.com.infumia.server.common;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.Locale;
import java.util.ResourceBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an interface that contains utility methods for localizations.
 */
public interface Localizations {
  /**
   * the cache.
   */
  LoadingCache<String, LoadingCache<Locale, ResourceBundle>> CACHE = Caffeine
    .newBuilder()
    .expireAfterWrite(Duration.ofMinutes(30L))
    .build(Localizations::create);

  /**
   * gets the value.
   *
   * @param bundle the bundle to get.
   * @param locale the locale to get.
   * @param key the key to get.
   * @param args the args to get.
   *
   * @return formatted string value.
   */
  @NotNull
  static String get(
    @NotNull final String bundle,
    @Nullable final Locale locale,
    @NotNull final String key,
    @NotNull final Object... args
  ) {
    return MessageFormat.format(
      Localizations.get(bundle, locale).getString(key),
      args
    );
  }

  /**
   * creates a simple cache for resource bundle.
   *
   * @param bundle the bundle to create.
   *
   * @return resource bundle cache.
   */
  @NotNull
  private static LoadingCache<Locale, ResourceBundle> create(
    @NotNull final String bundle
  ) {
    return Caffeine
      .newBuilder()
      .expireAfterWrite(Duration.ofMinutes(30L))
      .build(key -> ResourceBundle.getBundle(bundle, key));
  }

  /**
   * gets the bundle.
   *
   * @param bundle the bundle to get.
   * @param locale the locale to get.
   *
   * @return bundle.
   */
  @NotNull
  private static ResourceBundle get(
    @NotNull final String bundle,
    @Nullable final Locale locale
  ) {
    return Localizations.get(bundle).get(locale == null ? Locale.US : locale);
  }

  /**
   * gets the cache.
   *
   * @param bundle the bungle to get.
   *
   * @return cache.
   */
  @NotNull
  private static LoadingCache<Locale, ResourceBundle> get(
    @NotNull final String bundle
  ) {
    return Localizations.CACHE.get(bundle);
  }
}
