package tr.com.infumia.server.queue;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tr.com.infumia.server.common.Localizations;

public interface QueueLocalizations {
  String POSITION_IN_QUEUE = "position-in-queue";

  @NotNull
  static String positionInQueue(@Nullable final Locale locale) {
    return QueueLocalizations.queue(
      locale,
      QueueLocalizations.POSITION_IN_QUEUE
    );
  }

  @NotNull
  private static String queue(
    @Nullable final Locale locale,
    @NotNull final String key,
    @NotNull final Object... args
  ) {
    return Localizations.get("localization.Queue", locale, key, args);
  }
}
