package com.demonfight.server.minestom;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.extras.velocity.VelocityProxy;

/**
 * a class that contains utility methods for velocity support on Minestom.
 */
@Slf4j
@UtilityClass
class VelocitySupport {

  /**
   * initiates the support.
   */
  void init() {
    if (MinestomVars.VELOCITY_FORWARDING_SECRET != null) {
      VelocitySupport.log.info("Velocity support enabled.");
      VelocityProxy.enable(MinestomVars.VELOCITY_FORWARDING_SECRET);
    }
  }
}
