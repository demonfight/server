package tr.com.infumia.server.common;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.Locale;
import java.util.ResourceBundle;
import org.jetbrains.annotations.NotNull;

/**
 * an interface that contains utility methods for resource bundles.
 */
public interface ResourceBundles {

  /**
   * the cache.
   */
  LoadingCache<String, LoadingCache<Locale, ResourceBundle>> CACHE = Caffeine.newBuilder()
    .expireAfterWrite(Duration.ofMinutes(30L))
    .build(ResourceBundles::create);

  /**
   * creates a simple cache for resource bundle.
   *
   * @param bundle the bundle to create.
   *
   * @return resource bundle cache.
   */
  @NotNull
  static LoadingCache<Locale, ResourceBundle> create(
    @NotNull final String bundle
  ) {
    return Caffeine.newBuilder()
      .expireAfterWrite(Duration.ofMinutes(30L))
      .build(key -> ResourceBundle.getBundle(bundle, key));
  }

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
    @NotNull final Locale locale,
    @NotNull final String key,
    @NotNull final Object... args
  ) {
    return MessageFormat.format(
      ResourceBundles.get(bundle, locale).getString(key),
      args
    );
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
    @NotNull final Locale locale
  ) {
    return ResourceBundles.get(bundle).get(locale);
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
    return ResourceBundles.CACHE.get(bundle);
  }
}
