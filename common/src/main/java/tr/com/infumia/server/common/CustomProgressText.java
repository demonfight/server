package tr.com.infumia.server.common;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents custom progress texts.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class CustomProgressText implements Supplier<String> {

  /**
   * the frame.
   */
  AtomicInteger frame = new AtomicInteger();

  /**
   * the frames.
   */
  @NotNull
  String @NotNull [] frames;

  /**
   * ctor.
   *
   * @param frames the frames.
   */
  public CustomProgressText(
    @NotNull final String @NotNull ... frames
  ) {
    this.frames = frames.clone();
  }

  @Override
  public String get() {
    return this.frames[this.nextFrame()];
  }

  /**
   * gets the next frame.
   *
   * @return next frame.
   */
  private int nextFrame() {
    final var nextFrame = this.frame.getAndIncrement();
    if (nextFrame < this.frames.length) {
      return nextFrame;
    }
    this.resetFrame();
    return this.nextFrame();
  }

  /**
   * resets the frame.
   */
  private void resetFrame() {
    this.frame.set(0);
  }
}
