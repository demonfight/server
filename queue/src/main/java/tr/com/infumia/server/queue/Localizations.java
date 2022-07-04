package tr.com.infumia.server.queue;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tr.com.infumia.server.common.ResourceBundles;

public interface Localizations {
  String POSITION_IN_QUEUE = "position-in-queue";

  @NotNull
  static String positionInQueue(@Nullable final Locale locale) {
    return Localizations.queue(locale, Localizations.POSITION_IN_QUEUE);
  }

  @NotNull
  private static String queue(
    @Nullable final Locale locale,
    @NotNull final String key,
    @NotNull final Object... args
  ) {
    return ResourceBundles.get("localization.Queue", locale, key, args);
  }
}
