package com.voxelgameslib.voxelgameslib.persistence.converter;

import javax.persistence.AttributeConverter;

public interface VGLConverter<A, B> extends AttributeConverter<A, B> {

    void init();
}
