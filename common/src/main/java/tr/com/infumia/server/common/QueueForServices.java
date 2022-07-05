package tr.com.infumia.server.common;

import org.jetbrains.annotations.NotNull;

/**
 * an interface to determine queue for services.
 */
public interface QueueForServices {

  /**
   * initiates the queue for services.
   *
   * @param serviceDns the service dns to initiate.
   * @param targetServiceDns the target service dns to initiate.
   */
  static void init(
    @NotNull final String serviceDns,
    @NotNull final String targetServiceDns
  ) {
  }
}
