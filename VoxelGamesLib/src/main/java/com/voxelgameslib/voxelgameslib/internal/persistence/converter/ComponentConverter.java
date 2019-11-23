package com.voxelgameslib.voxelgameslib.internal.persistence.converter;

import net.kyori.text.Component;
import net.kyori.text.serializer.ComponentSerializers;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.JavaTypeDescriptorRegistry;

import javax.annotation.Nonnull;
import javax.persistence.Converter;

@Converter
public class ComponentConverter implements VGLConverter<Component, String> {

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

    @Override
    public void init() {
        JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(new AbstractTypeDescriptor<Component>(Component.class) {
            @Override
            public String toString(Component value) {
                return convertToDatabaseColumn(value);
            }

            @Override
            public Component fromString(String string) {
                return convertToEntityAttribute(string);
            }

            @Override
            @SuppressWarnings("unchecked")
            public <X> X unwrap(Component value, Class<X> type, WrapperOptions options) {
                if (value == null) {
                    return null;
                }

                if (Component.class.isAssignableFrom(type)) {
                    return (X) value;
                }

                throw unknownWrap(value.getClass());
            }

            @Override
            public <X> Component wrap(X value, WrapperOptions options) {
                if (value == null) {
                    return null;
                }

                if (Component.class.isInstance(value)) {
                    return (Component) value;
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
