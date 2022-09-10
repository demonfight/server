package com.demonfight.server.queue;

import com.demonfight.server.minestom.MinestomDns;
import com.demonfight.server.minestom.Servers;
import com.demonfight.server.minestom.annotations.QueueTarget;

public final class Server {

  public static void main(final String[] args) {
    Servers.simple(
      injector ->
        injector.createChildInjector(binder ->
          binder
            .bindConstant()
            .annotatedWith(QueueTarget.class)
            .to(MinestomDns.TEXTURE_SERVER)
        ),
      InstanceModule.class,
      EventModule.class,
      QueueModule.class
    );
  }
}
