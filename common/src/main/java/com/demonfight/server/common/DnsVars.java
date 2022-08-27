package com.demonfight.server.common;

/**
 * an interface that contains DNS variables.
 */
public interface DnsVars {
  /**
   * the redis.
   */
  String REDIS = Dns.svc(Vars.REDIS_SERVICE_NAME, Vars.REDIS_SERVICE_NAMESPACE);

  /**
   * the server.
   */
  String SERVER = Dns.svc(
    Vars.SERVER_SERVICE_NAME,
    Vars.SERVER_SERVICE_NAMESPACE
  );
}
