package com.demonfight.server.queue;

import com.demonfight.server.minestom.MinestomDns;
import com.demonfight.server.minestom.Servers;
import com.google.inject.name.Names;

public final class Server {

  public static void main(final String[] args) {
    Servers.simple(
      injector ->
        injector.createChildInjector(binder ->
          binder
            .bind(String.class)
            .annotatedWith(Names.named("queueTarget"))
            .toInstance(MinestomDns.TEXTURE_SERVER)
        ),
      InstanceModule.class,
      EventModule.class,
      QueueModule.class
    );
  }
}
