package me.minidigger.voxelgameslib.persistence.converter;

import net.kyori.text.Component;
import net.kyori.text.serializer.ComponentSerializer;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ComponentConverter implements AttributeConverter<Component, String> {

    @Override
    public String convertToDatabaseColumn(Component attribute) {
        return ComponentSerializer.serialize(attribute);
    }

    @Override
    public Component convertToEntityAttribute(String dbData) {
        return ComponentSerializer.deserialize(dbData);
    }
}
