package tr.com.infumia.server.minestom;

import java.text.MessageFormat;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.terminable.Terminable;

/**
 * a class that represents measured operations.
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Measured implements Terminable {

  /**
   * the message.
   */
  @NotNull
  String message;

  /**
   * the start time.
   */
  long startTime = System.nanoTime();

  @Override
  public void close() {
    final var timeElapsed = (System.nanoTime() - this.startTime) / 1.0E6;
    Measured.log.info(MessageFormat.format(
      this.message,
      timeElapsed
    ));
  }
}
