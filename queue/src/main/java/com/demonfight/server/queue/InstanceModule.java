package com.demonfight.server.queue;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.terminable.TerminableConsumer;
import tr.com.infumia.terminable.TerminableModule;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class InstanceModule implements TerminableModule {

  @Inject
  @NotNull
  @Named("defaultContainer")
  InstanceContainer container;

  @Override
  public void setup(@NotNull final TerminableConsumer consumer) {
    this.container.setTime(18_000);
    this.container.setTimeRate(0);
    this.container.setTimeUpdate(null);
    this.container.setGenerator(unit -> {});
    this.container.enableAutoChunkLoad(true);
    this.container.setBlock(0, 62, 0, Block.BARRIER);
    this.container.setBlock(1, 63, 0, Block.BARRIER);
    this.container.setBlock(-1, 63, 0, Block.BARRIER);
    this.container.setBlock(0, 63, 1, Block.BARRIER);
    this.container.setBlock(0, 63, -1, Block.BARRIER);
    this.container.setBlock(1, 64, 0, Block.BARRIER);
    this.container.setBlock(-1, 64, 0, Block.BARRIER);
    this.container.setBlock(0, 64, 1, Block.BARRIER);
    this.container.setBlock(0, 64, -1, Block.BARRIER);
    this.container.setBlock(0, 65, 0, Block.BARRIER);
  }
}
