package com.voxelgameslib.voxelgameslib.persistence;

import net.kyori.text.Component;
import net.kyori.text.serializer.ComponentSerializer;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;

public class ComponentTypeDescriptor extends AbstractTypeDescriptor<Component> {

    public static ComponentTypeDescriptor INSTANCE = new ComponentTypeDescriptor();

    @SuppressWarnings("unchecked")
    protected ComponentTypeDescriptor() {
        super(Component.class, ImmutableMutabilityPlan.INSTANCE);
    }

    @Override
    public String toString(Component value) {
        return ComponentSerializer.serialize(value);
    }

    @Override
    public Component fromString(String string) {
        return ComponentSerializer.deserialize(string);
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
}
