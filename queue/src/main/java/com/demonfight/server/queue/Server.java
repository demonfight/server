package com.demonfight.server.queue;

import com.demonfight.server.minestom.Servers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Server {

  public static void main(final String[] args) {
    Servers.simple(InstanceModule.class, EventModule.class, QueueModule.class);
  }
}
