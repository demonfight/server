package tr.com.infumia.server.queue;

import lombok.extern.slf4j.Slf4j;
import tr.com.infumia.server.minestom.Servers;

@Slf4j
public final class Server {

  public static void main(final String[] args) {
    Servers.simple(InstanceModule.class, EventModule.class, QueueModule.class);
  }
}
