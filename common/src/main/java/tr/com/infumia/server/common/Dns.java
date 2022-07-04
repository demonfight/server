package tr.com.infumia.server.common;

import org.jetbrains.annotations.NotNull;

/**
 * an interface that contains utility methods for dns.
 */
public interface Dns {
  /**
   * creates async stub.
   *
   * @param name the name to create.
   * @param namespace the namespace to create.
   *
   * @return stub.
   */
  @NotNull
  static String pod(
    @NotNull final String name,
    @NotNull final String namespace
  ) {
    return Vars.POD_ADDRESS_FORMAT.formatted(name, namespace);
  }

  /**
   * creates async stub.
   *
   * @param name the name to create.
   * @param namespace the namespace to create.
   *
   * @return stub.
   */
  @NotNull
  static String svc(
    @NotNull final String name,
    @NotNull final String namespace
  ) {
    return Vars.SERVICE_ADDRESS_FORMAT.formatted(name, namespace);
  }
}
