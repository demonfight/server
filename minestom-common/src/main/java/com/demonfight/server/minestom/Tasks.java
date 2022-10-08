package com.demonfight.server.minestom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import tr.com.infumia.terminable.Terminable;

/**
 * an interface that contains utility methods for tasks.
 */
public interface Tasks {
  /**
   * runs a task.
   *
   * @param runnable the runnable to run.
   * @param delay the delay to run.
   * @param repeat the repeat to run.
   * @param type the type to run.
   *
   * @return terminable task.
   */
  @NotNull
  static Terminable run(
    @NotNull final Runnable runnable,
    @NotNull final TaskSchedule delay,
    @NotNull final TaskSchedule repeat,
    @NotNull final ExecutionType type
  ) {
    final var task = MinecraftServer
      .getSchedulerManager()
      .scheduleTask(runnable, delay, repeat, type);
    return task::cancel;
  }
}
