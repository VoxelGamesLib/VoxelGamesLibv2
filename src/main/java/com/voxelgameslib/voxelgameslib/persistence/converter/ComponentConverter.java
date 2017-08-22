package com.voxelgameslib.voxelgameslib.persistence.converter;

import net.kyori.text.Component;
import net.kyori.text.serializer.ComponentSerializers;

import javax.annotation.Nonnull;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ComponentConverter implements AttributeConverter<Component, String> {

    @Override
    @Nonnull
    public String convertToDatabaseColumn(@Nonnull Component attribute) {
        return ComponentSerializers.JSON.serialize(attribute);
    }

    @Override
    @Nonnull
    public Component convertToEntityAttribute(@Nonnull String dbData) {
        return ComponentSerializers.JSON.deserialize(dbData);
    }
}
