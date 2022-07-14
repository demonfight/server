package com.demonfight.server.minestom;

import net.minestom.server.extras.velocity.VelocityProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a class that contains utility methods for velocity support on Minestom.
 */
interface VelocitySupport {
  /**
   * the logger.
   */
  Logger LOGGER = LoggerFactory.getLogger(VelocitySupport.class);

  /**
   * initiates the support.
   */
  static void init() {
    if (MinestomVars.VELOCITY_FORWARDING_SECRET != null) {
      VelocitySupport.LOGGER.info("Velocity support enabled.");
      VelocityProxy.enable(MinestomVars.VELOCITY_FORWARDING_SECRET);
    }
  }
}
