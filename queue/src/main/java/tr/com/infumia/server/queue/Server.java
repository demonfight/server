package tr.com.infumia.server.queue;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.MinecraftServer;
import tr.com.infumia.server.common.Envs;
import tr.com.infumia.server.minestom.Measured;
import tr.com.infumia.server.minestom.VelocitySupport;

@Slf4j
public final class Server {

  public static void main(final String[] args) {
    try (
      final var ignored = new Measured("Done ({0} ms)! For help, type nothing.")
    ) {
      Server.log.info("Starting Queue/AFK server.");
      System.setProperty(
        "minestom.chunk-view-distance",
        Envs.get(Envs.CHUNK_VIEW_DISTANCE, "2")
      );
      final var server = MinecraftServer.init();
      MinecraftServer.setBrandName(Envs.get(Envs.BRAND_NAME, "Infumia"));
      MinecraftServer.setCompressionThreshold(
        Envs.getInt(Envs.COMPRESSION_THRESHOLD, 0)
      );
      final var container = Instances.init();
      Events.init(container);
      VelocitySupport.init();
      server.start("0.0.0.0", Envs.getInt(Envs.SERVER_PORT, 25565));
    }
  }
}
