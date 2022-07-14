package com.demonfight.server.common.functions;

/**
 * an interface to determine failable runnables.
 */
@FunctionalInterface
public interface FailableRunnable {
  /**
   * runs.
   *
   * @throws Throwable if something goes wrong.
   */
  void run() throws Throwable;
}
