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
            .bindConstant()
            .annotatedWith(Names.named("queueTarget"))
            .to(MinestomDns.TEXTURE_SERVER)
        ),
      InstanceModule.class,
      EventModule.class,
      QueueModule.class
    );
  }
}
