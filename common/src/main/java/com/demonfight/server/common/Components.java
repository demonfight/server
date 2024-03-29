package com.demonfight.server.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an interface that contains utility methods for component.
 */
public interface Components {
  /**
   * deserializes the text.
   *
   * @param text the text to deserialize.
   *
   * @return deserialized text.
   */
  @Nullable
  @Contract("null -> null; !null -> !null")
  static Component deserialize(@Nullable final String text) {
    return Optional
      .ofNullable(text)
      .map(input -> MiniMessage.miniMessage().deserialize(input))
      .orElse(null);
  }

  /**
   * replaces the components with the given replaces.
   *
   * @param components the components to replace.
   * @param replaces the replaces to replace.
   *
   * @return replaced component list.
   */
  @Nullable
  @SafeVarargs
  @Contract("null, _ -> null; !null, _ -> !null")
  static List<Component> replace(
    @Nullable final List<Component> components,
    @NotNull final Map.Entry<String, Object>... replaces
  ) {
    return Components.replace(components, Set.of(replaces));
  }

  /**
   * replaces the components with the given replaces.
   *
   * @param components the components to replace.
   * @param replaces the replaces to replace.
   *
   * @return replaced component list.
   */
  @Nullable
  @Contract("null, _ -> null; !null, _ -> !null")
  static List<Component> replace(
    @Nullable final List<Component> components,
    @NotNull final Collection<Map.Entry<String, Object>> replaces
  ) {
    return Optional
      .ofNullable(components)
      .map(c -> {
        final var list = new ArrayList<Component>();
        for (final var component : c) {
          list.add(Components.replace(component, replaces));
        }
        return list;
      })
      .orElse(null);
  }

  /**
   * replaces the component with the given replaces.
   *
   * @param component the component to replace.
   * @param replaces the replaces to replace.
   *
   * @return replaced component.
   */
  @Nullable
  @SafeVarargs
  @Contract("null, _ -> null; !null, _ -> !null")
  static Component replace(
    @Nullable final Component component,
    @NotNull final Map.Entry<String, Object>... replaces
  ) {
    return Components.replace(component, Set.of(replaces));
  }

  /**
   * replaces the component with the given replaces.
   *
   * @param component the component to replace.
   * @param replaces the replaces to replace.
   *
   * @return replaced component.
   */
  @Nullable
  @Contract("null, _ -> null; !null, _ -> !null")
  static Component replace(
    @Nullable final Component component,
    @NotNull final Collection<Map.Entry<String, Object>> replaces
  ) {
    return Optional
      .ofNullable(component)
      .map(c -> {
        var temp = component;
        for (final var entry : Entries.keyPercentages(replaces)) {
          final var key = entry.getKey();
          final var value = entry.getValue();
          temp =
            temp.replaceText(builder -> {
              builder.matchLiteral(key);
              if (value instanceof ComponentLike componentLike) {
                builder.replacement(componentLike);
              } else {
                builder.replacement(value.toString());
              }
            });
        }
        return temp;
      })
      .orElse(null);
  }

  /**
   * serializes the component.
   *
   * @param input the input to serialize.
   *
   * @return serialized component.
   */
  @Nullable
  @Contract("null -> null; !null -> !null")
  static String serialize(@Nullable final Component input) {
    return Optional
      .ofNullable(input)
      .map(component -> MiniMessage.miniMessage().serialize(component))
      .orElse(null);
  }
}
