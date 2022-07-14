package com.demonfight.server.common;

import io.grpc.stub.StreamObserver;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * a utility class that contains utility methods for observers.
 */
@UtilityClass
@SuppressWarnings("unchecked")
public class Observers {

  /**
   * the noop.
   */
  private final StreamObserver<?> NOOP = new StreamObserver<>() {
    @Override
    public void onNext(final Object value) {}

    @Override
    public void onError(final Throwable t) {}

    @Override
    public void onCompleted() {}
  };

  /**
   * creates an observer which runs only when it completed.
   *
   * @param completed the complete to create.
   * @param <T> type of the observed object.
   *
   * @return observer.
   */
  @NotNull
  public <T> StreamObserver<T> completed(@NotNull final Runnable completed) {
    return new FunctionalObserver<>(completed, null, null);
  }

  /**
   * gets the noop observer.
   *
   * @param <T> type of the observed object.
   *
   * @return noop observer.
   */
  @NotNull
  public <T> StreamObserver<T> noop() {
    return (StreamObserver<T>) Observers.NOOP;
  }
}
