package com.voxelgameslib.voxelgameslib.internal.lang;

import java.util.UUID;
import javax.annotation.Nonnull;

public interface ExternalTranslatable extends Translatable {

    @Nonnull
    UUID getUuid();
}
