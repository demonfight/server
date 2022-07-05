package tr.com.infumia.server.common;

import io.grpc.stub.StreamObserver;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents functional observers.
 *
 * @param <V> type of the observable object.
 */
record FunctionalObserver<V>(
  @Nullable Runnable completed,
  @Nullable Consumer<V> next,
  @Nullable Consumer<Throwable> error
)
  implements StreamObserver<V> {
  @Override
  public void onNext(final V value) {
    if (this.next != null) {
      this.next.accept(value);
    }
  }

  @Override
  public void onError(final Throwable t) {
    if (this.error != null) {
      this.error.accept(t);
    }
  }

  @Override
  public void onCompleted() {
    if (this.completed != null) {
      this.completed.run();
    }
  }
}
