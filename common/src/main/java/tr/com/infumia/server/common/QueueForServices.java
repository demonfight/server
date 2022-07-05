package tr.com.infumia.server.common;

import org.jetbrains.annotations.NotNull;

/**
 * an interface to determine queue for services.
 */
public interface QueueForServices {

  /**
   * initiates the queue for services.
   * @param service the service to initiate.
   */
  static void init(
    @NotNull final String service,
    @NotNull final String targetService,
    @NotNull final String target
  ) {
  }
}
