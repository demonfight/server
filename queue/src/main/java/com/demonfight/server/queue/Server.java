package com.demonfight.server.queue;

import com.demonfight.server.minestom.Servers;

public final class Server {

  public static void main(final String[] args) {
    Servers.simple(InstanceModule.class, EventModule.class, QueueModule.class);
  }
}
