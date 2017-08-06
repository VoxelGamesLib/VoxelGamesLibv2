package com.voxelgameslib.voxelgameslib.persistence.converter;

import net.kyori.text.Component;
import net.kyori.text.serializer.ComponentSerializer;

import javax.annotation.Nonnull;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ComponentConverter implements AttributeConverter<Component, String> {

    @Override
    @Nonnull
    public String convertToDatabaseColumn(@Nonnull Component attribute) {
        return ComponentSerializer.serialize(attribute);
    }

    @Override
    @Nonnull
    public Component convertToEntityAttribute(@Nonnull String dbData) {
        return ComponentSerializer.deserialize(dbData);
    }
}
