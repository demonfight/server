package tr.com.infumia.server.queue.module;

import org.jetbrains.annotations.NotNull;
import tr.com.infumia.terminable.TerminableConsumer;
import tr.com.infumia.terminable.TerminableModule;

public record QueueModule() implements TerminableModule {
  @Override
  public void setup(@NotNull final TerminableConsumer consumer) {}
}
