package tr.com.infumia.server.minestom;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.extras.velocity.VelocityProxy;
import tr.com.infumia.server.common.Vars;

/**
 * a class that contains utility methods for velocity support on Minestom.
 */
@Slf4j
@UtilityClass
public class VelocitySupport {

  /**
   * initiates the support.
   */
  public void init() {
    if (Vars.VELOCITY_SECRET != null) {
      VelocitySupport.log.info("Velocity support enabled.");
      VelocityProxy.enable(Vars.VELOCITY_SECRET);
    }
  }
}
