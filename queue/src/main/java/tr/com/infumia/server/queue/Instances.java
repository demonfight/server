package tr.com.infumia.server.queue;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

public interface Instances {

  @NotNull
  static InstanceContainer init() {
    final var container = MinecraftServer.getInstanceManager().createInstanceContainer();
    container.setTime(18_000);
    container.setTimeRate(0);
    container.setTimeUpdate(null);
    container.setGenerator(unit -> {
    });
    container.enableAutoChunkLoad(true);
    container.setBlock(0, 62, 0, Block.BARRIER);
    container.setBlock(1, 63, 0, Block.BARRIER);
    container.setBlock(-1, 63, 0, Block.BARRIER);
    container.setBlock(0, 63, 1, Block.BARRIER);
    container.setBlock(0, 63, -1, Block.BARRIER);
    container.setBlock(1, 64, 0, Block.BARRIER);
    container.setBlock(-1, 64, 0, Block.BARRIER);
    container.setBlock(0, 64, 1, Block.BARRIER);
    container.setBlock(0, 64, -1, Block.BARRIER);
    container.setBlock(0, 65, 0, Block.BARRIER);
    return container;
  }
}
