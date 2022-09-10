package com.demonfight.server.common;

import com.demonfight.server.common.configurate.AddressSerializer;
import com.demonfight.server.common.configurate.BigDecimalSerializer;
import com.demonfight.server.common.configurate.BigIntegerSerializer;
import com.demonfight.server.common.configurate.LocaleSerializer;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

public interface Configurate {
  TypeSerializerCollection COLLECTION = TypeSerializerCollection
    .builder()
    .register(AddressSerializer.INSTANCE)
    .register(BigDecimalSerializer.INSTANCE)
    .register(BigIntegerSerializer.INSTANCE)
    .register(LocaleSerializer.INSTANCE)
    .registerAll(
      ConfigurateComponentSerializer
        .builder()
        .outputStringComponents(true)
        .scalarSerializer(MiniMessage.miniMessage())
        .build()
        .serializers()
    )
    .build();
}
