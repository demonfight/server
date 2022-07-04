package tr.com.infumia.server.queue;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.MinecraftServer;
import tr.com.infumia.server.common.Redis;
import tr.com.infumia.server.common.ResourceBundles;
import tr.com.infumia.server.common.Vars;
import tr.com.infumia.server.minestom.Measured;
import tr.com.infumia.server.minestom.VelocitySupport;
import tr.com.infumia.server.queue.module.EventModule;
import tr.com.infumia.server.queue.module.InstanceModule;
import tr.com.infumia.server.queue.module.QueueModule;
import tr.com.infumia.terminable.CompositeTerminable;

@Slf4j
public final class Server {

  public static void main(final String[] args) {
    try (
      final var ignored = new Measured("Done ({0} ms)! For help, type nothing.")
    ) {
      Server.log.info("Starting Queue/AFK server.");
      final var composite = CompositeTerminable.simple();
      ResourceBundles.create("localization.Queue");
      Redis.init().bindWith(composite);
      System.setProperty(
        "minestom.chunk-view-distance",
        Vars.CHUNK_VIEW_DISTANCE
      );
      final var server = MinecraftServer.init();
      MinecraftServer.setBrandName(Vars.BRAND_NAME);
      MinecraftServer.setCompressionThreshold(Vars.COMPRESSION_THRESHOLD);
      MinecraftServer
        .getSchedulerManager()
        .buildShutdownTask(composite::closeUnchecked);
      final var container = MinecraftServer
        .getInstanceManager()
        .createInstanceContainer();
      new InstanceModule(container).bindModuleWith(composite);
      new EventModule(container).bindModuleWith(composite);
      new QueueModule().bindModuleWith(composite);
      VelocitySupport.init();
      server.start("0.0.0.0", Vars.SERVER_PORT);
    }
  }
}
