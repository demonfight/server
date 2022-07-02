package tr.com.infumia.server.minestom;

import java.util.function.Consumer;
import lombok.experimental.UtilityClass;
import net.minestom.server.event.trait.CancellableEvent;
import org.jetbrains.annotations.NotNull;

/**
 * a class that contains event handlers.
 */
@UtilityClass
@SuppressWarnings("unchecked")
public class EventHandlers {

  /**
   * the cancel.
   */
  private final Consumer<? extends CancellableEvent> CANCEL = cancellableEvent -> cancellableEvent.setCancelled(true);

  /**
   * cancels the event.
   *
   * @param <T> type of the event.
   *
   * @return a handler that cancels the event.
   */
  @NotNull
  public <T extends CancellableEvent> Consumer<T> cancel() {
    return (Consumer<T>) EventHandlers.CANCEL;
  }
}
