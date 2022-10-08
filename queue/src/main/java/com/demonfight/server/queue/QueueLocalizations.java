package com.demonfight.server.queue;

import com.demonfight.server.common.Localizations;
import java.util.Locale;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
final class QueueLocalizations {

  private final String BUNDLE_KEY = "localization.Queue";

  private final String POSITION_IN_QUEUE = "position-in-queue";

  @NotNull
  String positionInQueue(@Nullable final Locale locale) {
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
    return Localizations.get(QueueLocalizations.BUNDLE_KEY, locale, key, args);
  }
}
