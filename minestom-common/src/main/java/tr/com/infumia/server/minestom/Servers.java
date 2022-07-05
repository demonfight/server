package tr.com.infumia.server.minestom;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.infumia.agones4j.AgonesSdk;
import tr.com.infumia.server.common.Dns;
import tr.com.infumia.server.common.Observers;
import tr.com.infumia.server.common.Redis;
import tr.com.infumia.server.common.Vars;
import tr.com.infumia.server.common.functions.FailableConsumer;
import tr.com.infumia.terminable.CompositeTerminable;
import tr.com.infumia.terminable.TerminableConsumer;
import tr.com.infumia.terminable.TerminableModule;

/**
 * an interface that contains utility methods for servers.
 */
public interface Servers {
  /**
   * the logger.
   */
  Logger LOGGER = LoggerFactory.getLogger(Servers.class);

  /**
   * starts a simple server.
   *
   * @param onStart the on start to start.
   */
  static void simple(@NotNull final FailableConsumer<Injector> onStart) {
    try (
      final var ignored = new Measured("Done ({0} ms)! For help, type nothing.")
    ) {
      Servers.LOGGER.info("Starting Queue/AFK server.");
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
          .toInstance(
            Dns.svc(Vars.SERVER_SERVICE_NAME, Vars.SERVER_SERVICE_NAMESPACE)
          );
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

  /**
   * starts a simple server with the modules.
   *
   * @param modules the modules to start.
   */
  @SafeVarargs
  static void simple(
    @NotNull final Class<? extends TerminableModule>... modules
  ) {
    Servers.simple(injector -> {
      final var consumer = injector.getInstance(CompositeTerminable.class);
      for (final var module : modules) {
        injector.getInstance(module).bindModuleWith(consumer);
      }
    });
  }
}
