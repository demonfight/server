package com.demonfight.server.minestom;

import com.demonfight.server.common.Dns;

/**
 * an interface that contains Minestom DNSs.
 */
public interface MinestomDns {
  /**
   * the texture server dns.
   */
  String TEXTURE_SERVER = Dns.svc(
    MinestomVars.TEXTURE_SERVER_SERVICE_NAME,
    MinestomVars.TEXTURE_SERVER_SERVICE_NAMESPACE
  );
}
