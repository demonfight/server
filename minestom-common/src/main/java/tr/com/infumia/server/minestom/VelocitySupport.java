package tr.com.infumia.server.minestom;

import net.minestom.server.extras.velocity.VelocityProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.infumia.server.common.Vars;

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
    if (Vars.VELOCITY_SECRET != null) {
      VelocitySupport.LOGGER.info("Velocity support enabled.");
      VelocityProxy.enable(Vars.VELOCITY_SECRET);
    }
  }
}
