package me.minidigger.voxelgameslib.persistence.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import me.minidigger.voxelgameslib.lang.Locale;

@Converter
public class LocaleConverter implements AttributeConverter<Locale, String> {

    @Override
    public String convertToDatabaseColumn(Locale attribute) {
        return attribute.getTag();
    }

    @Override
    public Locale convertToEntityAttribute(String dbData) {
        return Locale.fromTag(dbData).orElse(Locale.ENGLISH);
    }
}
