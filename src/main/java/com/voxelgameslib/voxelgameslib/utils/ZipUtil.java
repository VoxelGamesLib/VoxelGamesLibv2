package com.voxelgameslib.voxelgameslib.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import java.io.File;
import javax.annotation.Nonnull;

/**
 * Small util to make working with zips easier
 */
public class ZipUtil {

    /**
     * Creates a zip from all files and folders in the specified folder. DOES NOT INCLUDE THE FOLDER
     * ITSELF!
     *
     * @param file the folder which content should be zipped
     * @return the created zip
     * @throws ZipException if something goes wrong
     */
    @Nonnull
    public static ZipFile createZip(@Nonnull File file) throws ZipException {
        ZipFile zip = new ZipFile(new File(file.getParent(), file.getName() + ".zip"));

        ZipParameters params = new ZipParameters();
        params.setIncludeRootFolder(false);
        zip.addFolder(file, params);

        return zip;
    }
}
