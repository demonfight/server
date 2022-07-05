package tr.com.infumia.server.common.functions;

/**
 * an interface to determine failable bi consumers.
 *
 * @param <X> type of the consumed object.
 * @param <Y> type of the other consumed object.
 */
@FunctionalInterface
public interface FailableBiConsumer<X, Y> {
  /**
   * consumes {@link X} and {@link Y} value.
   *
   * @param x the x to consume.
   * @param y the t to consume.
   *
   * @throws Throwable if something goes wrong.
   */
  void accept(X x, Y y) throws Throwable;
}
