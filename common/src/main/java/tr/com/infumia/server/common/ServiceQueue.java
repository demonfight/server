package tr.com.infumia.server.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

/**
 * an interface to determine service queues.
 */
public interface ServiceQueue {
  /**
   * initiates the queue for services.
   *
   * @param current the current service dns to initiate.
   * @param target the target service dns to initiate.
   */
  @NotNull
  static Impl init(
    @NotNull final String current,
    @NotNull final String target
  ) {
    return new Impl(current, target);
  }

  /**
   * obtains the current.
   *
   * @return current.
   */
  @NotNull
  String current();

  /**
   * obtains the target.
   *
   * @return target.
   */
  @NotNull
  String target();

  /**
   * a simple implementation of {@link ServiceQueue}.
   */
  @Getter
  @RequiredArgsConstructor
  @Accessors(fluent = true)
  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  final class Impl implements ServiceQueue {

    /**
     * the current.
     */
    @NotNull
    String current;

    /**
     * the target.
     */
    @NotNull
    String target;
  }
}
