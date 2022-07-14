package com.demonfight.server.common.functions;

/**
 * an interface to determine failable consumers.
 *
 * @param <X> type of the consumed object.
 */
@FunctionalInterface
public interface FailableConsumer<X> {
  /**
   * consumes {@link X} value.
   *
   * @param x the x to consume.
   *
   * @throws Throwable if something goes wrong.
   */
  void accept(X x) throws Throwable;
}
