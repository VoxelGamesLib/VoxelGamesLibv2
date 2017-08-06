package com.voxelgameslib.voxelgameslib.persistence.converter;

import com.voxelgameslib.voxelgameslib.lang.Locale;

import javax.annotation.Nonnull;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LocaleConverter implements AttributeConverter<Locale, String> {

    @Override
    @Nonnull
    public String convertToDatabaseColumn(@Nonnull Locale attribute) {
        return attribute.getTag();
    }

    @Override
    @Nonnull
    public Locale convertToEntityAttribute(@Nonnull String dbData) {
        return Locale.fromTag(dbData).orElse(Locale.ENGLISH);
    }
}
