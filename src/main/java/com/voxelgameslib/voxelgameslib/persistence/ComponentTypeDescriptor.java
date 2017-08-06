package com.voxelgameslib.voxelgameslib.persistence;

import net.kyori.text.Component;
import net.kyori.text.serializer.ComponentSerializer;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ComponentTypeDescriptor extends AbstractTypeDescriptor<Component> {

    public static ComponentTypeDescriptor INSTANCE = new ComponentTypeDescriptor();

    @SuppressWarnings("unchecked")
    protected ComponentTypeDescriptor() {
        super(Component.class, ImmutableMutabilityPlan.INSTANCE);
    }

    @Override
    @Nonnull
    public String toString(@Nonnull Component value) {
        return ComponentSerializer.serialize(value);
    }

    @Override
    @Nonnull
    public Component fromString(@Nonnull String string) {
        return ComponentSerializer.deserialize(string);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Nullable
    public <X> X unwrap(@Nullable Component value, @Nonnull Class<X> type, @Nonnull WrapperOptions options) {
        if (value == null) {
            return null;
        }

        if (Component.class.isAssignableFrom(type)) {
            return (X) value;
        }

        throw unknownWrap(value.getClass());
    }

    @Override
    @Nullable
    public <X> Component wrap(@Nullable X value, @Nonnull WrapperOptions options) {
        if (value == null) {
            return null;
        }

        if (Component.class.isInstance(value)) {
            return (Component) value;
        }

        throw unknownUnwrap(value.getClass());
    }
}
