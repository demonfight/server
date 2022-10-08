package com.demonfight.server.common;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public interface Entries {
  @NotNull
  static Collection<Map.Entry<String, Object>> keyPercentages(
    @NotNull final Collection<Map.Entry<String, Object>> entries
  ) {
    return entries
      .stream()
      .map(entry ->
        entry.getKey().startsWith("%")
          ? entry
          : Map.entry("%" + entry.getKey() + "%", entry.getValue())
      )
      .collect(Collectors.toSet());
  }

  @NotNull
  @SafeVarargs
  static Collection<Map.Entry<String, Object>> keyPercentages(
    @NotNull final Map.Entry<String, Object>... entries
  ) {
    return Entries.keyPercentages(Set.of(entries));
  }
}
