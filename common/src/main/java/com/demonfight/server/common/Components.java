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

public interface Components {
  @Nullable
  @Contract("null -> null; !null -> !null")
  static Component deserialize(@Nullable final String text) {
    return Optional
      .ofNullable(text)
      .map(input -> MiniMessage.miniMessage().deserialize(input))
      .orElse(null);
  }

  @Nullable
  @SafeVarargs
  @Contract("null, _ -> null; !null, _ -> !null")
  static List<Component> replace(
    @Nullable final List<Component> components,
    @NotNull final Map.Entry<String, Object>... replaces
  ) {
    return Components.replace(components, Set.of(replaces));
  }

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

  @Nullable
  @SafeVarargs
  @Contract("null, _ -> null; !null, _ -> !null")
  static Component replace(
    @Nullable final Component component,
    @NotNull final Map.Entry<String, Object>... replaces
  ) {
    return Components.replace(component, Set.of(replaces));
  }

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

  @Nullable
  @Contract("null -> null; !null -> !null")
  static String serialize(@Nullable final Component input) {
    return Optional
      .ofNullable(input)
      .map(component -> MiniMessage.miniMessage().serialize(component))
      .orElse(null);
  }
}
