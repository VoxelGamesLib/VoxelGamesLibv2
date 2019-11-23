package com.voxelgameslib.voxelgameslib.internal.persistence.converter;

import net.kyori.text.Component;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.JavaTypeDescriptorRegistry;

import javax.annotation.Nonnull;
import javax.persistence.Converter;

import com.voxelgameslib.voxelgameslib.internal.lang.Locale;

@Converter
public class LocaleConverter implements VGLConverter<Locale, String> {

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

    @Override
    public void init() {
        JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(new AbstractTypeDescriptor<Locale>(Locale.class) {
            @Override
            public String toString(Locale value) {
                return convertToDatabaseColumn(value);
            }

            @Override
            public Locale fromString(String string) {
                return convertToEntityAttribute(string);
            }


            @Override
            @SuppressWarnings("unchecked")
            public <X> X unwrap(Locale value, Class<X> type, WrapperOptions options) {
                if (value == null) {
                    return null;
                }

                if (Locale.class.isAssignableFrom(type)) {
                    return (X) value;
                }

                throw unknownWrap(value.getClass());
            }

            @Override
            public <X> Locale wrap(X value, WrapperOptions options) {
                if (value == null) {
                    return null;
                }

                if (Component.class.isInstance(value)) {
                    return (Locale) value;
                }

                throw unknownUnwrap(value.getClass());
            }
        });
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
