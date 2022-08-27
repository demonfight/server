package com.demonfight.server.minestom;

import com.demonfight.server.common.DnsVars;
import com.demonfight.server.common.Observers;
import com.demonfight.server.common.Redis;
import com.demonfight.server.common.Vars;
import com.demonfight.server.common.functions.FailableConsumer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import java.util.Arrays;
import java.util.function.UnaryOperator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.agones4j.AgonesSdk;
import tr.com.infumia.terminable.CompositeTerminable;
import tr.com.infumia.terminable.TerminableConsumer;
import tr.com.infumia.terminable.TerminableModule;

/**
 * a class that contains utility methods for servers.
 */
@Slf4j
@UtilityClass
public class Servers {

  /**
   * starts a simple server with the modules.
   *
   * @param modules the modules to start.
   */
  @SafeVarargs
  public void simple(
    @NotNull final Class<? extends TerminableModule>... modules
  ) {
    Servers.simple(UnaryOperator.identity(), modules);
  }

  /**
   * starts a simple server with the modules.
   *
   * @param operator the operator to start.
   * @param modules the modules to start.
   */
  @SafeVarargs
  public void simple(
    @NotNull final UnaryOperator<Injector> operator,
    @NotNull final Class<? extends TerminableModule>... modules
  ) {
    Servers.simple(injector -> {
      final var newInjector = operator.apply(injector);
      final var consumer = newInjector.getInstance(CompositeTerminable.class);
      Arrays
        .stream(modules)
        .map(newInjector::getInstance)
        .forEach(module -> module.bindModuleWith(consumer));
    });
  }

  /**
   * starts a simple server.
   *
   * @param onStart the on start to start.
   */
  private void simple(@NotNull final FailableConsumer<Injector> onStart) {
    try (
      final var ignored = new Measured("Done ({0} ms)! For help, type nothing.")
    ) {
      Servers.log.info("Starting Queue/AFK server.");
      final var composite = CompositeTerminable.simple();
      final var agones = new AgonesSdk();
      agones.bindWith(composite);
      Redis.init().bindWith(composite);
      System.setProperty(
        "minestom.chunk-view-distance",
        MinestomVars.CHUNK_VIEW_DISTANCE
      );
      final var server = MinecraftServer.init();
      MinecraftServer.setBrandName(MinestomVars.BRAND_NAME);
      MinecraftServer.setCompressionThreshold(
        MinestomVars.COMPRESSION_THRESHOLD
      );
      MinecraftServer
        .getSchedulerManager()
        .buildShutdownTask(composite::closeUnchecked);
      final var container = MinecraftServer
        .getInstanceManager()
        .createInstanceContainer();
      final var injector = Guice.createInjector(binder -> {
        binder
          .bind(String.class)
          .annotatedWith(Names.named("serviceDns"))
          .toInstance(DnsVars.SERVER);
        binder.bind(MinecraftServer.class).toInstance(server);
        binder.bind(AgonesSdk.class).toInstance(agones);
        binder.bind(CompositeTerminable.class).toInstance(composite);
        binder.bind(TerminableConsumer.class).toInstance(composite);
        binder
          .bind(InstanceContainer.class)
          .annotatedWith(Names.named("defaultInstance"))
          .toInstance(container);
      });
      VelocitySupport.init();
      onStart.accept(injector);
      server.start("0.0.0.0", Vars.SERVER_PORT);
      agones.ready(
        Observers.completed(() -> {
          final var task = MinecraftServer
            .getSchedulerManager()
            .scheduleTask(
              () -> agones.health(Observers.noop()),
              TaskSchedule.seconds(1L),
              TaskSchedule.seconds(3L)
            );
          composite.bind(task::cancel);
        })
      );
    } catch (final Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
