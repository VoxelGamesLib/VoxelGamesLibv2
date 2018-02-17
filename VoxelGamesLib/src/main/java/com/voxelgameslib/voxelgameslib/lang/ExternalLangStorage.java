package com.voxelgameslib.voxelgameslib.lang;

import java.io.File;
import javax.annotation.Nonnull;

public class ExternalLangStorage extends LangStorage {

    public void setLangFolder(@Nonnull File langFolder) {
        this.langFolder = langFolder;
    }
}
