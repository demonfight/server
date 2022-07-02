package tr.com.infumia.server.minestom;

import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import net.minestom.server.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

/**
 * a class that contains event filters.
 */
@UtilityClass
public class EventFilters {

  /**
   * the ignore same block.
   */
  private final Predicate<PlayerMoveEvent> IGNORE_SAME_BLOCK = event ->
    !event.getNewPosition().sameBlock(event.getPlayer().getPosition());

  /**
   * the ignore same point.
   */
  private final Predicate<PlayerMoveEvent> IGNORE_SAME_POINT = event ->
    !event.getNewPosition().samePoint(event.getPlayer().getPosition());

  /**
   * filters same blocks.
   *
   * @return a filter that filters same blocks.
   */
  @NotNull
  public static Predicate<PlayerMoveEvent> ignoreSameBlock() {
    return EventFilters.IGNORE_SAME_BLOCK;
  }

  /**
   * filters same points.
   *
   * @return a filter that filters same points.
   */
  @NotNull
  public static Predicate<PlayerMoveEvent> ignoreSamePoint() {
    return EventFilters.IGNORE_SAME_POINT;
  }
}
