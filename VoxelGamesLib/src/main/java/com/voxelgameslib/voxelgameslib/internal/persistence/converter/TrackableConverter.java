package com.voxelgameslib.voxelgameslib.internal.persistence.converter;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.JavaTypeDescriptorRegistry;

import javax.annotation.Nonnull;
import javax.persistence.Converter;

import com.voxelgameslib.voxelgameslib.api.exception.VoxelGameLibException;
import com.voxelgameslib.voxelgameslib.api.stats.StatsHandler;
import com.voxelgameslib.voxelgameslib.api.stats.Trackable;

@Converter
public class TrackableConverter implements VGLConverter<Trackable, String> {

    @Override
    @Nonnull
    public String convertToDatabaseColumn(@Nonnull Trackable attribute) {
        return attribute.getPrefix() + ":" + attribute.name();
    }

    @Override
    @Nonnull
    public Trackable convertToEntityAttribute(@Nonnull String dbData) {
        return StatsHandler.fromName(dbData).orElseThrow(() -> new VoxelGameLibException("Couldn't load db data: Encountered unknown stat type: " + dbData));
    }

    public void init() {
        JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(new AbstractTypeDescriptor<Trackable>(Trackable.class) {
            @Override
            public String toString(Trackable value) {
                return convertToDatabaseColumn(value);
            }

            @Override
            public Trackable fromString(String string) {
                return convertToEntityAttribute(string);
            }

            @Override
            @SuppressWarnings("unchecked")
            public <X> X unwrap(Trackable value, Class<X> type, WrapperOptions options) {
                if (value == null) {
                    return null;
                }

                if (Trackable.class.isAssignableFrom(type)) {
                    return (X) value;
                }

                throw unknownWrap(value.getClass());
            }

            @Override
            public <X> Trackable wrap(X value, WrapperOptions options) {
                if (value == null) {
                    return null;
                }

                if (Trackable.class.isInstance(value)) {
                    return (Trackable) value;
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
